package dev.lucasfransson.shrinkmechanic.world.tiles;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Animation;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import javafx.scene.image.Image;

public abstract class Tile extends Renderable {

	protected Tile(Image texture) {
		super(texture == null
				? new Image(Renderable.class.getResource("/default.png")
						.toExternalForm())
				: texture);

		this.setSize(new Vector2(1.0, 1.0));
		this.setSpriteAlignment(SpriteAlignment.TOP);
		this.setHasCollision(false);
	}

	protected Tile(Animation animation) {
		super(animation);
	}

	public abstract void onDestroy();
}
