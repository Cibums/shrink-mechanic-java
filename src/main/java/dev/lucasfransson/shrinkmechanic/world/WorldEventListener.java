package dev.lucasfransson.shrinkmechanic.world;

import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.world.objects.WorldObject;
import dev.lucasfransson.shrinkmechanic.world.tiles.Tile;

public interface WorldEventListener {
	void onWorldObjectDestroyed(Vector2Int position);
	void onTileDestroyed(Vector2Int position);
	void onWorldObjectPlaced(Vector2Int position, WorldObject object);
	void onTilePlaced(Vector2Int position, Tile tile);
}
