package dev.lucasfransson.shrinkmechanic.engine.rendering;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import javafx.scene.image.Image;

public abstract class Renderable extends GameObject {

	private static final Map<String, Image> imageCache = new ConcurrentHashMap<>();
	private static final Image DEFAULT_IMAGE = new Image("default.png");

	private Image texture;
	private double spriteYOffset = 0;
	private Vector2 spriteSize;
	private boolean flipX = false;
	private int renderingLayer;

	private Animation currentAnimation = null;
	private boolean isPaused = true;

	protected Renderable(Animation animation) {
		this(animation.getFrames().getFirst());
		currentAnimation = animation;
		this.playAnimation();
	}

	protected Renderable(Image texture) {
		this.setTexture(texture);
	}

	public Image getTexture() {
		if (texture != null) {
			return texture;
		}

		return DEFAULT_IMAGE;
	}

	public void setTexture(Image texture) {
		this.texture = texture;
		this.spriteSize = new Vector2(texture.getWidth(), texture.getHeight());
	}

	public void setRenderingLayer(int layer) {
		this.renderingLayer = layer;
	}

	public double getRenderingZOffset() {
		return renderingLayer - this.getPosition().getY();
	}

	public static Image getTextureFromPath(String path) {
		return imageCache.computeIfAbsent(path, p -> {
			var resource = Renderable.class.getResource(p);
			if (resource != null) {
				return new Image(resource.toExternalForm());
			}
			return DEFAULT_IMAGE;
		});
	}

	public Vector2 getSpriteSize() {
		return spriteSize;
	}

	public double getSpriteYOffset() {
		return spriteYOffset;
	}

	public void setSpriteYOffset(double spriteYOffset) {
		this.spriteYOffset = spriteYOffset;
	}

	public void setSpriteAlignment(SpriteAlignment alignment) {
		int grid = GameConfig.GRID_CELL_SIZE;
		double spriteH = this.getTexture().getHeight();

		double offset = switch (alignment) {
			case TOP -> 0;
			case CENTER -> (spriteH - grid) / 2.0;
			case BOTTOM -> spriteH - grid;
		};

		this.setSpriteYOffset(offset);
	}

	public boolean getFlipX() {
		return flipX;
	}

	public void setFlipX(boolean flipX) {
		this.flipX = flipX;
	}

	public void playAnimation() {
		this.isPaused = false;
	}

	public void pauseAnimation() {
		this.isPaused = true;
	}

	public void setAnimation(Animation animation) {
		this.currentAnimation = animation;
	}

	public Animation getCurrentAnimation() {
		return currentAnimation;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void updateAnimationFrame(double elapsedTime, double deltaTime) {
		List<Image> frames = currentAnimation.getFrames();
		int frameCount = frames.size();
		this.setTexture(frames.get(
				(int) Math.floor((elapsedTime % currentAnimation.getPlayTime())
						/ currentAnimation.getPlayTime() * frameCount)));
	}
}
