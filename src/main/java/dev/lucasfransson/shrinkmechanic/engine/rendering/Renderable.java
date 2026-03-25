package dev.lucasfransson.shrinkmechanic.engine.rendering;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.lucasfransson.shrinkmechanic.engine.GameObject;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
import javafx.scene.image.Image;

public abstract class Renderable extends GameObject {

	private static final Map<String, Image> imageCache = new ConcurrentHashMap<>();
	private static final Image DEFAULT_IMAGE = new Image("default.png");

	private Image texture;
	private double spriteYOffset = 0;
	private boolean flipX = false;
	private int renderingLayer;

	private Animation currentAnimation = null;
	private boolean isPaused = true;

	private int currentFrame = 0;

	protected Renderable(Animation animation) {
		this(animation.getClips().getFirst());
		currentAnimation = animation;
		this.playAnimation();
	}

	protected Renderable(Image texture) {
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
		return imageCache.computeIfAbsent(path, p -> {
			var resource = Renderable.class.getResource(p);
			if (resource != null) {
				return new Image(resource.toExternalForm());
			}
			return DEFAULT_IMAGE;
		});
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

	public void setSpriteAlignment(SpriteAlignment alignment) {
		int grid = GameWorld.gridElementSize;
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

		int clipCount = currentAnimation.getClips().size();
		currentFrame = (int) Math
				.floor((elapsedTime % currentAnimation.getPlayTime())
						/ currentAnimation.getPlayTime() * clipCount);

		this.setTexture(currentAnimation.getClips().get(currentFrame));
	}
}
