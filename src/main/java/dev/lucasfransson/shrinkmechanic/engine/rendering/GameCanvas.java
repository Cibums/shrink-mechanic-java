package dev.lucasfransson.shrinkmechanic.engine.rendering;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.entities.Player;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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

		for (Renderable r : renderSystem.getRenderables()) {

			Vector2 objectSpriteSize = r.getSpriteSize();

			Vector2 renderablePosition = r.getPosition();
			Vector2 playerPosition = player.getPosition();

			Vector2 offsetPosition = new Vector2(
					renderablePosition.getX() - playerPosition.getX(),
					renderablePosition.getY() - playerPosition.getY());

			gc.drawImage(r.getTexture(),
					offsetPosition.getX() * GameWorld.gridElementSize
							+ canvasWidth / 2 - GameWorld.gridElementSize / 2,
					-offsetPosition.getY() * GameWorld.gridElementSize / 2
							- GameWorld.gridElementSize / 2 + canvasHeight / 2,
					r.getSpriteSize().getX(), r.getSpriteSize().getY());

			gc.setFill(Color.RED);
			strokeCorners(
					offsetPosition.getX() * GameWorld.gridElementSize
							+ canvasWidth / 2 - GameWorld.gridElementSize / 2,
					-offsetPosition.getY() * GameWorld.gridElementSize / 2
							- GameWorld.gridElementSize / 2 + canvasHeight / 2,
					r.getSize().getX(), r.getSize().getY(), 10);
		}
	}

	private void strokeCorners(double x, double y, double w, double h,
			double len) {
		gc.strokeLine(x, y, x + len, y);
		gc.strokeLine(x, y, x, y + len);
		gc.strokeLine(x + w, y, x + w - len, y);
		gc.strokeLine(x + w, y, x + w, y + len);
		gc.strokeLine(x, y + h, x + len, y + h);
		gc.strokeLine(x, y + h, x, y + h - len);
		gc.strokeLine(x + w, y + h, x + w - len, y + h);
		gc.strokeLine(x + w, y + h, x + w, y + h - len);
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
