package dev.lucasfransson.shrinkmechanic.core;

import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.tick.TickSystem;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

	private GameCanvas canvas;
	private TickSystem tickSystem;

	public GameLoop(GameCanvas canvas, TickSystem tickSystem) {
		this.canvas = canvas;
		this.tickSystem = tickSystem;
	}

	long lastUpdate = System.nanoTime();

	@Override
	public void handle(long now) {
		double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // convert to
																	// seconds
		lastUpdate = now;

		tickSystem.update(deltaTime);
		canvas.render();
	}

}
