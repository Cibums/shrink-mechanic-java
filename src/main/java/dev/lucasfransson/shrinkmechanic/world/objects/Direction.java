package dev.lucasfransson.shrinkmechanic.world.objects;

import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;

public enum Direction {
	UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0), ALL(0, 0);

	private final Vector2Int offset;

	Direction(int x, int y) {
		this.offset = new Vector2Int(x, y);
	}

	public List<Direction> expand() {
		if (this == ALL) {
			return List.of(UP, DOWN, LEFT, RIGHT);
		}
		return List.of(this);
	}

	public Vector2Int offset() {
		return offset;
	}

	public Direction opposite() {
		return switch (this) {
			case UP -> DOWN;
			case DOWN -> UP;
			case LEFT -> RIGHT;
			case RIGHT -> LEFT;
			case ALL -> ALL;
		};
	}
}
