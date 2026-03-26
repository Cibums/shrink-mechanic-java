package dev.lucasfransson.shrinkmechanic.engine.rendering;
import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class GameCanvas extends Canvas {

	private double canvasWidth;
	private double canvasHeight;

	private GraphicsContext gc;
	private RenderSystem renderSystem;
	private Camera camera;
	private InputManager input;

	public GameCanvas(Stage stage, RenderSystem renderSystem, Camera camera,
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
		this.camera = camera;
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

	private void render() {
		double rangeX = canvasWidth / (GameConfig.GRID_CELL_SIZE * zoom);
		double rangeY = canvasHeight / (GameConfig.GRID_CELL_SIZE * zoom);
		render(renderSystem.getSpriteEntriesInRange(camera.getPosition(),
				rangeX, rangeY));
	}

	public void render(List<SpriteEntry> entries) {
		double scroll = input.consumeScrollDelta();
		if (scroll != 0 && input.isKeyHeld(KeyCode.ALT)) {
			zoom += scroll * 0.001;
			zoom = Math.clamp(zoom, 0.25, 5.0);
		}

		clearCanvas();
		Vector2 playerPosition = camera.getPosition();
		gc.setImageSmoothing(false);

		for (SpriteEntry entry : entries) {
			Sprite s = entry.getSprite();
			Vector2 pos = entry.getWorldPosition();

			Vector2 offset = pos.subtract(playerPosition);
			int grid = GameConfig.GRID_CELL_SIZE;

			double cellX = (offset.x() * grid * zoom) + (canvasWidth / 2.0)
					- ((grid * zoom) / 2.0);
			double cellY = (-offset.y() * grid * zoom) + (canvasHeight / 2.0)
					- ((grid * zoom) / 2.0);

			double spriteW = s.getSpriteSize().x() * zoom;
			double spriteH = s.getSpriteSize().y() * zoom;
			double spriteX = cellX + ((grid * zoom) - spriteW) / 2.0;
			double spriteY = cellY - (s.getSpriteYOffset() * zoom);

			Image img = s.hasTint()
					? Sprite.applyTint(s.getTexture(), s.getTint())
					: s.getTexture();

			if (s.getFlipX()) {
				gc.drawImage(img, spriteX + spriteW, spriteY, -spriteW,
						spriteH);
			} else {
				gc.drawImage(img, spriteX, spriteY, spriteW, spriteH);
			}
		}
	}

	public double getZoom() {
		return zoom;
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

	public Vector2Int screenToWorld(Vector2 screenPos) {
		int grid = GameConfig.GRID_CELL_SIZE;
		double worldX = camera.getPosition().x()
				+ (screenPos.x() - canvasWidth / 2.0 + (grid * zoom) / 2.0)
						/ (grid * zoom);
		double worldY = camera.getPosition().y()
				- (screenPos.y() - canvasHeight / 2.0 + (grid * zoom) / 2.0)
						/ (grid * zoom);
		return new Vector2Int((int) Math.floor(worldX),
				(int) Math.ceil(worldY));
	}
}
