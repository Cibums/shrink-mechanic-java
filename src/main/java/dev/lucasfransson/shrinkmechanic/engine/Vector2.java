package dev.lucasfransson.shrinkmechanic.engine;

public record Vector2(double x, double y) {

	public static Vector2 zero() {
		return new Vector2(0, 0);
	}

	public Vector2 add(Vector2 other) {
		return new Vector2(x + other.x, y + other.y);
	}

	public Vector2 multiply(Vector2 other) {
		return new Vector2(x * other.x, y * other.y);
	}

	public Vector2 divide(Vector2 other) {
		return new Vector2(x / other.x, y / other.y);
	}

	public Vector2 subtract(Vector2 other) {
		return new Vector2(x - other.x, y - other.y);
	}
}