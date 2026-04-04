package dev.lucasfransson.shrinkmechanic.core;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import dev.lucasfransson.shrinkmechanic.items.IPlaceableItem;
import dev.lucasfransson.shrinkmechanic.items.Item;
import dev.lucasfransson.shrinkmechanic.items.SaplingItem;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
import dev.lucasfransson.shrinkmechanic.world.ReplacementMode;

public class PlayerInteraction implements ITickable {

	private final InputManager input;
	private final GameCanvas canvas;
	private final GameWorld world;

	private Item selectedItem = new SaplingItem();

	private Item selectedItemCache = null;
	private Vector2Int mouseTilePositionCache = null;

	public PlayerInteraction(InputManager input, GameCanvas canvas,
			GameWorld world) {
		this.input = input;
		this.canvas = canvas;
		this.world = world;
	}

	@Override
	public void update(double deltaTime) {

		if (selectedItem instanceof IPlaceableItem placeableItem) {

			Vector2 mousePos = input.getMousePosition();
			Vector2Int tilePosition = null;

			if (mousePos != null) {

				tilePosition = canvas.screenToWorld(mousePos);

				boolean updatePreview = this.selectedItem != this.selectedItemCache
						|| mouseTilePositionCache != tilePosition;

				if (updatePreview) {
					world.previewWorldObject(tilePosition,
							placeableItem.createWorldObject(),
							ReplacementMode.KEEP);

					this.selectedItemCache = selectedItem;
					this.mouseTilePositionCache = tilePosition;
				}
			}

			Vector2 left = input.consumeLeftClick();
			if (left != null) {
				tilePosition = canvas.screenToWorld(left);
				world.placeWorldObject(tilePosition,
						placeableItem.createWorldObject(),
						ReplacementMode.KEEP);
			}

		}

		Vector2 right = input.consumeRightClick();
		if (right != null) {
			Vector2Int tile = canvas.screenToWorld(right);
			world.destroyWorldObject(tile);
		}

	}
}