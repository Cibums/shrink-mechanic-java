package dev.lucasfransson.shrinkmechanic.engine.input;
import java.util.HashSet;
import java.util.Set;

import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class InputManager {

	private final Set<KeyCode> heldKeys = new HashSet<>();
	private double scrollDelta = 0;

	private Vector2 leftClick = null;
	private Vector2 rightClick = null;

	private Vector2 mousePosition;

	public void listen(Scene scene) {
		scene.setOnKeyPressed(event -> heldKeys.add(event.getCode()));
		scene.setOnKeyReleased(event -> heldKeys.remove(event.getCode()));

		scene.setOnMouseMoved(event -> {
			mousePosition = new Vector2(event.getX(), event.getY());
		});

		scene.setOnScroll(event -> {
			scrollDelta += event.getDeltaY();
		});

	}

	public void listenMouseClicks(Node node) {
		node.setOnMousePressed(event -> {
			Vector2 pos = new Vector2(event.getX(), event.getY());
			if (event.getButton() == MouseButton.PRIMARY)
				leftClick = pos;
			if (event.getButton() == MouseButton.SECONDARY)
				rightClick = pos;
		});
	}

	public Vector2 consumeLeftClick() {
		Vector2 v = leftClick;
		leftClick = null;
		return v;
	}

	public Vector2 consumeRightClick() {
		Vector2 v = rightClick;
		rightClick = null;
		return v;
	}

	public boolean isKeyHeld(KeyCode key) {
		return heldKeys.contains(key);
	}

	public boolean isKeyHeld(KeyCode... keys) {
		for (KeyCode key : keys) {
			if (heldKeys.contains(key))
				return true;
		}
		return false;
	}

	public double consumeScrollDelta() {
		double delta = scrollDelta;
		scrollDelta = 0;
		return delta;
	}

	public Vector2 getMousePosition() {
		return mousePosition;
	}
}
