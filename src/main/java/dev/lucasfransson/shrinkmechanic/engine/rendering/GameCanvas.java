package dev.lucasfransson.shrinkmechanic.engine.rendering;
import java.util.ArrayList;
import java.util.List;

import dev.lucasfransson.shrinkmechanic.engine.GameConfig;
import dev.lucasfransson.shrinkmechanic.engine.Vector2;
import dev.lucasfransson.shrinkmechanic.engine.Vector2Int;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GameCanvas extends Canvas {

	private double canvasWidth;
	private double canvasHeight;

	private GraphicsContext gc;
	private RenderSystem renderSystem;
	private Camera camera;

	public GameCanvas(Stage stage, RenderSystem renderSystem, Camera camera) {
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

		updateDimensions(stage);
	}

	private void updateDimensions(Stage stage) {
		setCanvasWidth(stage.widthProperty().doubleValue());
		setCanvasHeight(stage.heightProperty().doubleValue());
	}

	private void onStageWindowResize(Stage stage) {
		updateDimensions(stage);
		render();
	}

	private void clearCanvas() {
		gc.clearRect(0, 0, canvasWidth, canvasHeight);
	}

	private void render() {
		double rangeX = canvasWidth
				/ (GameConfig.GRID_CELL_SIZE * camera.getZoom());
		double rangeY = canvasHeight
				/ (GameConfig.GRID_CELL_SIZE * camera.getZoom());

		List<SpriteEntry> spriteEntryBuffer = new ArrayList<>();
		renderSystem.getSpriteEntriesInRange(camera.getPosition(), rangeX,
				rangeY, spriteEntryBuffer);

		render(spriteEntryBuffer);
	}

	public void render(List<SpriteEntry> entries) {

		double zoom = camera.getZoom();

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

		double zoom = camera.getZoom();
		int grid = GameConfig.GRID_CELL_SIZE;

		double worldX = camera.getPosition().x()
				+ (screenPos.x() - canvasWidth / 2.0 + (grid * zoom) / 2.0)
						/ (grid * zoom);
		double worldY = camera.getPosition().y()
				- (screenPos.y() - canvasHeight / 2.0 + (grid * zoom) / 2.0)
						/ (grid * zoom);

		// X uses floor, Y uses ceil because the Y axis is inverted in screen
		// space
		return new Vector2Int((int) Math.floor(worldX),
				(int) Math.ceil(worldY));
	}
}
