package dev.lucasfransson.shrinkmechanic.engine;

public final class GameConfig {
	private GameConfig() {
	}
	public static final int GRID_CELL_SIZE = 32;
	public static final int CHUNK_SIZE = 16;
	public static final int CHUNK_LOAD_RADIUS = 1;

	/**
	 * Assumed fixed tick rate for deterministic timing calculations.
	 * In multiplayer, the server tick rate will be the authoritative clock.
	 */
	public static final int TICK_RATE = 60;
}
