

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

	private GameCanvas canvas;
	private GameState state;

	private long lastTime = 0;

	public GameLoop(GameCanvas canvas, GameState state) {
		this.canvas = canvas;
		this.state = state;
	}

	@Override
	public void handle(long now) {

		if (lastTime == 0) {
			lastTime = now;
			return;
		}

		double deltaTime = (now - lastTime) / 1_000_000_000.0;
		lastTime = now;

		state.update(deltaTime);
		canvas.render();
	}

}
