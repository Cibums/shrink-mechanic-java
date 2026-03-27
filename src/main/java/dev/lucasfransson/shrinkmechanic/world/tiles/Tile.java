package dev.lucasfransson.shrinkmechanic.world.tiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.IRenderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import dev.lucasfransson.shrinkmechanic.world.IDestroyable;

public abstract class Tile extends GameObject
		implements
			IRenderable,
			IDestroyable {

	private final List<Sprite> sprites = new ArrayList<>();
	private boolean canBePlacedOn = true;

	protected Tile(Sprite sprite) {
		sprite.setSpriteAlignment(SpriteAlignment.CENTER);
		sprites.add(sprite);
		this.setSize(new Vector2(1.0, 1.0));
	}

	protected Sprite getMainSprite() {
		return sprites.getFirst();
	}

	protected void addSprite(Sprite sprite) {
		sprites.add(sprite);
	}

	@Override
	public List<Sprite> getSprites() {
		return Collections.unmodifiableList(sprites);
	}

	public boolean canBePlacedOn() {
		return canBePlacedOn;
	}

	public void setCanBePlacedOn(boolean canBePlacedOn) {
		this.canBePlacedOn = canBePlacedOn;
	}
}