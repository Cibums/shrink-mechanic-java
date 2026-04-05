package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.List;
import java.util.Map;

import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;

/**
 * Looks up world objects adjacent to a grid position in the given directions.
 * Returns paired (Direction, WorldObject) entries — only non-null objects are
 * included.
 */
@FunctionalInterface
public interface AdjacentObjectLookup {
	List<Map.Entry<Direction, WorldObject>> apply(Vector2Int position,
			Direction... directions);
}
