package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import javafx.scene.paint.Color;

public class Flowers extends WorldObject implements IRandomizable {

	private static final Color[] COLORS = {Color.web("#D4A940"),
			Color.web("#8B6DB0"), Color.web("#C75D6E")};

	public Flowers() {
		super(Renderable.getTextureFromPath("/flowers.png"));
		this.setSpriteAlignment(SpriteAlignment.CENTER);
		this.setSize(new Vector2(0.5, 0.5));
		this.setTint(Color.RED);
		this.setHasCollision(false);
	}

	@Override
	public void onDestroy() {

	}

	@Override
	public void randomize(Random rnd) {
		applyTintRandomization(rnd, COLORS);
		applyColorOffsetRandomization(rnd, 0.1);
		applyPositionRandomization(rnd, 0.2);
	}

}
