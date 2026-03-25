package dev.lucasfransson.shrinkmechanic.world;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;

public record ChunkCoord(int x, int y) {

	public static ChunkCoord fromWorldPos(double worldX, double worldY) {
		return new ChunkCoord(
				Math.floorDiv((int) worldX, GameConfig.CHUNK_SIZE),
				Math.floorDiv((int) worldY, GameConfig.CHUNK_SIZE));
	}
}
