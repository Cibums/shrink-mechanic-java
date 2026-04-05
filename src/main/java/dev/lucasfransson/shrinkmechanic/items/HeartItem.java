package dev.lucasfransson.shrinkmechanic.items;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.world.objects.PulsatingMushroom;

public class HeartItem extends Item implements IPlaceableItem<PulsatingMushroom> {

	public HeartItem() {
		super(new Sprite(Sprite.getTextureFromPath("/items/heart.png")));
	}

	@Override
	public String getName() {
		return "Heart";
	}

	@Override
	public PulsatingMushroom createWorldObject() {
		return new PulsatingMushroom();
	}

}
