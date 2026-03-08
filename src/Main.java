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

		VBox layout = new VBox();
		GameCanvas canvas = new GameCanvas(stage);
		layout.getChildren().add(canvas);
		Scene scene = new Scene(layout);

		stage.setFullScreen(true);

		stage.setScene(scene);
		stage.show();
	}
}
