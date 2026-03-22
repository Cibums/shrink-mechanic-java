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

	public void update() {
		for (ITickable t : tickables) {
			t.update();
		}
	}
}