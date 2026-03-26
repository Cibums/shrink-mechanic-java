package dev.lucasfransson.shrinkmechanic.entities;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.IRenderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;

public class RemotePlayer extends GameObject implements IRenderable {

	private final List<Sprite> sprites;

	public RemotePlayer() {
		Sprite sprite = new Sprite(Sprite.getTextureFromPath("/player.png"));
		sprite.setSpriteAlignment(SpriteAlignment.TOP);
		this.sprites = List.of(sprite);
		this.setSize(new Vector2(0.2, 0.2));
	}

	@Override
	public List<Sprite> getSprites() {
		return sprites;
	}
}