package dev.lucasfransson.shrinkmechanic.engine.rendering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.image.Image;

public class Animation {

	private double playTime;
	private List<Image> frames = new ArrayList<>();

	public Animation(String directoryPath, double playTime) {

		int index = 0;

		while (true) {
			String path = directoryPath + "/" + index + ".png";

			var resource = IRenderable.class.getResource(path);

			if (resource == null) {
				break;
			}

			frames.add(new Image(resource.toExternalForm()));
			index++;
		}

		if (frames.isEmpty()) {
			throw new IllegalArgumentException(
					"No animation frames found in: " + directoryPath);
		}

		this.playTime = playTime;
	}

	public double getPlayTime() {
		return playTime;
	}

	public List<Image> getFrames() {
		return Collections.unmodifiableList(frames);
	}
}