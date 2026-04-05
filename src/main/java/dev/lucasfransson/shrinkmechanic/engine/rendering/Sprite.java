package dev.lucasfransson.shrinkmechanic.engine.rendering;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Sprite {

	private static final Map<String, Image> imageCache = new ConcurrentHashMap<>();
	private static final int TINT_CACHE_MAX_SIZE = 1024;
	private static final Map<String, Image> tintedImageCache = Collections
			.synchronizedMap(
					new LinkedHashMap<>(TINT_CACHE_MAX_SIZE, 0.75f, true) {
						@Override
						protected boolean removeEldestEntry(
								Map.Entry<String, Image> eldest) {
							return size() > TINT_CACHE_MAX_SIZE;
						}
					});
	private static final Image DEFAULT_IMAGE = new Image("default.png");

	private final List<Image> variants;
	private Image texture;
	private Vector2 spriteSize;
	private Vector2 offset;
	private double spriteYOffset = 0;
	private double scale = 1.0;
	private boolean flipX = false;
	private int renderingLayer = 0;

	private Animation currentAnimation = null;
	private boolean isPaused = true;
	private boolean synced = true;
	private double localElapsed = 0.0;
	private boolean looping = true;

	private Color tint;
	private double opacity = 1.0;

	public Sprite() {
		this(DEFAULT_IMAGE);
	}

	public Sprite(Image texture) {
		this.variants = List.of(texture);
		applyTexture(texture);
		this.offset = Vector2.zero();
	}

	public Sprite(List<Image> variants) {
		if (variants == null || variants.isEmpty()) {
			throw new IllegalArgumentException(
					"Variants list must not be empty");
		}
		this.variants = List.copyOf(variants);
		applyTexture(this.variants.getFirst());
		this.offset = Vector2.zero();
	}

	public Sprite(Animation animation) {
		this.variants = List.of(animation.getFrames().getFirst());
		applyTexture(this.variants.getFirst());
		this.offset = Vector2.zero();
		this.currentAnimation = animation;
		this.playAnimation();
	}

	private void applyTexture(Image image) {
		this.texture = image;
		this.spriteSize = new Vector2(image.getWidth(), image.getHeight());
	}

	public Sprite copyAppearance() {
		Sprite ret = new Sprite(this.getTexture());

		ret.setScale(scale);
		ret.setSpriteYOffset(spriteYOffset);
		ret.setOffset(offset);
		ret.setAnimation(currentAnimation);
		ret.setFlipX(flipX);
		ret.setLooping(looping);
		ret.setOpacity(opacity);
		ret.setRenderingLayer(renderingLayer);
		ret.setSynced(synced);
		ret.setTint(tint);

		return ret;
	}

	// --- Variants ---

	public void selectVariant(Random rnd) {
		if (variants.size() > 1) {
			applyTexture(variants.get(rnd.nextInt(variants.size())));
		}
	}

	public boolean hasVariants() {
		return variants.size() > 1;
	}

	// --- Texture ---

	public Image getTexture() {
		return texture != null ? texture : DEFAULT_IMAGE;
	}

	public void setTexture(Image texture) {
		applyTexture(texture);
	}

	public static Image getTextureFromPath(String path) {
		return imageCache.computeIfAbsent(path, p -> {
			var resource = Sprite.class.getResource(p);
			if (resource != null) {
				return new Image(resource.toExternalForm());
			}
			return DEFAULT_IMAGE;
		});
	}

	// --- Size / Offset / Scale ---

	public Vector2 getSpriteSize() {
		return new Vector2(spriteSize.x() * scale, spriteSize.y() * scale);
	}

	public double getSpriteYOffset() {
		return spriteYOffset * scale;
	}

	public void setSpriteYOffset(double spriteYOffset) {
		this.spriteYOffset = spriteYOffset;
	}

	public void setSpriteAlignment(SpriteAlignment alignment) {
		int grid = GameConfig.GRID_CELL_SIZE;
		double spriteH = this.texture.getHeight();

		double calculatedOffset = switch (alignment) {
			case TOP -> 0;
			case CENTER -> (spriteH - grid) / 2.0;
			case BOTTOM -> spriteH - grid;
		};

		this.setSpriteYOffset(calculatedOffset);
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public Vector2 getOffset() {
		return offset;
	}

	public void setOffset(Vector2 offset) {
		this.offset = offset;
	}

	// --- Rendering Layer ---

	public int getRenderingLayer() {
		return renderingLayer;
	}

	public void setRenderingLayer(int layer) {
		this.renderingLayer = layer;
	}

	// --- Flip ---

	public boolean getFlipX() {
		return flipX;
	}

	public void setFlipX(boolean flipX) {
		this.flipX = flipX;
	}

	// --- Animation ---

	public Animation getCurrentAnimation() {
		return currentAnimation;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public boolean isLooping() {
		return looping;
	}

	public void setLooping(boolean looping) {
		this.looping = looping;
	}

	public boolean isSynced() {
		return synced;
	}

	public void setSynced(boolean synced) {
		this.synced = synced;
	}

	public void playAnimation() {
		this.localElapsed = 0.0;
		this.isPaused = false;
	}

	public void pauseAnimation() {
		this.isPaused = true;
	}

	public void setAnimation(Animation animation) {
		this.currentAnimation = animation;
		this.localElapsed = 0.0;
		this.isPaused = false;
	}

	public void updateAnimationFrame(double elapsedTime, double deltaTime) {
		double elapsed;
		if (synced) {
			elapsed = elapsedTime;
		} else {
			localElapsed += deltaTime;
			elapsed = localElapsed;
		}
		double playTime = currentAnimation.getPlayTime();
		List<Image> frames = currentAnimation.getFrames();
		int frameCount = frames.size();

		if (!looping && elapsed >= playTime) {
			applyTexture(frames.getLast());
			isPaused = true;
			return;
		}

		double progress = (elapsed % playTime) / playTime;
		int frameIndex = Math.clamp((int) Math.floor(progress * frameCount), 0,
				frameCount - 1);
		applyTexture(frames.get(frameIndex));
	}

	// --- Tint ---

	public void setTint(Color tint) {
		this.tint = tint;
	}

	public void clearTint() {
		this.tint = null;
	}

	public boolean hasTint() {
		return tint != null;
	}

	public Color getTint() {
		return tint;
	}

	public void randomizeTint(Random rnd, Color... colors) {
		setTint(colors[rnd.nextInt(colors.length)]);
	}

	public void randomizeColorOffset(Random rnd, double colorRange) {
		Color base = tint != null ? tint : Color.WHITE;
		double r = Math.clamp(
				base.getRed() + (rnd.nextDouble() * 2 - 1) * colorRange, 0, 1);
		double g = Math.clamp(
				base.getGreen() + (rnd.nextDouble() * 2 - 1) * colorRange, 0,
				1);
		double b = Math.clamp(
				base.getBlue() + (rnd.nextDouble() * 2 - 1) * colorRange, 0, 1);
		setTint(new Color(r, g, b, 1.0));
	}

	public static Image applyTint(Image source, Color tint) {
		String key = source.getUrl() + "_" + tint.toString();
		return tintedImageCache.computeIfAbsent(key, k -> {
			int w = (int) source.getWidth();
			int h = (int) source.getHeight();
			WritableImage result = new WritableImage(w, h);
			PixelReader reader = source.getPixelReader();
			PixelWriter writer = result.getPixelWriter();
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					Color p = reader.getColor(x, y);
					double a = tint.getOpacity();
					writer.setColor(x, y, new Color(
							p.getRed() * (1 - a) + p.getRed() * tint.getRed() * a,
							p.getGreen() * (1 - a) + p.getGreen() * tint.getGreen() * a,
							p.getBlue() * (1 - a) + p.getBlue() * tint.getBlue() * a,
							p.getOpacity()));
				}
			}
			return result;
		});
	}

	public void setOpacity(double opacity) {
		this.opacity = Math.clamp(opacity, 0.0, 1.0);
	}

	public double getOpacity() {
		return opacity;
	}
}