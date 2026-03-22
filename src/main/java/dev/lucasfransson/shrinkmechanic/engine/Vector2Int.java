package dev.lucasfransson.shrinkmechanic.engine;

public class Vector2Int extends Vector2 {

	public Vector2Int(int x, int y) {
		super(x, y);
	}

	@Override
	public double getX() {
		return Math.floor(super.getX());
	}

	public void setX(int x) {
		super.setX(x);
	}

	@Override
	public double getY() {
		return Math.floor(super.getY());
	}

	public int getIntX() {
		return (int) this.getX();
	}

	public int getIntY() {
		return (int) this.getY();
	}

	public void setY(int y) {
		super.setY(y);
	}

	public static Vector2Int zero() {
		return new Vector2Int(0, 0);
	}
}
