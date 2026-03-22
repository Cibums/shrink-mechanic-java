package dev.lucasfransson.shrinkmechanic.engine.rendering;
import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import javafx.scene.image.Image;

public class Renderable extends GameObject {

	private Image texture;
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
}
