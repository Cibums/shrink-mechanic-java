package dev.lucasfransson.shrinkmechanic.world.tiles;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Animation;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.rendering.SpriteAlignment;
import dev.lucasfransson.shrinkmechanic.world.IDestroyable;
import javafx.scene.image.Image;

public abstract class Tile extends Renderable implements IDestroyable {

	private boolean canBePlacedOn = true;

	protected Tile(Image texture) {
		super(texture == null
				? Renderable.getTextureFromPath("/default.png")
				: texture);

		initTile();
	}

	protected Tile(Animation animation) {
		super(animation);
		initTile();
	}

	private void initTile() {
		this.setSize(new Vector2(1.0, 1.0));
		this.setSpriteAlignment(SpriteAlignment.CENTER);
		this.setHasCollision(false);
	}

	public boolean canBePlacedOn() {
		return canBePlacedOn;
	}

	public void setCanBePlacedOn(boolean canBePlacedOn) {
		this.canBePlacedOn = canBePlacedOn;
	}
}
