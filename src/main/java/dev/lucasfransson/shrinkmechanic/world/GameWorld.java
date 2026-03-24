package dev.lucasfransson.shrinkmechanic.world;
import java.util.Random;

import dev.lucasfransson.shrinkmechanic.engine.ObjectRegistry;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.world.generation.PerlinNoise;
import dev.lucasfransson.shrinkmechanic.world.objects.Rock;
import dev.lucasfransson.shrinkmechanic.world.objects.Tree;
import dev.lucasfransson.shrinkmechanic.world.objects.WorldObject;
import dev.lucasfransson.shrinkmechanic.world.tiles.GrassTile;
import dev.lucasfransson.shrinkmechanic.world.tiles.Tile;
import dev.lucasfransson.shrinkmechanic.world.tiles.WaterTile;

public class GameWorld {

	public static final int gridElementSize = 32;

	private ObjectRegistry registry;

	private Tile[][] ground;
	private WorldObject[][] worldObjects;
	private int worldSize;

	public GameWorld(int worldSize, ObjectRegistry registry) {

		this.registry = registry;
		this.worldSize = worldSize;
		ground = new Tile[worldSize][worldSize];
		worldObjects = new WorldObject[worldSize][worldSize];

		generateWorld();
	}

	public void generateWorld() {

		PerlinNoise groundPerlinNoise = new PerlinNoise();
		PerlinNoise forestPerlinNoise = new PerlinNoise();

		double scale = 0.1;
		double threshold = 0.0;

		for (int x = 0; x < worldSize; x++) {
			for (int y = 0; y < worldSize; y++) {

				double noiseValue = groundPerlinNoise.perlin(x * scale,
						y * scale);

				if (noiseValue > threshold) {
					addTileToWorld(new GrassTile(), new Vector2Int(x, y));

					double forestNoiseValue = forestPerlinNoise
							.perlin(x * scale, y * scale);

					Random rnd = new Random();

					if (forestNoiseValue > threshold
							&& rnd.nextDouble() <= 0.7f) {
						addWorldObjectToWorld(new Tree(), new Vector2Int(x, y));
					}

					if (rnd.nextDouble() < 0.1f) {
						addWorldObjectToWorld(new Rock(), new Vector2Int(x, y),
								ReplacementMode.KEEP);
					}

				} else {
					addTileToWorld(new WaterTile(), new Vector2Int(x, y));
				}

			}
		}
	}

	public void addWorldObjectToWorld(WorldObject object, Vector2Int position) {
		addWorldObjectToWorld(object, position, ReplacementMode.REPLACE);
	}

	public void addWorldObjectToWorld(WorldObject object, Vector2Int position,
			ReplacementMode replacementMode) {
		object.setPosition(position);

		int x = position.getIntX();
		int y = position.getIntY();

		switch (replacementMode) {
			case ReplacementMode.KEEP :

				if (worldObjects[x][y] != null) {
					return;
				}

				break;
			case ReplacementMode.DESTROY :
				destroyWorldObject(position);
				break;
			default :
				break;
		}

		worldObjects[(int) position.getX()][(int) position.getY()] = object;
		registry.instantiate(object);
	}

	private void destroyWorldObject(Vector2Int position) {
		registry.destroy(worldObjects[position.getIntX()][position.getIntY()]);
		worldObjects[position.getIntX()][position.getIntY()].onDestroy();
		worldObjects[position.getIntX()][position.getIntY()] = null;
	}

	public void addTileToWorld(Tile tile, Vector2Int position) {
		addTileToWorld(tile, position, ReplacementMode.REPLACE);
	}

	public void addTileToWorld(Tile tile, Vector2Int position,
			ReplacementMode replacementMode) {

		tile.setPosition(position);

		int x = position.getIntX();
		int y = position.getIntY();

		switch (replacementMode) {
			case ReplacementMode.KEEP :

				if (ground[x][y] != null) {
					return;
				}

				break;
			case ReplacementMode.DESTROY :
				destroyTile(position);
				break;
			default :
				break;
		}

		ground[(int) position.getX()][(int) position.getY()] = tile;
		registry.instantiate(tile);
	}

	public void destroyTile(Vector2Int position) {
		registry.destroy(ground[position.getIntX()][position.getIntY()]);
		ground[position.getIntX()][position.getIntY()].onDestroy();
		ground[position.getIntX()][position.getIntY()] = null;
	}

	public Tile[][] getGround() {
		return ground;
	}

	public int getWorldSize() {
		return worldSize;
	}
}
