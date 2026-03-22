package dev.lucasfransson.shrinkmechanic.engine.rendering;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RenderSystem {
	private final List<Renderable> renderables = new ArrayList<>();
	private boolean dirty = false;

	public void register(Renderable r) {
		renderables.add(r);
		dirty = true;
	}
	public void unregister(Renderable r) {
		renderables.remove(r);
		dirty = true;
	}

	public List<Renderable> getRenderables() {
		if (dirty) {
			renderables.sort(Comparator
					.comparingDouble(Renderable::getRenderingZOffset));
			dirty = false;
		}
		return renderables;
	}
}