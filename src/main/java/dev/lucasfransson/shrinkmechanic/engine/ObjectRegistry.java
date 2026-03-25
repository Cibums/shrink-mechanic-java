package dev.lucasfransson.shrinkmechanic.engine;
import dev.lucasfransson.shrinkmechanic.engine.rendering.RenderSystem;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Renderable;
import dev.lucasfransson.shrinkmechanic.engine.tick.ITickable;
import dev.lucasfransson.shrinkmechanic.engine.tick.TickSystem;

public class ObjectRegistry {
	private final TickSystem tickSystem;
	private final RenderSystem renderSystem;
	private final CollisionSystem collisionSystem;

	public ObjectRegistry(TickSystem tickSystem, RenderSystem renderSystem,
			CollisionSystem collisionSystem) {
		this.tickSystem = tickSystem;
		this.renderSystem = renderSystem;
		this.collisionSystem = collisionSystem;
	}

	public <T> T instantiate(T object) {
		if (object instanceof Renderable r)
			renderSystem.register(r);
		if (object instanceof ITickable t)
			tickSystem.register(t);
		if (object instanceof GameObject g)
			collisionSystem.register(g);

		return object;
	}

	public <T> void destroy(T object) {
		if (object instanceof Renderable r)
			renderSystem.unregister(r);
		if (object instanceof ITickable t)
			tickSystem.unregister(t);
		if (object instanceof GameObject g)
			collisionSystem.unregister(g);
	}
}