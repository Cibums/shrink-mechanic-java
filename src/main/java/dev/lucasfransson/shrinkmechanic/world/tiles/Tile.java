package dev.lucasfransson.shrinkmechanic.world.tiles;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import javafx.scene.image.Image;

public abstract class Tile extends Renderable {

	public Tile(Image texture) {
		super(texture == null
				? new Image(Renderable.class.getResource("/default.png")
						.toExternalForm())
				: texture);

		this.setSize(new Vector2(32, 32));
		this.setSpriteAlignment(SpriteAlignment.TOP);
		this.setHasCollision(false);
	}

	public abstract void onDestroy();
}
