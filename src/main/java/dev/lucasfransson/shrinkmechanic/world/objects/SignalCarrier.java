package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import javafx.scene.paint.Color;

public abstract class SignalCarrier extends SignalEmitter
		implements
			ISignalReceiver {

	/**
	 * Tracks the signal strength received from each immediate upstream
	 * neighbor. Keyed on the direct source (not the root emitter) so that
	 * diamond/converging topologies register each path independently.
	 */
	private final Map<SignalEmitter, Integer> triggeredBy = new HashMap<>();

	protected SignalCarrier(Sprite sprite) {
		super(sprite);
	}

	@Override
	public void trigger(SignalEmitter source, int strength,
			Set<SignalEmitter> visited) {
		Integer previous = triggeredBy.get(source);
		triggeredBy.put(source, strength);

		int newMax = triggeredBy.values().stream()
				.mapToInt(Integer::intValue).max().orElse(0);

		// Skip if no effective change (same source, same or lower strength)
		if (previous != null && strength <= previous)
			return;

		// Cycle prevention: stop if we've already been visited this emit
		if (!visited.add(this))
			return;

		emit(newMax, visited);
	}

	@Override
	public void untrigger(SignalEmitter source) {
		if (triggeredBy.remove(source) == null)
			return;

		if (triggeredBy.isEmpty()) {
			unemitFrom(this);
			return;
		}

		// Source removed but others remain — withdraw the old path's
		// signal downstream, then re-emit at the new max strength so
		// downstream nodes see the correct value.
		cascadeUntrigger(this);

		int maxStrength = triggeredBy.values().stream()
				.mapToInt(Integer::intValue).max().orElse(0);
		emit(maxStrength, new HashSet<>());
	}

	@Override
	protected void onSignalDestroy() {
		if (!triggeredBy.isEmpty()) {
			unemitFrom(this);
			triggeredBy.clear();
		}
	}

	@Override
	public void onEmit() {
		double t = Math.clamp(getSignalStrength() / 5.0, 0.0, 1.0);
		Color color = Color.TRANSPARENT.interpolate(Color.LIME, t);

		this.getMainSprite().setTint(color);
	}

	@Override
	public void onUnemit() {
		this.getMainSprite().clearTint();
	}
}
