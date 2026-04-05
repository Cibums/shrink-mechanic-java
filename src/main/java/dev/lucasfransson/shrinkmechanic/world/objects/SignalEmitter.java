package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.LongSupplier;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;

public abstract class SignalEmitter extends WorldObject
		implements IHasOutputs, ITickable {

	private static final int SIGNAL_DURATION_TICKS = GameConfig.TICK_RATE / 2;

	private int signalStrength;
	private AdjacentObjectLookup adjacentProvider;
	private LongSupplier tickCountProvider;
	private final List<PendingUntrigger> pendingUntriggers = new ArrayList<>();
	private int emitRemainingTicks = 0;

	protected SignalEmitter(Sprite sprite) {
		super(sprite);
	}

	protected SignalEmitter(Sprite sprite, int signalStrength) {
		super(sprite);
		this.signalStrength = signalStrength;
	}

	public void setAdjacentProvider(AdjacentObjectLookup adjacentProvider) {
		this.adjacentProvider = adjacentProvider;
	}

	public void setTickCountProvider(LongSupplier tickCountProvider) {
		this.tickCountProvider = tickCountProvider;
	}

	protected long getTickCount() {
		return tickCountProvider != null ? tickCountProvider.getAsLong() : 0;
	}

	public int getSignalStrength() {
		return signalStrength;
	}

	/**
	 * Called by carriers to propagate a signal with the original source preserved.
	 */
	protected void emit(int strength, SignalEmitter originalSource) {
		this.signalStrength = strength;
		emitFrom(originalSource);
	}

	/**
	 * Called by the root emitter (e.g. mushroom) to start a signal chain.
	 * Uses itself as the original source.
	 */
	protected void emit() {
		if (adjacentProvider == null || tickCountProvider == null) {
			throw new IllegalStateException(
					"SignalEmitter requires adjacentProvider and "
							+ "tickCountProvider to be set before emitting. "
							+ "Ensure the post-instantiate hook has run.");
		}
		emitFrom(this);
	}

	private void emitFrom(SignalEmitter originalSource) {

		onEmit();
		emitRemainingTicks = SIGNAL_DURATION_TICKS;

		if (adjacentProvider == null || getGridPosition() == null)
			return;

		List<Direction> outputs = getOutputs();

		List<Map.Entry<Direction, WorldObject>> neighbors =
				adjacentProvider.apply(getGridPosition(),
						outputs.toArray(Direction[]::new));

		for (Map.Entry<Direction, WorldObject> entry : neighbors) {
			if (entry.getValue() instanceof ISignalReceiver receiver) {
				if (signalStrength <= 0 && receiver instanceof SignalCarrier)
					continue;
				Direction incomingDir = entry.getKey().opposite();
				if (receiver.getInputs().contains(incomingDir)) {
					receiver.trigger(originalSource,
							Math.max(signalStrength - 1, 0));
					if (schedulesUntriggers()) {
						pendingUntriggers.add(new PendingUntrigger(receiver,
								SIGNAL_DURATION_TICKS));
					}
				}
			}
		}
	}

	/**
	 * Called by carriers to propagate untrigger with the original source preserved.
	 * Calls onUnemit() and walks the neighbor graph.
	 */
	protected void unemitFrom(SignalEmitter originalSource) {
		onUnemit();
		cascadeUntrigger(originalSource);
	}

	/**
	 * Propagates untrigger to neighbors without calling onUnemit on this
	 * emitter. Used when a source is evicted but the carrier stays active
	 * (still has other sources).
	 */
	void cascadeUntrigger(SignalEmitter originalSource) {
		if (adjacentProvider == null || getGridPosition() == null)
			return;

		List<Direction> outputs = getOutputs();

		List<Map.Entry<Direction, WorldObject>> neighbors =
				adjacentProvider.apply(getGridPosition(),
						outputs.toArray(Direction[]::new));

		for (Map.Entry<Direction, WorldObject> entry : neighbors) {
			if (entry.getValue() instanceof ISignalReceiver receiver) {
				Direction incomingDir = entry.getKey().opposite();
				if (receiver.getInputs().contains(incomingDir)) {
					receiver.untrigger(originalSource);
				}
			}
		}
	}

	/**
	 * Called by the root emitter to untrigger all neighbors using itself as source.
	 */
	protected void unemit() {
		unemitFrom(this);
	}

	@Override
	public final void onDestroy() {
		onSignalDestroy();
		for (PendingUntrigger pending : pendingUntriggers) {
			pending.receiver.untrigger(this);
		}
		pendingUntriggers.clear();
		super.onDestroy();
	}

	/**
	 * Called during onDestroy before pending untriggers are flushed and the
	 * super chain runs. Root emitters use the default (unemit). Carriers
	 * override to untrigger downstream with correct original sources.
	 */
	protected void onSignalDestroy() {
		unemit();
	}

	@Override
	public final void update(double deltaTime) {
		onTick(deltaTime);

		for (int i = pendingUntriggers.size() - 1; i >= 0; i--) {
			PendingUntrigger pending = pendingUntriggers.get(i);
			pending.remainingTicks--;
			if (pending.remainingTicks <= 0) {
				pending.receiver.untrigger(this);
				pendingUntriggers.remove(i);
			}
		}

		if (emitRemainingTicks > 0) {
			emitRemainingTicks--;
			if (emitRemainingTicks <= 0) {
				onUnemit();
			}
		}
	}

	/**
	 * Called every tick before untrigger processing. Override in root
	 * emitters for emission timing logic. No-op by default.
	 */
	protected void onTick(double deltaTime) {
	}

	/**
	 * Override to return true in root emitters that schedule untriggers
	 * for their immediate neighbors after emitting.
	 */
	protected boolean schedulesUntriggers() {
		return false;
	}

	public List<Direction> getOutputs() {
		return Direction.CARDINAL;
	}

	private static class PendingUntrigger {
		final ISignalReceiver receiver;
		int remainingTicks;

		PendingUntrigger(ISignalReceiver receiver, int remainingTicks) {
			this.receiver = receiver;
			this.remainingTicks = remainingTicks;
		}
	}

	public abstract void onEmit();
	public abstract void onUnemit();
}
