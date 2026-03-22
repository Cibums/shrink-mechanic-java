package dev.lucasfransson.shrinkmechanic.world.tiles;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import javafx.scene.image.Image;

public abstract class Tile extends Renderable {

	public Tile(Image texture) {
		super(texture == null
				? new Image(Renderable.class.getResource("/default.png")
						.toExternalForm())
				: texture);
	}

	public abstract void onDestroy();
}
