package dev.lucasfransson.shrinkmechanic.entities;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;

public class RemotePlayer extends Renderable {
	public RemotePlayer() {
		super(Renderable.getTextureFromPath("/player.png"));
		this.setSize(new Vector2(0.2, 0.2));
		this.setRenderingLayer(1);
		this.setSpriteAlignment(SpriteAlignment.TOP);
	}
}