package dev.lucasfransson.shrinkmechanic.world.objects;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;

public class Tree extends WorldObject {

	public Tree() {
		super(Renderable.getTextureFromPath("/tree.png"));
		this.setSpriteAlignment(SpriteAlignment.BOTTOM);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

}
