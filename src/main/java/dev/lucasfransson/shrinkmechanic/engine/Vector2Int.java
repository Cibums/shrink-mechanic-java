package dev.lucasfransson.shrinkmechanic.engine;

public record Vector2Int(int x, int y) {

	public static Vector2Int zero() {
		return new Vector2Int(0, 0);
	}

	public Vector2Int subtract(Vector2Int other) {
		return new Vector2Int(x - other.x, y - other.y);
	}

	public Vector2 convertToVector2() {
		return new Vector2(x, y);
	}

	public Vector2Int add(Vector2Int other) {
		return new Vector2Int(x + other.x(), y + other.y());
	}
}