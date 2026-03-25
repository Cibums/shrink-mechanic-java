package dev.lucasfransson.shrinkmechanic.world.objects;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import javafx.scene.image.Image;

public abstract class WorldObject extends Renderable {

	public WorldObject(Image texture) {
		super(texture == null
				? new Image(Renderable.class.getResource("/default.png")
						.toExternalForm())
				: texture);
		this.setSize(new Vector2(10, 10));
		this.setRenderingLayer(1);
		this.setHasCollision(true);
	}

	public abstract void onDestroy();
}
