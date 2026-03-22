
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

	private GameCanvas canvas;
	private TickSystem tickSystem;

	public GameLoop(GameCanvas canvas, TickSystem tickSystem) {
		this.canvas = canvas;
		this.tickSystem = tickSystem;
	}

	long lastUpdate = System.nanoTime();

	@Override
	public void handle(long now) {
		long currentTime = System.nanoTime();
		if (currentTime - lastUpdate >= 16_666_667) {
			tickSystem.update();
			canvas.render();
			lastUpdate = currentTime;
		}
	}

}
