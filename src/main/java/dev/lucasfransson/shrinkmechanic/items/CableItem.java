package dev.lucasfransson.shrinkmechanic.items;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.world.objects.RootBundle;

public class CableItem extends Item implements IPlaceableItem<RootBundle> {

	public CableItem() {
		super(new Sprite(Sprite.getTextureFromPath("/items/cable.png")));
	}

	@Override
	public String getName() {
		return "Cable";
	}

	@Override
	public RootBundle createWorldObject() {
		return new RootBundle();
	}

}
