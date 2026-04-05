package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;

public class RootBundle extends SignalCarrier {

	public RootBundle() {
		super(new Sprite(Sprite.getTextureFromPath("/root.png")));
		this.setHasCollision(false);
		this.getMainSprite().setRenderingLayer(1);
	}

	@Override
	public List<Direction> getInputs() {
		return Direction.CARDINAL;
	}

}
