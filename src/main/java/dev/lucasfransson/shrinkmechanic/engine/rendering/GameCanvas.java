package dev.lucasfransson.shrinkmechanic.engine.rendering;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.entities.Player;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
import dev.lucasfransson.shrinkmechanic.world.objects.WorldObject;
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

		onStageWindowResize(stage);
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
			Vector2 offset = r.getPosition().subtract(player.getPosition());
			Vector2 size = r.getSize();
			int grid = GameWorld.gridElementSize;

			double x = (offset.getX() * grid)
					+ (canvasWidth - size.getX()) / 2.0;
			double y = (-offset.getY() * grid)
					+ (canvasHeight - size.getY()) / 2.0;

			gc.drawImage(r.getTexture(), x, y - r.getSpriteYOffset(),
					r.getSpriteSize().getX(), r.getSpriteSize().getY());

			Color debugColor = r instanceof WorldObject
					? Color.RED
					: Color.BLACK;

			strokeCorners(x, y, size.getX(), size.getY(), 10, debugColor);
		}
	}

	private void strokeCorners(double x, double y, double w, double h,
			double len, Color color) {
		gc.setStroke(color);
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
