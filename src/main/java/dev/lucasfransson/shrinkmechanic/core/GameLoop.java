package dev.lucasfransson.shrinkmechanic.core;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.CollisionSystem;
import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Camera;
import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.rendering.RenderSystem;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteEntry;
import dev.lucasfransson.shrinkmechanic.engine.tick.TickSystem;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

	private final GameCanvas canvas;
	private final TickSystem tickSystem;
	private final RenderSystem renderSystem;
	private final CollisionSystem collisionSystem;
	private final Camera camera;
	private final GameWorld world;

	public GameLoop(GameCanvas canvas, TickSystem tickSystem,
			RenderSystem renderSystem, CollisionSystem collisionSystem,
			Camera camera, GameWorld world) {
		this.canvas = canvas;
		this.tickSystem = tickSystem;
		this.renderSystem = renderSystem;
		this.collisionSystem = collisionSystem;
		this.camera = camera;
		this.world = world;
	}

	private long lastUpdate = System.nanoTime();

	@Override
	public void handle(long now) {
		double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
		lastUpdate = now;

		collisionSystem.updateDynamicPositions();
		tickSystem.update(deltaTime);
		world.updateChunks(camera.getPosition());

		double rangeX = canvas.getCanvasWidth()
				/ (GameConfig.GRID_CELL_SIZE * camera.getZoom());
		double rangeY = canvas.getCanvasHeight()
				/ (GameConfig.GRID_CELL_SIZE * camera.getZoom());

		List<SpriteEntry> visible = renderSystem
				.getSpriteEntriesInRange(camera.getPosition(), rangeX, rangeY);
		renderSystem.updateAnimations(deltaTime, visible);
		canvas.render(visible);
	}
}