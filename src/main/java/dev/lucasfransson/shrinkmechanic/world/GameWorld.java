package dev.lucasfransson.shrinkmechanic.world;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.ObjectRegistry;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.world.generation.PerlinNoise;
import dev.lucasfransson.shrinkmechanic.world.objects.WorldObject;
import dev.lucasfransson.shrinkmechanic.world.tiles.Tile;

public class GameWorld {

	private final ObjectRegistry registry;
	private final Map<ChunkCoord, Chunk> chunks = new HashMap<>();
	private final Set<ChunkCoord> activeChunks = new HashSet<>();
	private final PerlinNoise groundNoise;
	private final PerlinNoise forestNoise;
	private final long worldSeed;
	private ChunkCoord lastPlayerChunk = null;
	private final List<WorldEventListener> eventListeners = new ArrayList<>();

	public GameWorld(ObjectRegistry registry) {
		this.registry = registry;
		this.worldSeed = new Random().nextLong();
		this.groundNoise = new PerlinNoise(worldSeed);
		this.forestNoise = new PerlinNoise(worldSeed + 1);
	}

	public GameWorld(ObjectRegistry registry, long seed) {
		this.registry = registry;
		this.worldSeed = seed;
		this.groundNoise = new PerlinNoise(worldSeed);
		this.forestNoise = new PerlinNoise(worldSeed + 1);
	}

	public void updateChunks(Vector2 playerPos) {
		ChunkCoord current = ChunkCoord.fromWorldPos(playerPos.x(),
				playerPos.y());

		if (current.equals(lastPlayerChunk)) {
			return;
		}
		lastPlayerChunk = current;

		int radius = GameConfig.CHUNK_LOAD_RADIUS;

		Set<ChunkCoord> shouldBeLoaded = new HashSet<>();
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				shouldBeLoaded.add(
						new ChunkCoord(current.x() + dx, current.y() + dy));
			}
		}

		List<ChunkCoord> toUnload = new ArrayList<>();
		for (ChunkCoord coord : activeChunks) {
			if (!shouldBeLoaded.contains(coord)) {
				toUnload.add(coord);
			}
		}
		for (ChunkCoord coord : toUnload) {
			chunks.get(coord).unload(registry);
			activeChunks.remove(coord);
		}

		for (ChunkCoord coord : shouldBeLoaded) {
			if (!activeChunks.contains(coord)) {
				if (!chunks.containsKey(coord)) {
					chunks.put(coord, new Chunk(coord, groundNoise, forestNoise,
							worldSeed));
				}
				chunks.get(coord).load(registry);
				activeChunks.add(coord);
			}
		}
	}

	public void destroyWorldObject(Vector2Int position) {
		ChunkCoord coord = toChunkCoord(position);
		Chunk chunk = chunks.get(coord);
		if (chunk != null) {
			chunk.destroyObject(localCoord(position.x()),
					localCoord(position.y()), registry);
		}

		notifyListeners(l -> l.onWorldObjectDestroyed(position));
	}

	public void destroyTile(Vector2Int position) {
		ChunkCoord coord = toChunkCoord(position);
		Chunk chunk = chunks.get(coord);
		if (chunk != null) {
			chunk.destroyTile(localCoord(position.x()),
					localCoord(position.y()), registry);
		}

		notifyListeners(l -> l.onTileDestroyed(position));
	}

	public void placeWorldObject(Vector2Int position, WorldObject object,
			ReplacementMode mode) {
		ChunkCoord coord = toChunkCoord(position);
		Chunk chunk = chunks.get(coord);

		if (chunk != null) {
			chunk.placeWorldObject(localCoord(position.x()),
					localCoord(position.y()), object,
					activeChunks.contains(coord) ? registry : null, mode);
		}

		notifyListeners(l -> l.onWorldObjectPlaced(position, object));
	}

	public void placeTile(Vector2Int position, Tile tile,
			ReplacementMode mode) {
		ChunkCoord coord = toChunkCoord(position);
		Chunk chunk = chunks.get(coord);
		if (chunk != null) {
			chunk.placeTile(localCoord(position.x()), localCoord(position.y()),
					tile, activeChunks.contains(coord) ? registry : null, mode);
		}

		notifyListeners(l -> l.onTilePlaced(position, tile));
	}

	private ChunkCoord toChunkCoord(Vector2Int pos) {
		return new ChunkCoord(Math.floorDiv(pos.x(), GameConfig.CHUNK_SIZE),
				Math.floorDiv(pos.y(), GameConfig.CHUNK_SIZE));
	}

	private int localCoord(int worldCoord) {
		return Math.floorMod(worldCoord, GameConfig.CHUNK_SIZE);
	}

	public void addEventListener(WorldEventListener listener) {
		eventListeners.add(listener);
	}

	public void removeEventListener(WorldEventListener listener) {
		eventListeners.remove(listener);
	}

	private void notifyListeners(Consumer<WorldEventListener> action) {
		for (WorldEventListener listener : eventListeners) {
			action.accept(listener);
		}
	}

}
