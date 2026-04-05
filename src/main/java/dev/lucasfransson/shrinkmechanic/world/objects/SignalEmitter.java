package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.LongSupplier;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;

public abstract class SignalEmitter extends WorldObject
		implements IHasOutputs, ITickable {

	private static final int SIGNAL_DURATION_TICKS = GameConfig.TICK_RATE / 2;
	private static final Direction[] CARDINAL_ARRAY = Direction.CARDINAL
			.toArray(Direction[]::new);

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
	 * Called by carriers to propagate a signal along the chain.
	 * Passes {@code this} as the immediate source to downstream receivers.
	 *
	 * @param strength signal strength at this emitter
	 * @param visited  per-emit visited set for cycle prevention
	 */
	protected void emit(int strength, Set<SignalEmitter> visited) {
		this.signalStrength = strength;
		emitFrom(visited);
	}

	/**
	 * Called by root emitters (e.g. mushroom) to start a new signal chain.
	 * Creates a fresh visited set and uses itself as the source.
	 */
	protected void emit() {
		if (adjacentProvider == null || tickCountProvider == null) {
			throw new IllegalStateException(
					"SignalEmitter requires adjacentProvider and "
							+ "tickCountProvider to be set before emitting. "
							+ "Ensure the post-instantiate hook has run.");
		}
		Set<SignalEmitter> visited = new HashSet<>();
		visited.add(this);
		emitFrom(visited);
	}

	private void emitFrom(Set<SignalEmitter> visited) {

		onEmit();

		if (schedulesUntriggers()) {
			emitRemainingTicks = SIGNAL_DURATION_TICKS;
		}

		if (adjacentProvider == null || getGridPosition() == null)
			return;

		Direction[] outputDirs = resolveOutputArray();

		List<Map.Entry<Direction, WorldObject>> neighbors =
				adjacentProvider.apply(getGridPosition(), outputDirs);

		long currentTick = getTickCount();

		for (Map.Entry<Direction, WorldObject> entry : neighbors) {
			if (entry.getValue() instanceof ISignalReceiver receiver) {
				if (signalStrength <= 0 && receiver instanceof SignalCarrier)
					continue;
				Direction incomingDir = entry.getKey().opposite();
				if (receiver.getInputs().contains(incomingDir)) {
					receiver.trigger(this,
							Math.max(signalStrength - 1, 0), visited);
					if (schedulesUntriggers()) {
						pendingUntriggers.add(new PendingUntrigger(receiver,
								currentTick + SIGNAL_DURATION_TICKS));
					}
				}
			}
		}
	}

	/**
	 * Called by carriers to propagate untrigger with the immediate source
	 * preserved. Calls onUnemit() and walks the neighbor graph.
	 */
	protected void unemitFrom(SignalEmitter immediateSource) {
		onUnemit();
		cascadeUntrigger(immediateSource);
	}

	/**
	 * Propagates untrigger to neighbors without calling onUnemit on this
	 * emitter. Used when a carrier evicts one source but still has others.
	 *
	 * @param immediateSource the emitter whose signal is being withdrawn
	 */
	void cascadeUntrigger(SignalEmitter immediateSource) {
		if (adjacentProvider == null || getGridPosition() == null)
			return;

		Direction[] outputDirs = resolveOutputArray();

		List<Map.Entry<Direction, WorldObject>> neighbors =
				adjacentProvider.apply(getGridPosition(), outputDirs);

		for (Map.Entry<Direction, WorldObject> entry : neighbors) {
			if (entry.getValue() instanceof ISignalReceiver receiver) {
				Direction incomingDir = entry.getKey().opposite();
				if (receiver.getInputs().contains(incomingDir)) {
					receiver.untrigger(immediateSource);
				}
			}
		}
	}

	/**
	 * Called by root emitters to untrigger all neighbors using itself as source.
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
	 * override to untrigger downstream with correct immediate sources.
	 */
	protected void onSignalDestroy() {
		unemit();
	}

	@Override
	public final void update(double deltaTime) {
		onTick(deltaTime);

		long currentTick = getTickCount();

		for (int i = pendingUntriggers.size() - 1; i >= 0; i--) {
			PendingUntrigger pending = pendingUntriggers.get(i);
			if (currentTick >= pending.fireAtTick) {
				pending.receiver.untrigger(this);
				pendingUntriggers.remove(i);
			}
		}

		if (schedulesUntriggers() && emitRemainingTicks > 0) {
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

	@Override
	public List<Direction> getOutputs() {
		return Direction.CARDINAL;
	}

	/**
	 * Returns the output directions as an array. Uses the cached
	 * CARDINAL_ARRAY when outputs are the default CARDINAL list.
	 */
	private Direction[] resolveOutputArray() {
		List<Direction> outputs = getOutputs();
		if (outputs == Direction.CARDINAL) {
			return CARDINAL_ARRAY;
		}
		return outputs.toArray(Direction[]::new);
	}

	/**
	 * Pending untrigger scheduled by a root emitter. Uses an absolute
	 * tick count for deterministic timing (multiplayer-safe).
	 */
	private static class PendingUntrigger {
		final ISignalReceiver receiver;
		final long fireAtTick;

		PendingUntrigger(ISignalReceiver receiver, long fireAtTick) {
			this.receiver = receiver;
			this.fireAtTick = fireAtTick;
		}
	}

	public abstract void onEmit();
	public abstract void onUnemit();
}
