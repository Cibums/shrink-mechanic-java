package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.HashSet;
import java.util.Set;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import javafx.scene.paint.Color;

public abstract class SignalCarrier extends SignalEmitter
		implements
			ISignalReciever {

	private final Set<SignalEmitter> triggeredBy = new HashSet<>();

	protected SignalCarrier(Sprite sprite) {
		super(sprite);
	}

	@Override
	public void trigger(SignalEmitter source, int strength) {
		boolean isNew = triggeredBy.add(source);
		if (!isNew && strength <= getSignalStrength())
			return;

		emit(strength, source);
	}

	@Override
	public void untrigger(SignalEmitter source) {
		if (!triggeredBy.remove(source))
			return;

		if (!triggeredBy.isEmpty())
			return;

		unemitFrom(source);
	}

	@Override
	public void onDestroy() {
		for (SignalEmitter source : triggeredBy) {
			unemitFrom(source);
		}
		triggeredBy.clear();
		super.onDestroy();
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
