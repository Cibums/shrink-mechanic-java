
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

	private GameCanvas canvas;
	private GameState state;

	public GameLoop(GameCanvas canvas, GameState state) {
		this.canvas = canvas;
		this.state = state;
	}

	long lastUpdate = System.nanoTime();

	@Override
	public void handle(long now) {
		long currentTime = System.nanoTime();
		if (currentTime - lastUpdate >= 16_666_667) {
			state.update();
			canvas.render();
			lastUpdate = currentTime;
		}
	}

}
