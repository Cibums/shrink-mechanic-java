package dev.lucasfransson.shrinkmechanic.engine;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.rendering.IRenderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;

public class Cursor extends GameObject implements IRenderable, ITickable {

	private final List<Sprite> sprites;
	private final InputManager input;
	private final GameCanvas canvas;

	public Cursor(InputManager input, GameCanvas canvas) {
		Sprite sprite = new Sprite(Sprite.getTextureFromPath("/cursor.png"));
		sprite.setRenderingLayer(1);
		this.sprites = List.of(sprite);
		this.input = input;
		this.canvas = canvas;
	}

	@Override
	public List<Sprite> getSprites() {
		return sprites;
	}

	@Override
	public void update(double deltaTime) {
		Vector2 mousePos = input.getMousePosition();
		if (mousePos == null)
			return;
		Vector2Int worldMousePos = canvas.screenToWorld(mousePos);
		this.setPosition(worldMousePos);
	}
}