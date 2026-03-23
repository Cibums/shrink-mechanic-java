package dev.lucasfransson.shrinkmechanic.world.objects;

import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;

public class Tree extends WorldObject {

	public Tree() {
		super(Renderable.getTextureFromPath("/tree.png"));

		this.setSpriteYOffset(
				(this.getTexture().getHeight() - GameWorld.gridElementSize)
						+ GameWorld.gridElementSize / 2);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

}
