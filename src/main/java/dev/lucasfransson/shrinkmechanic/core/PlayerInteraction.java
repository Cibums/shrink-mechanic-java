package dev.lucasfransson.shrinkmechanic.core;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
import dev.lucasfransson.shrinkmechanic.world.ReplacementMode;
import dev.lucasfransson.shrinkmechanic.world.objects.Tree;

public class PlayerInteraction implements ITickable {

	private final InputManager input;
	private final GameCanvas canvas;
	private final GameWorld world;

	public PlayerInteraction(InputManager input, GameCanvas canvas,
			GameWorld world) {
		this.input = input;
		this.canvas = canvas;
		this.world = world;
	}

	@Override
	public void update(double deltaTime) {
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
	}
}