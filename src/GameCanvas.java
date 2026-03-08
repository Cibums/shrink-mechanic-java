import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameCanvas extends Canvas {

	private double canvasWidth;
	private double canvasHeight;

	private GraphicsContext gc;

	public GameCanvas(Stage stage) {
		super(stage.widthProperty().doubleValue(),
				stage.heightProperty().doubleValue());

		this.widthProperty().bind(stage.widthProperty());
		this.heightProperty().bind(stage.heightProperty());

		ChangeListener<Number> stageSizeListener = (observable, oldValue,
				newValue) -> onStageWindowResize(stage);

		stage.widthProperty().addListener(stageSizeListener);
		stage.heightProperty().addListener(stageSizeListener);

		gc = this.getGraphicsContext2D();
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

		System.out
				.println("Width: " + canvasWidth + "; Height: " + canvasHeight);

		this.gc.setFill(Color.RED);
		this.gc.fillRect(canvasWidth / 2 - 25, canvasHeight / 2 - 25, 50, 50);
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
