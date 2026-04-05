package dev.lucasfransson.shrinkmechanic.core;

import dev.lucasfransson.shrinkmechanic.engine.ObjectRegistry;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import dev.lucasfransson.shrinkmechanic.items.CableItem;
import dev.lucasfransson.shrinkmechanic.items.HeartItem;
import dev.lucasfransson.shrinkmechanic.items.IPlaceableItem;
import dev.lucasfransson.shrinkmechanic.items.Item;
import dev.lucasfransson.shrinkmechanic.items.SaplingItem;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
import dev.lucasfransson.shrinkmechanic.world.ReplacementMode;
import dev.lucasfransson.shrinkmechanic.world.objects.PreviewObject;
import dev.lucasfransson.shrinkmechanic.world.objects.WorldObject;
import javafx.scene.input.KeyCode;

public class PlayerInteraction implements ITickable {

	private final InputManager input;
	private final GameCanvas canvas;
	private final GameWorld world;
	private final ObjectRegistry registry;

	private Item selectedItem = new HeartItem();

	private Item selectedItemCache = null;
	private Vector2Int mouseTilePositionCache = null;
	private WorldObject previewWorldObject = null;
	private PreviewObject previewObject = null;

	public PlayerInteraction(InputManager input, GameCanvas canvas,
			GameWorld world, ObjectRegistry registry) {
		this.input = input;
		this.canvas = canvas;
		this.world = world;
		this.registry = registry;
	}

	@Override
	public void update(double deltaTime) {

		if (input.isKeyHeld(KeyCode.DIGIT1)) {
			selectedItem = new SaplingItem();
		}

		if (input.isKeyHeld(KeyCode.DIGIT2)) {
			selectedItem = new HeartItem();
		}

		if (input.isKeyHeld(KeyCode.DIGIT3)) {
			selectedItem = new CableItem();
		}

		if (selectedItem instanceof IPlaceableItem placeableItem) {

			Vector2 mousePos = input.getMousePosition();
			Vector2Int tilePosition = null;

			if (mousePos != null) {

				tilePosition = canvas.screenToWorld(mousePos);

				boolean itemChanged = this.selectedItem != this.selectedItemCache;
				boolean tileChanged = !tilePosition
						.equals(mouseTilePositionCache);

				if (itemChanged) {
					previewWorldObject = placeableItem.createWorldObject();
					this.selectedItemCache = selectedItem;
				}

				if (itemChanged || tileChanged) {
					updatePreview(tilePosition);
					this.mouseTilePositionCache = tilePosition;
				}
			} else {
				if (previewObject != null)
					previewObject.hide();
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

	private void updatePreview(Vector2Int tilePosition) {
		if (previewObject == null) {
			previewObject = registry.instantiate(new PreviewObject());
		}

		if (previewWorldObject == null) {
			previewObject.hide();
			return;
		}

		if (!world.canPlaceWorldObjectAt(tilePosition, ReplacementMode.KEEP)) {
			previewObject.hide();
			return;
		}

		previewWorldObject.setPosition(tilePosition);
		previewObject.setWorldObject(previewWorldObject);
		previewObject.show();
	}
}
