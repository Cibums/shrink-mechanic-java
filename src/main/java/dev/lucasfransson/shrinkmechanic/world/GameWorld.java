package dev.lucasfransson.shrinkmechanic.world;
import dev.lucasfransson.shrinkmechanic.engine.ObjectRegistry;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.world.generation.PerlinNoise;
import dev.lucasfransson.shrinkmechanic.world.tiles.GrassTile;
import dev.lucasfransson.shrinkmechanic.world.tiles.Tile;
import dev.lucasfransson.shrinkmechanic.world.tiles.WaterTile;

public class GameWorld {

	private ObjectRegistry registry;

	private Tile[][] ground;
	private int worldSize;

	public GameWorld(int worldSize, ObjectRegistry registry) {

		this.registry = registry;
		this.worldSize = worldSize;
		ground = new Tile[worldSize][worldSize];

		generateWorld();
	}

	public void generateWorld() {

		PerlinNoise perlinNoise = new PerlinNoise();

		double scale = 0.1;
		double threshold = 0.0;

		for (int x = 0; x < worldSize; x++) {
			for (int y = 0; y < worldSize; y++) {

				double noiseValue = perlinNoise.perlin(x * scale, y * scale);

				if (noiseValue > threshold) {
					addTileToWorld(new GrassTile(), new Vector2Int(x, y));
				} else {
					addTileToWorld(new WaterTile(), new Vector2Int(x, y));
				}

			}
		}
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

				if (ground[x][y] == null) {
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
