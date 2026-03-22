package dev.lucasfransson.shrinkmechanic.engine.rendering;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.entities.Player;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class GameCanvas extends Canvas {

	private double canvasWidth;
	private double canvasHeight;

	private GraphicsContext gc;
	private RenderSystem renderSystem;
	private Player player;

	public GameCanvas(Stage stage, RenderSystem renderSystem, Player player) {
		super(stage.widthProperty().doubleValue(),
				stage.heightProperty().doubleValue());

		this.widthProperty().bind(stage.widthProperty());
		this.heightProperty().bind(stage.heightProperty());

		ChangeListener<Number> stageSizeListener = (observable, oldValue,
				newValue) -> onStageWindowResize(stage);

		stage.widthProperty().addListener(stageSizeListener);
		stage.heightProperty().addListener(stageSizeListener);

		gc = this.getGraphicsContext2D();
		this.renderSystem = renderSystem;
		this.player = player;
	}

	private void onStageWindowResize(Stage stage) {
		setCanvasWidth(stage.widthProperty().doubleValue());
		setCanvasHeight(stage.heightProperty().doubleValue());
		render();
	}

	private void clearCanvas() {
		gc.clearRect(0, 0, canvasWidth, canvasHeight);
	}

	public void render() {
		clearCanvas();

		int tileSize = 100;

		for (Renderable r : renderSystem.getRenderables()) {

			Vector2 renderablePosition = r.getPosition();
			Vector2 playerPosition = player.getPosition();

			Vector2 offsetPosition = new Vector2(
					renderablePosition.getX() - playerPosition.getX(),
					renderablePosition.getY() - playerPosition.getY());

			double x = renderablePosition.getX();
			double y = renderablePosition.getY();

			gc.drawImage(r.getTexture(),
					(offsetPosition.getX()) * tileSize + canvasWidth / 2
							- tileSize / 2,
					-(offsetPosition.getY()) * (tileSize / 2) + canvasHeight / 2
							- tileSize / 2,
					tileSize, tileSize);
		}
	}

	public double getCanvasWidth() {
		return canvasWidth;
	}

	public void setCanvasWidth(double canvasWidth) {
		this.canvasWidth = canvasWidth;
	}

	public double getCanvasHeight() {
		return canvasHeight;
	}

	public void setCanvasHeight(double canvasHeight) {
		this.canvasHeight = canvasHeight;
	}

}
