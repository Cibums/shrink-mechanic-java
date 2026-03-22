import java.util.ArrayList;
import java.util.Comparator;

public class GameState {

	private static GameState instance;
	private GameWorld world;

	private ArrayList<ITickable> tickables = new ArrayList<ITickable>();
	private ArrayList<Renderable> renderables = new ArrayList<Renderable>();

	private InputManager inputManager;

	private GameState() {
		instance = this;
		this.inputManager = new InputManager();
		this.world = new GameWorld(100);
	}

	public static GameState getInstance() {
		if (instance == null) {
			instance = new GameState();
		}
		return instance;
	}

	public void update() {

		System.out.println("Updating All");

		for (ITickable t : tickables) {
			t.update();
		}
	}

	public GameWorld getWorld() {
		return world;
	}

	public void registerTickable(ITickable tickable) {
		this.tickables.add(tickable);
	}

	public void registerRenderable(Renderable renderable) {
		this.renderables.add(renderable);
	}

	public ArrayList<Renderable> getRenderables() {
		renderables.sort(
				Comparator.comparingDouble(Renderable::getRenderingZOffset));
		return renderables;
	}

	public InputManager getInputManager() {
		return inputManager;
	}
}