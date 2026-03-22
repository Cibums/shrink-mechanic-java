package dev.lucasfransson.shrinkmechanic.engine.tick;
import java.util.ArrayList;
import java.util.List;

public class TickSystem {
	private final List<ITickable> tickables = new ArrayList<>();

	public void register(ITickable t) {
		tickables.add(t);
	}
	public void unregister(ITickable t) {
		tickables.remove(t);
	}

	public void update(double deltaTime) {
		for (ITickable t : List.copyOf(tickables)) {
			t.update(deltaTime);
		}
	}
}