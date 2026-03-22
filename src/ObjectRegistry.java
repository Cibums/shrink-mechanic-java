public class ObjectRegistry {
	private final TickSystem tickSystem;
	private final RenderSystem renderSystem;

	public ObjectRegistry(TickSystem tickSystem, RenderSystem renderSystem) {
		this.tickSystem = tickSystem;
		this.renderSystem = renderSystem;
	}

	public <T extends GameObject> T instantiate(T object) {
		if (object instanceof Renderable r)
			renderSystem.register(r);
		if (object instanceof ITickable t)
			tickSystem.register(t);
		return object;
	}

	public <T extends GameObject> void destroy(T object) {
		if (object instanceof Renderable r)
			renderSystem.unregister(r);
		if (object instanceof ITickable t)
			tickSystem.unregister(t);
	}
}