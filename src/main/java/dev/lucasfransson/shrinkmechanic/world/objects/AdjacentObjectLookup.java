package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.List;
import java.util.Map;

import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;

@FunctionalInterface
public interface AdjacentObjectLookup {
	List<Map.Entry<Direction, WorldObject>> apply(Vector2Int position,
			Direction... directions);
}
