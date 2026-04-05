package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import javafx.scene.paint.Color;

public class Flowers extends WorldObject
		implements
			IRandomizable,
			ISignalReceiver {

	private static final Color[] COLORS = {Color.web("#D4A940"),
			Color.web("#8B6DB0"), Color.web("#C75D6E")};
	private final Set<SignalEmitter> triggeredBy = new HashSet<>();

	public Flowers() {
		super(new Sprite(
				List.of(Sprite.getTextureFromPath("/flowers/flowers_1.png"),
						Sprite.getTextureFromPath("/flowers/flowers_2.png"))));
		getMainSprite().setSpriteAlignment(SpriteAlignment.CENTER);
		getMainSprite().setRenderingLayer(1);
		this.setSize(new Vector2(0.5, 0.5));
		this.setHasCollision(false);
	}

	@Override
	public void randomize(Random rnd) {
		getMainSprite().selectVariant(rnd);
		getMainSprite().setFlipX(rnd.nextBoolean());
		getMainSprite().randomizeTint(rnd, COLORS);
		getMainSprite().randomizeColorOffset(rnd, 0.06);
		applyPositionRandomization(rnd, 0.2);
	}

	@Override
	public List<Direction> getInputs() {
		return List.of(Direction.ALL);
	}

	@Override
	public void trigger(SignalEmitter source, int strength) {
		triggeredBy.add(source);
		this.getMainSprite().setTint(Color.LIME);
	}

	@Override
	public void untrigger(SignalEmitter source) {
		triggeredBy.remove(source);
		if (triggeredBy.isEmpty()) {
			this.getMainSprite().clearTint();
		}
	}
}
