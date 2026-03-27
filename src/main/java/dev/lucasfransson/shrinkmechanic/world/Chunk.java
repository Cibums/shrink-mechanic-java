package dev.lucasfransson.shrinkmechanic.world;

import java.util.List;
import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.ObjectRegistry;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.items.ItemDrop;
import dev.lucasfransson.shrinkmechanic.world.generation.PerlinNoise;
import dev.lucasfransson.shrinkmechanic.world.objects.Flowers;
import dev.lucasfransson.shrinkmechanic.world.objects.IRandomizable;
import dev.lucasfransson.shrinkmechanic.world.objects.Rock;
import dev.lucasfransson.shrinkmechanic.world.objects.Tree;
import dev.lucasfransson.shrinkmechanic.world.objects.WorldObject;
import dev.lucasfransson.shrinkmechanic.world.tiles.GrassTile;
import dev.lucasfransson.shrinkmechanic.world.tiles.Tile;
import dev.lucasfransson.shrinkmechanic.world.tiles.WaterTile;

public class Chunk {

	private static final double SCALE = 0.1;
	private static final double THRESHOLD = -0.3;

	private final ChunkCoord coord;
	private final Tile[][] tiles;
	private final WorldObject[][] objects;

	public Chunk(ChunkCoord coord, PerlinNoise groundNoise,
			PerlinNoise forestNoise, long worldSeed) {
		this.coord = coord;
		int size = GameConfig.CHUNK_SIZE;
		tiles = new Tile[size][size];
		objects = new WorldObject[size][size];

		long chunkSeed = worldSeed
				^ (coord.x() * 341873128712L + coord.y() * 132897987541L);
		Random rnd = new Random(chunkSeed);

		for (int lx = 0; lx < size; lx++) {
			for (int ly = 0; ly < size; ly++) {
				int wx = coord.x() * size + lx;
				int wy = coord.y() * size + ly;

				double noiseValue = groundNoise.perlin(wx * SCALE, wy * SCALE);

				if (noiseValue > THRESHOLD) {
					tiles[lx][ly] = new GrassTile();

					double forestNoiseValue = forestNoise.perlin(wx * SCALE,
							wy * SCALE);

					if (forestNoiseValue > THRESHOLD
							&& rnd.nextDouble() <= 0.7) {
						objects[lx][ly] = new Tree();
					} else if (rnd.nextDouble() < 0.1) {
						objects[lx][ly] = new Rock();
					} else if (rnd.nextDouble() < 0.3) {
						objects[lx][ly] = new Flowers();
					}

					if (objects[lx][ly] instanceof IRandomizable r) {
						r.randomize(rnd);
					}

					if (tiles[lx][ly] instanceof IRandomizable r) {
						r.randomize(rnd);
					}

				} else {
					tiles[lx][ly] = new WaterTile();
				}
			}
		}
	}

	public void load(ObjectRegistry registry) {
		int size = GameConfig.CHUNK_SIZE;
		for (int lx = 0; lx < size; lx++) {
			for (int ly = 0; ly < size; ly++) {
				int wx = coord.x() * size + lx;
				int wy = coord.y() * size + ly;
				Vector2Int worldPos = new Vector2Int(wx, wy);

				if (tiles[lx][ly] != null) {
					tiles[lx][ly].setPosition(worldPos);
					registry.instantiate(tiles[lx][ly]);
				}
				if (objects[lx][ly] != null) {
					objects[lx][ly].setPosition(worldPos);
					registry.instantiate(objects[lx][ly]);
				}
			}
		}
	}

	public void unload(ObjectRegistry registry) {
		int size = GameConfig.CHUNK_SIZE;
		for (int lx = 0; lx < size; lx++) {
			for (int ly = 0; ly < size; ly++) {
				if (tiles[lx][ly] != null) {
					registry.destroy(tiles[lx][ly]);
				}
				if (objects[lx][ly] != null) {
					registry.destroy(objects[lx][ly]);
				}
			}
		}
	}

	public void placeWorldObject(int localX, int localY, WorldObject obj,
			ObjectRegistry registry, ReplacementMode mode) {

		if (tiles[localX][localY] == null
				|| !tiles[localX][localY].canBePlacedOn()) {
			return;
		}

		WorldObject existing = objects[localX][localY];
		if (existing != null) {
			switch (mode) {
				case KEEP -> {
					return;
				}
				case DESTROY -> {
					existing.onDestroy();
					if (registry != null)
						registry.destroy(existing);
				}
				case REPLACE -> {
					if (registry != null)
						registry.destroy(existing);
				}
			}
		}
		objects[localX][localY] = obj;
		if (obj != null && registry != null) {
			int wx = coord.x() * GameConfig.CHUNK_SIZE + localX;
			int wy = coord.y() * GameConfig.CHUNK_SIZE + localY;
			obj.setPosition(new Vector2Int(wx, wy));
			registry.instantiate(obj);
		}
	}

	public void placeTile(int localX, int localY, Tile tile,
			ObjectRegistry registry, ReplacementMode mode) {
		Tile existing = tiles[localX][localY];
		if (existing != null) {
			switch (mode) {
				case KEEP -> {
					return;
				}
				case DESTROY -> {
					existing.onDestroy();
					if (registry != null)
						registry.destroy(existing);
				}
				case REPLACE -> {
					if (registry != null)
						registry.destroy(existing);
				}
			}
		}
		tiles[localX][localY] = tile;
		if (tile != null && registry != null) {
			int wx = coord.x() * GameConfig.CHUNK_SIZE + localX;
			int wy = coord.y() * GameConfig.CHUNK_SIZE + localY;
			tile.setPosition(new Vector2Int(wx, wy));
			registry.instantiate(tile);
		}
	}

	public WorldObject getObject(int localX, int localY) {
		return objects[localX][localY];
	}

	public List<ItemDrop> destroyObject(int localX, int localY) {
		if (objects[localX][localY] == null)
			return List.of();
		List<ItemDrop> drops = objects[localX][localY].getDrops();
		objects[localX][localY].onDestroy();
		objects[localX][localY].destroy();
		objects[localX][localY] = null;
		return drops;
	}

	public void destroyTile(int localX, int localY) {
		if (tiles[localX][localY] != null) {
			tiles[localX][localY].onDestroy();
			tiles[localX][localY].destroy();
			tiles[localX][localY] = null;
		}
	}
}
