package dev.lucasfransson.shrinkmechanic.core;

import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.rendering.RenderSystem;
import dev.lucasfransson.shrinkmechanic.engine.tick.TickSystem;
import dev.lucasfransson.shrinkmechanic.entities.Player;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

	private GameCanvas canvas;
	private TickSystem tickSystem;
	private RenderSystem renderSystem;
	private Player player;

	public GameLoop(GameCanvas canvas, TickSystem tickSystem,
			RenderSystem renderSystem, Player player) {
		this.canvas = canvas;
		this.tickSystem = tickSystem;
		this.renderSystem = renderSystem;
		this.player = player;
	}

	long lastUpdate = System.nanoTime();

	@Override
	public void handle(long now) {
		double deltaTime = (now - lastUpdate) / 1_000_000_000.0;

		lastUpdate = now;

		tickSystem.update(deltaTime);

		renderSystem.updateAnimations(deltaTime, player.getPosition(),
				canvas.getCanvasWidth()
						/ (GameWorld.gridElementSize * canvas.getZoom()),
				canvas.getCanvasHeight()
						/ (GameWorld.gridElementSize * canvas.getZoom()));

		canvas.render();
	}

}
