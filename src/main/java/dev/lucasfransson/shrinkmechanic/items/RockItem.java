package dev.lucasfransson.shrinkmechanic.items;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;

public class RockItem extends Item {

	public RockItem() {
		super(new Sprite(Sprite.getTextureFromPath("/items/rock.png")));
	}

	@Override
	public String getName() {
		return "Rock";
	}

}
