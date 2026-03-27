package dev.lucasfransson.shrinkmechanic.entities;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;

public abstract class Player extends Entity {

	protected Player() {
		super(new Sprite(Sprite.getTextureFromPath("/player.png")));
		getMainSprite().setSpriteAlignment(SpriteAlignment.TOP);
		this.setSize(new Vector2(0.1, 0.05));
		this.getMainSprite().setRenderingLayer(2);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}
}
