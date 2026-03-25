package dev.lucasfransson.shrinkmechanic.engine;

import java.util.ArrayList;
import java.util.List;

public class CollisionSystem {

	private final List<GameObject> collidables = new ArrayList<>();

	public void register(GameObject obj) {
		if (obj.hasCollision()) {

			if (obj instanceof ICollisionAware c) {
				c.setCollisionSystem(this);
			}

			collidables.add(obj);
		}
	}

	public void unregister(GameObject obj) {
		collidables.remove(obj);
	}

	public List<GameObject> getNearbyCollidables(Vector2 center, double range) {
		return collidables.stream().filter(o -> {
			Vector2 p = o.getPosition();
			return Math.abs(p.getX() - center.getX()) <= range
					&& Math.abs(p.getY() - center.getY()) <= range;
		}).toList();
	}
}