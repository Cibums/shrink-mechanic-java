package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import javafx.scene.paint.Color;

public abstract class SignalCarrier extends SignalEmitter
		implements
			ISignalReceiver {

	private final Map<SignalEmitter, Integer> triggeredBy = new HashMap<>();

	protected SignalCarrier(Sprite sprite) {
		super(sprite);
	}

	@Override
	public void trigger(SignalEmitter source, int strength) {
		Integer previous = triggeredBy.get(source);
		if (previous != null && strength <= previous)
			return;

		triggeredBy.put(source, strength);

		int maxStrength = triggeredBy.values().stream()
				.mapToInt(Integer::intValue).max().orElse(0);
		emit(maxStrength, source);
	}

	@Override
	public void untrigger(SignalEmitter source) {
		if (triggeredBy.remove(source) == null)
			return;

		if (triggeredBy.isEmpty()) {
			unemitFrom(source);
			return;
		}

		int maxStrength = triggeredBy.values().stream()
				.mapToInt(Integer::intValue).max().orElse(0);
		emit(maxStrength, source);
	}

	@Override
	protected void onSignalDestroy() {
		for (SignalEmitter source : new ArrayList<>(triggeredBy.keySet())) {
			unemitFrom(source);
		}
		triggeredBy.clear();
	}

	public void onEmit() {
		double t = Math.clamp(getSignalStrength() / 5.0, 0.0, 1.0);
		Color color = Color.TRANSPARENT.interpolate(Color.LIME, t);

		this.getMainSprite().setTint(color);
	}

	public void onUnemit() {
		this.getMainSprite().clearTint();
	}
}
