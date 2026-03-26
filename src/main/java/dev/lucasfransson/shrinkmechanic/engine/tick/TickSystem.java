package dev.lucasfransson.shrinkmechanic.engine.tick;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import dev.lucasfransson.shrinkmechanic.engine.IGameSystem;

public class TickSystem implements IGameSystem {

	private final Set<ITickable> tickables = new LinkedHashSet<>();

	public void register(ITickable t) {
		tickables.add(t);
	}

	public void unregister(ITickable t) {
		tickables.remove(t);
	}

	@Override
	public void tryRegister(Object object) {
		if (object instanceof ITickable t)
			register(t);
	}

	@Override
	public void tryUnregister(Object object) {
		if (object instanceof ITickable t)
			unregister(t);
	}

	public void update(double deltaTime) {
		for (ITickable t : List.copyOf(tickables)) {
			t.update(deltaTime);
		}
	}
}