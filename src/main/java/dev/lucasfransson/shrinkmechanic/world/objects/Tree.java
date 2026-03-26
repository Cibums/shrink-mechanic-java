package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;

public class Tree extends WorldObject implements IRandomizable {

	public Tree() {
		super(new Sprite(Sprite.getTextureFromPath("/tree.png")));
		getMainSprite().setSpriteAlignment(SpriteAlignment.BOTTOM);
		this.setSize(new Vector2(0.17, 0.17));
	}

	@Override
	public void randomize(Random rnd) {
		getMainSprite().setFlipX(rnd.nextBoolean());
		getMainSprite().randomizeColorOffset(rnd, 0.15);
		applyPositionRandomization(rnd, 0.2);
		applySizeRandomization(rnd, 0.9, 1.1);
	}

	@Override
	public void onDestroy() {
	}
}