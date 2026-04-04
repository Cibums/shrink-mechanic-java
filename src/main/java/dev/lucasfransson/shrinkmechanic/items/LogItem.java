package dev.lucasfransson.shrinkmechanic.items;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;

public class LogItem extends Item {

	public LogItem() {
		super(new Sprite(Sprite.getTextureFromPath("/items/log.png")));
	}

	@Override
	public String getName() {
		return "Log";
	}

}
