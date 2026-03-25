package dev.lucasfransson.shrinkmechanic.core;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Camera;
import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.rendering.RenderSystem;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.tick.TickSystem;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

	private final GameCanvas canvas;
	private final TickSystem tickSystem;
	private final RenderSystem renderSystem;
	private final Camera camera;

	public GameLoop(GameCanvas canvas, TickSystem tickSystem,
			RenderSystem renderSystem, Camera camera) {
		this.canvas = canvas;
		this.tickSystem = tickSystem;
		this.renderSystem = renderSystem;
		this.camera = camera;
	}

	private long lastUpdate = System.nanoTime();

	@Override
	public void handle(long now) {
		double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
		lastUpdate = now;

		tickSystem.update(deltaTime);

		double rangeX = canvas.getCanvasWidth()
				/ (GameConfig.GRID_CELL_SIZE * canvas.getZoom());
		double rangeY = canvas.getCanvasHeight()
				/ (GameConfig.GRID_CELL_SIZE * canvas.getZoom());
		List<Renderable> visible = renderSystem
				.getRenderablesInRange(camera.getPosition(), rangeX, rangeY);

		renderSystem.updateAnimations(deltaTime, visible);
		canvas.render(visible);
	}

}
