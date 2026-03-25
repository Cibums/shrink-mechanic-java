package dev.lucasfransson.shrinkmechanic.engine.rendering;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Animation {

	private double playTime;
	private ArrayList<Image> clips = new ArrayList<>();

	public Animation(String directoryPath, double playTime) {

		int index = 0;

		while (true) {
			String path = directoryPath + "/" + index + ".png";

			var resource = Renderable.class.getResource(path);

			if (resource == null) {
				break;
			}

			clips.add(new Image(resource.toExternalForm()));
			index++;
		}

		if (clips.isEmpty()) {
			throw new IllegalArgumentException(
					"No animation frames found in: " + directoryPath);
		}

		this.playTime = playTime;
	}

	public double getPlayTime() {
		return playTime;
	}

	public List<Image> getClips() {
		return clips;
	}

	public double getTimeBetweenFrames() {
		return this.playTime / this.getClips().size();
	}
}