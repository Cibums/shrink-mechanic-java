package dev.lucasfransson.shrinkmechanic.engine.rendering;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dev.lucasfransson.shrinkmechanic.engine.IGameSystem;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;

public class RenderSystem implements IGameSystem {

	private final Set<IRenderable> renderables = new LinkedHashSet<>();
	private final List<SpriteEntry> spriteEntryBuffer = new ArrayList<>();
	private double elapsedTime = 0.0;

	public void register(IRenderable r) {
		renderables.add(r);
	}

	public void unregister(IRenderable r) {
		renderables.remove(r);
	}

	@Override
	public void tryRegister(Object object) {
		if (object instanceof IRenderable r)
			register(r);
	}

	@Override
	public void tryUnregister(Object object) {
		if (object instanceof IRenderable r)
			unregister(r);
	}

	public void getSpriteEntriesInRange(Vector2 center, double rangeX,
			double rangeY, List<SpriteEntry> out) {
		spriteEntryBuffer.clear();

		for (IRenderable r : renderables) {
			Vector2 pos = r.getPosition();
			if (Math.abs(pos.x() - center.x()) > rangeX
					|| Math.abs(pos.y() - center.y()) > rangeY) {
				continue;
			}
			for (Sprite s : r.getSprites()) {
				spriteEntryBuffer.add(new SpriteEntry(s, pos));
			}
		}

		spriteEntryBuffer.sort(
				Comparator.comparingDouble(SpriteEntry::getRenderingZOffset));

		out.clear();
		out.addAll(spriteEntryBuffer);
	}

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