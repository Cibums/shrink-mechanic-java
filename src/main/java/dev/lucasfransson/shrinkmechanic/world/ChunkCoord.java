package dev.lucasfransson.shrinkmechanic.world;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;

public record ChunkCoord(int x, int y) {

	public static ChunkCoord fromWorldPos(double worldX, double worldY) {
		return new ChunkCoord(
				Math.floorDiv((int) Math.floor(worldX), GameConfig.CHUNK_SIZE),
				Math.floorDiv((int) Math.floor(worldY), GameConfig.CHUNK_SIZE));
	}
}
