package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.IPositioned;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.IRenderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;

public class PreviewObject implements IRenderable, IPositioned {

	private Vector2 position;
	private final Sprite sprite;

	public PreviewObject() {
		this.position = Vector2.zero();
		this.sprite = new Sprite();
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public List<Sprite> getSprites() {
		return List.of(sprite);
	}

	public void show() {
		this.sprite.setOpacity(0.5);
	}

	public void hide() {
		this.sprite.setOpacity(0.0);
	}

	public void setWorldObject(WorldObject obj) {
		this.position = obj.getPosition();
		Sprite source = obj.getSprites().getFirst();
		this.sprite.setTexture(source.getTexture());
		this.sprite.setSpriteYOffset(source.getSpriteYOffset());
		this.sprite.setScale(source.getScale());
		this.sprite.setOffset(source.getOffset());
		this.sprite.setRenderingLayer(source.getRenderingLayer());
	}
}
