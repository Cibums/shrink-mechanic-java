import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		stage.setWidth(960);
		stage.setHeight(720);

		VBox layout = new VBox();

		GameState state = GameState.getInstance();
		Player player = new Player();
		GameCanvas canvas = new GameCanvas(stage, state, player);

		GameLoop loop = new GameLoop(canvas, state);
		loop.start();

		layout.getChildren().add(canvas);
		Scene scene = new Scene(layout);

		state.getInputManager().listen(scene);

		stage.setFullScreen(false);

		stage.setScene(scene);
		stage.show();
	}
}
