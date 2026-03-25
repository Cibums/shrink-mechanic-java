package dev.lucasfransson.shrinkmechanic.world.objects;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.world.IDestroyable;
import javafx.scene.image.Image;

public abstract class WorldObject extends Renderable implements IDestroyable {

	protected WorldObject(Image texture) {
		super(texture == null
				? Renderable.getTextureFromPath("/default.png")
				: texture);

		this.setSize(new Vector2(0.3125, 0.3125));
		this.setRenderingLayer(1);
		this.setHasCollision(true);
	}
}
