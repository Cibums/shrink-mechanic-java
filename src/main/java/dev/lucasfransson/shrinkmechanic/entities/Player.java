package dev.lucasfransson.shrinkmechanic.entities;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import dev.lucasfransson.shrinkmechanic.items.Inventory;
import dev.lucasfransson.shrinkmechanic.items.Item;

public abstract class Player extends Entity {

	private Inventory inventory;

	protected Player() {
		super(new Sprite(Sprite.getTextureFromPath("/player.png")));
		getMainSprite().setSpriteAlignment(SpriteAlignment.TOP);
		this.setSize(new Vector2(0.2, 0.1));
		this.getMainSprite().setRenderingLayer(2);
		this.inventory = new Inventory();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Vector2 pos = getPosition();
		for (Item item : inventory.getItems()) {
			DroppedItem di = new DroppedItem(item);
			di.setPosition(pos);
			spawn(di);
		}
	}

	public Inventory getInventory() {
		return inventory;
	}
}
