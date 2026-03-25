package dev.lucasfransson.shrinkmechanic.engine;

import dev.lucasfransson.shrinkmechanic.world.GameWorld;

public class GameObject {

	private Vector2 position;
	private Vector2 size;
	private boolean hasCollision = false;

	private GameWorld world;

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

	public GameWorld getWorld() {
		return world;
	}

	public void setWorld(GameWorld world) {
		this.world = world;
	}
}
