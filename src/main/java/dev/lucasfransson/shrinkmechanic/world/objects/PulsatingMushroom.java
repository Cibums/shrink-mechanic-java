package dev.lucasfransson.shrinkmechanic.world.objects;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import javafx.scene.paint.Color;

public class PulsatingMushroom extends SignalEmitter {

	private final long emitIntervalTicks;
	private long lastEmitTick = -1;

	public PulsatingMushroom() {
		this(5, 20);
	}

	public PulsatingMushroom(int signalStrength, int bpm) {
		super(new Sprite(Sprite.getTextureFromPath("/mushroom.png")),
				signalStrength);
		this.emitIntervalTicks = Math.round(
				(double) GameConfig.TICK_RATE * 60.0 / bpm);
	}

	@Override
	protected void onTick(double deltaTime) {
		long currentTick = getTickCount();

		if (lastEmitTick < 0
				|| currentTick - lastEmitTick >= emitIntervalTicks) {
			emit();
			lastEmitTick = currentTick;
		}
	}

	@Override
	protected boolean schedulesUntriggers() {
		return true;
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
