package dev.lucasfransson.shrinkmechanic.engine;

public class Vector2Int {

	private int x;
	private int y;

	public Vector2Int(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public int getX() {
		return x;
	}

	private void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	private void setY(int y) {
		this.y = y;
	}

	public static Vector2Int zero() {
		return new Vector2Int(0, 0);
	}

	public Vector2Int subtract(Vector2Int position) {
		return new Vector2Int(this.getX() - position.getX(),
				this.getY() - position.getY());
	}

	public Vector2 convertToVector2() {
		return new Vector2(x, y);
	}
}
