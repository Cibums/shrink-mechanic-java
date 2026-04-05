package dev.lucasfransson.shrinkmechanic.world.objects;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import javafx.scene.paint.Color;

public class PulsatingMushroom extends SignalEmitter implements ITickable {

	private final int bpm;

	public PulsatingMushroom() {
		this(5, 20);
	}

	public PulsatingMushroom(int signalStrength, int bpm) {
		super(new Sprite(Sprite.getTextureFromPath("/mushroom.png")),
				signalStrength);
		this.bpm = bpm;
	}

	private double time = 0;

	@Override
	public void update(double deltaTime) {

		if (time >= 60.0 / bpm) {

			emit();

			time = 0;
		}

		time += deltaTime;
		updateUntriggers(deltaTime);
	}

	@Override
	public void onEmit() {
		this.getMainSprite().setTint(Color.LIME);
	}

	@Override
	public void onUnemit() {
		this.getMainSprite().clearTint();
	}

}
