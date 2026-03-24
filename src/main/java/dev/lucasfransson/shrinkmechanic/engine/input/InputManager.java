package dev.lucasfransson.shrinkmechanic.engine.input;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputManager {

	private final Set<KeyCode> heldKeys = new HashSet<>();
	private double scrollDelta = 0;

	public void listen(Scene scene) {
		scene.setOnKeyPressed(event -> heldKeys.add(event.getCode()));
		scene.setOnKeyReleased(event -> heldKeys.remove(event.getCode()));

		scene.setOnScroll(event -> {
			scrollDelta += event.getDeltaY();
		});
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
}
