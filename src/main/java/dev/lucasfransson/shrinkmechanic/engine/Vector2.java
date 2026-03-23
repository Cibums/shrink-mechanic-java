package dev.lucasfransson.shrinkmechanic.engine;

public class Vector2 {
	private double x;
	private double y;

	public Vector2(double x, double y) {
		this.setX(x);
		this.setY(y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public static Vector2 zero() {
		return new Vector2(0, 0);
	}

	public Vector2 subtract(Vector2 position) {
		return new Vector2(this.getX() - position.getX(),
				this.getY() - position.getY());
	}
}
