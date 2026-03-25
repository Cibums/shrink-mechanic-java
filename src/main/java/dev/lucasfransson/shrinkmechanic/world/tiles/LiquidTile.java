package dev.lucasfransson.shrinkmechanic.world.tiles;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Animation;
import javafx.scene.image.Image;

public abstract class LiquidTile extends Tile {

	protected LiquidTile(Image texture) {
		super(texture == null
				? new Image(LiquidTile.class.getResource("/liquid_default.png")
						.toExternalForm())
				: texture);

		this.setSpriteYOffset(-8);
		this.setHasCollision(true);
	}

	protected LiquidTile(Animation animation) {
		super(animation);

		this.setSpriteYOffset(-8);
		this.setHasCollision(true);
	}
}
