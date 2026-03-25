package dev.lucasfransson.shrinkmechanic.world.objects;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;

public class Tree extends WorldObject {

	public Tree() {
		super(Renderable.getTextureFromPath("/tree.png"));
		this.setSpriteAlignment(SpriteAlignment.BOTTOM);
		this.setSize(new Vector2(0.25, 0.1875));
	}

	@Override
	public void onDestroy() {

	}

}
