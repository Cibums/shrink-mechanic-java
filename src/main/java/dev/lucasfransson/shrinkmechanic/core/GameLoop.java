package dev.lucasfransson.shrinkmechanic.core;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Camera;
import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.rendering.RenderSystem;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.tick.TickSystem;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
import dev.lucasfransson.shrinkmechanic.world.ReplacementMode;
import dev.lucasfransson.shrinkmechanic.world.objects.Tree;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

	private final GameCanvas canvas;
	private final TickSystem tickSystem;
	private final RenderSystem renderSystem;
	private final Camera camera;
	private final GameWorld world;
	private final InputManager input;

	public GameLoop(GameCanvas canvas, TickSystem tickSystem,
			RenderSystem renderSystem, Camera camera, GameWorld world,
			InputManager input) {
		this.canvas = canvas;
		this.tickSystem = tickSystem;
		this.renderSystem = renderSystem;
		this.camera = camera;
		this.world = world;
		this.input = input;
	}

	private long lastUpdate = System.nanoTime();

	@Override
	public void handle(long now) {
		double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
		lastUpdate = now;

		Vector2 left = input.consumeLeftClick();
		if (left != null) {
			Vector2Int tilePosition = canvas.screenToWorld(left);
			world.placeWorldObject(tilePosition, new Tree(),
					ReplacementMode.KEEP);
		}

		Vector2 right = input.consumeRightClick();
		if (right != null) {
			Vector2Int tile = canvas.screenToWorld(right);
			world.destroyWorldObject(tile);
		}

		tickSystem.update(deltaTime);
		world.updateChunks(camera.getPosition());

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
