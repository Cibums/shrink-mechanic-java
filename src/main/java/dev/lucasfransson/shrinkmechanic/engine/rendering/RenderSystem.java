package dev.lucasfransson.shrinkmechanic.engine.rendering;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;

public class RenderSystem {

	private final List<IRenderable> renderables = new ArrayList<>();

	public void register(IRenderable r) {
		renderables.add(r);
	}

	public void unregister(IRenderable r) {
		renderables.remove(r);
	}

	public List<SpriteEntry> getSpriteEntriesInRange(Vector2 center,
			double rangeX, double rangeY) {
		List<SpriteEntry> entries = new ArrayList<>();

		for (IRenderable r : renderables) {
			Vector2 pos = r.getPosition();
			if (Math.abs(pos.getX() - center.getX()) > rangeX
					|| Math.abs(pos.getY() - center.getY()) > rangeY) {
				continue;
			}
			for (Sprite s : r.getSprites()) {
				entries.add(new SpriteEntry(s, pos));
			}
		}

		entries.sort(
				Comparator.comparingDouble(SpriteEntry::getRenderingZOffset));
		return entries;
	}

	private double elapsedTime = 0.0;

	public void updateAnimations(double deltaTime, List<SpriteEntry> entries) {
		elapsedTime += deltaTime;
		for (SpriteEntry entry : entries) {
			Sprite s = entry.getSprite();
			if (!s.isPaused() && s.getCurrentAnimation() != null) {
				s.updateAnimationFrame(elapsedTime, deltaTime);
			}
		}
	}
}