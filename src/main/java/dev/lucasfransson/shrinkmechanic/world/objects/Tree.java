package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;

public class Tree extends WorldObject implements IRandomizable {

	public Tree() {
		super(Renderable.getTextureFromPath("/tree.png"));
		this.setSpriteAlignment(SpriteAlignment.BOTTOM);
		this.setSize(new Vector2(0.17, 0.17));
	}

	@Override
	public void randomize(Random rnd) {
		applyRandomization(rnd, 0.2, 0.9, 1.1);
	}

	@Override
	public void onDestroy() {
	}
}
