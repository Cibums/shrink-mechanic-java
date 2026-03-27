package dev.lucasfransson.shrinkmechanic.engine.rendering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.IGameSystem;

public class CameraSystem implements IGameSystem {

	private final List<Camera> cameras = new ArrayList<>();

	@Override
	public void tryRegister(Object object) {
		if (object instanceof Camera e)
			register(e);
	}

	private void register(Camera e) {
		cameras.add(e);
	}

	@Override
	public void tryUnregister(Object object) {
		if (object instanceof Camera e)
			unregister(e);
	}

	private void unregister(Camera e) {
		cameras.remove(e);
	}

	public List<Camera> getAllCameras() {
		return Collections.unmodifiableList((List<Camera>) cameras);
	}
}
