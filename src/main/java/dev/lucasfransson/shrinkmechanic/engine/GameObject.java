package dev.lucasfransson.shrinkmechanic.engine;

public class GameObject {

	private Vector2 position;
	private Vector2 size;
	private boolean hasCollision = false;

	public GameObject() {
		this(Vector2.zero());
	}

	public GameObject(Vector2 position) {
		this(position, new Vector2(100, 100));
	}

	public GameObject(Vector2 position, Vector2 size) {
		this.setSize(size);
		this.setPosition(position);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public void moveHorizontally(double speed) {
		this.move(speed, 0);
	}

	public void moveVertically(double speed) {
		this.move(0, speed);
	}

	public void move(double x, double y) {
		this.move(new Vector2(x, y));
	}

	public void move(Vector2 vector) {
		this.position = new Vector2(this.position.getX() + vector.getX(),
				this.position.getY() + vector.getY());
	}

	public boolean hasCollision() {
		return hasCollision;
	}

	public void setHasCollision(boolean state) {
		this.hasCollision = state;
	}

	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}
}
