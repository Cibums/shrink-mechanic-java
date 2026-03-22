package dev.lucasfransson.shrinkmechanic.engine.rendering;
import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import javafx.scene.image.Image;

public class Renderable extends GameObject {

	private Image texture;
	private double spriteYOffset = 0;
	private int renderingLayer;

	public Renderable(Image texture) {
		this.texture = texture;
	}

	public Image getTexture() {
		if (texture != null) {
			return texture;
		}

		return new Image(
				Renderable.class.getResource("/default.png").toExternalForm());
	}

	public void setTexture(Image texture) {
		this.texture = texture;
	}

	public void setRenderingLayer(int layer) {
		this.renderingLayer = layer;
	}

	public double getRenderingZOffset() {
		return renderingLayer - this.getPosition().getY();
	}

	public static Image getTextureFromPath(String path) {
		var resource = Renderable.class.getResource(path);

		if (resource != null) {
			return new Image(resource.toExternalForm());
		}

		return null;
	}

	public Vector2 getSpriteSize() {
		double width = this.getTexture().getWidth();
		double height = this.getTexture().getHeight();
		return new Vector2(width, height);
	}

	public double getSpriteYOffset() {
		return spriteYOffset;
	}

	public void setSpriteYOffset(double spriteYOffset) {
		this.spriteYOffset = spriteYOffset;
	}
}
