package dev.lucasfransson.shrinkmechanic.entities;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import dev.lucasfransson.shrinkmechanic.items.Inventory;
import dev.lucasfransson.shrinkmechanic.items.ItemDrop;

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
	public List<ItemDrop> getDrops() {
		ItemDrop drop = ItemDrop.fromList(inventory.getItems());
		return drop != null ? List.of(drop) : List.of();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	public Inventory getInventory() {
		return inventory;
	}
}
