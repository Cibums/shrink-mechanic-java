package dev.lucasfransson.shrinkmechanic.items;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.world.objects.Tree;

public class SaplingItem extends Item implements IPlaceableItem<Tree> {

	public SaplingItem() {
		super(new Sprite(Sprite.getTextureFromPath("/items/sapling.png")));
	}

	@Override
	public String getName() {
		return "Sapling";
	}

	@Override
	public Tree createWorldObject() {
		return new Tree();
	}

}
