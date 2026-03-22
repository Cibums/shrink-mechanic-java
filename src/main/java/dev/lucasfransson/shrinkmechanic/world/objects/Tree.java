package dev.lucasfransson.shrinkmechanic.world.objects;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;

public class Tree extends WorldObject {

	public Tree() {
		super(Renderable.getTextureFromPath("/tree.png"));

		this.setSpriteYOffset(-100);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

}
