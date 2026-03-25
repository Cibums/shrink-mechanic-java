package dev.lucasfransson.shrinkmechanic.engine.rendering;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.entities.Player;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
import dev.lucasfransson.shrinkmechanic.world.objects.WorldObject;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameCanvas extends Canvas {

	private boolean debugMode = false;

	private double canvasWidth;
	private double canvasHeight;

	private GraphicsContext gc;
	private RenderSystem renderSystem;
	private Player player;
	private InputManager input;

	public GameCanvas(Stage stage, RenderSystem renderSystem, Player player,
			InputManager input) {
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
		this.input = input;

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

	private double zoom = 1.5;

	public void render() {

		if (input.isKeyHeld(KeyCode.H)) {
			debugMode = true;
		} else {
			debugMode = false;
		}

		double scroll = input.consumeScrollDelta();

		if (scroll != 0 && input.isKeyHeld(KeyCode.ALT)) {
			zoom += scroll * 0.001;
			zoom = Math.max(0.25, Math.min(zoom, 5.0));
		}

		clearCanvas();

		Vector2 playerPosition = player.getPosition();

		gc.setImageSmoothing(false);

		for (Renderable r : renderSystem.getRenderablesInRange(playerPosition,
				canvasWidth / (GameWorld.gridElementSize * zoom),
				canvasHeight / (GameWorld.gridElementSize * zoom))) {

			Vector2 offset = r.getPosition().subtract(playerPosition);
			Vector2 size = r.getSize();
			int grid = GameWorld.gridElementSize;

			double cellX = (offset.getX() * grid * zoom) + (canvasWidth / 2.0)
					- ((grid * zoom) / 2.0);
			double cellY = (-offset.getY() * grid * zoom) + (canvasHeight / 2.0)
					- ((grid * zoom) / 2.0);

			double spriteW = r.getSpriteSize().getX() * zoom;
			double spriteH = r.getSpriteSize().getY() * zoom;
			double spriteX = cellX + ((grid * zoom) - spriteW) / 2.0;
			double spriteY = cellY - (r.getSpriteYOffset() * zoom);

			gc.drawImage(r.getTexture(), spriteX, spriteY, spriteW, spriteH);

			if (debugMode) {
				double collW = size.getX() * grid * zoom;
				double collH = size.getY() * grid * zoom;
				double collX = cellX + ((grid * zoom) - collW) / 2.0;
				double collY = cellY + ((grid * zoom) - collH) / 2.0;

				Color debugColor = r instanceof WorldObject
						? Color.RED
						: Color.BLACK;
				strokeCorners(collX, collY, collW, collH, 10, debugColor);
			}
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
