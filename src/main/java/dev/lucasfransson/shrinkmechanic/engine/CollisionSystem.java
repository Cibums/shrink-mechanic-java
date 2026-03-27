package dev.lucasfransson.shrinkmechanic.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollisionSystem implements IGameSystem {

	private final Map<Long, List<GameObject>> cells = new HashMap<>();
	private final Map<GameObject, Long> objectCells = new HashMap<>();
	private final List<GameObject> dynamicObjects = new ArrayList<>();
	private final List<GameObject> queryBuffer = new ArrayList<>();

	public void register(GameObject obj) {
		if (!obj.hasCollision())
			return;

		if (obj instanceof ICollisionAware c) {
			c.setCollisionSystem(this);
		}

		long key = cellKey(obj.getPosition());
		cells.computeIfAbsent(key, k -> new ArrayList<>()).add(obj);
		objectCells.put(obj, key);

		if (obj instanceof ICollisionAware) {
			dynamicObjects.add(obj);
		}
	}

	public void unregister(GameObject obj) {
		Long key = objectCells.remove(obj);
		if (key != null) {
			List<GameObject> cell = cells.get(key);
			if (cell != null) {
				cell.remove(obj);
				if (cell.isEmpty())
					cells.remove(key);
			}
		}
		dynamicObjects.remove(obj);
	}

	@Override
	public void tryRegister(Object object) {
		if (object instanceof GameObject g)
			register(g);
	}

	@Override
	public void tryUnregister(Object object) {
		if (object instanceof GameObject g)
			unregister(g);
	}

	public void updateDynamicPositions() {
		for (GameObject obj : dynamicObjects) {
			Long oldKey = objectCells.get(obj);
			long newKey = cellKey(obj.getPosition());

			if (oldKey != null && oldKey != newKey) {
				List<GameObject> oldCell = cells.get(oldKey);
				if (oldCell != null) {
					oldCell.remove(obj);
					if (oldCell.isEmpty())
						cells.remove(oldKey);
				}
				cells.computeIfAbsent(newKey, k -> new ArrayList<>()).add(obj);
				objectCells.put(obj, newKey);
			}
		}
	}

	public List<GameObject> getNearbyCollidables(Vector2 center, double range) {
		queryBuffer.clear();

		int minX = (int) Math.floor(center.x() - range);
		int maxX = (int) Math.floor(center.x() + range);
		int minY = (int) Math.floor(center.y() - range);
		int maxY = (int) Math.floor(center.y() + range);

		for (int cx = minX; cx <= maxX; cx++) {
			for (int cy = minY; cy <= maxY; cy++) {
				List<GameObject> cell = cells.get(cellKey(cx, cy));
				if (cell != null) {
					queryBuffer.addAll(cell);
				}
			}
		}

		return queryBuffer;
	}

	private long cellKey(Vector2 pos) {
		return cellKey((int) Math.floor(pos.x()), (int) Math.floor(pos.y()));
	}

	private long cellKey(int x, int y) {
		return ((long) x << 32) | (y & 0xFFFFFFFFL);
	}
}