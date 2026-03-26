package dev.lucasfransson.shrinkmechanic.engine;

import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;

public class Cursor extends Renderable implements ITickable {

	private InputManager input;
	private GameCanvas canvas;

	public Cursor(InputManager input, GameCanvas canvas) {
		super(Renderable.getTextureFromPath("/cursor.png"));
		this.input = input;
		this.canvas = canvas;
		this.setRenderingLayer(1);
	}

	@Override
	public void update(double deltaTime) {

		Vector2 mousePos = input.getMousePosition();

		if (mousePos == null) {
			return;
		}

		Vector2Int worldMousePos = canvas.screenToWorld(mousePos);

		this.setPosition(worldMousePos);
	}
}
