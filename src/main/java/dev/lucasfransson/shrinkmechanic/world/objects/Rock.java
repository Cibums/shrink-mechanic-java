package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;

public class Rock extends WorldObject implements IRandomizable {

	public Rock() {
		super(Renderable.getTextureFromPath("/rock.png"));
		this.setSpriteAlignment(SpriteAlignment.CENTER);
		this.setSize(new Vector2(0.375, 0.20));
	}

	@Override
	public void randomize(Random rnd) {
		applyRandomization(rnd, 0.2, 0.9, 1.1);
	}

	@Override
	public void onDestroy() {
	}
}
