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

		InputManager input = new InputManager();

		TickSystem tickSystem = new TickSystem();
		RenderSystem renderSystem = new RenderSystem();
		ObjectRegistry registry = new ObjectRegistry(tickSystem, renderSystem);

		GameWorld world = new GameWorld(100, registry);
		GameState state = new GameState(world);
		Player player = registry.instantiate(new Player(input));
		GameCanvas canvas = new GameCanvas(stage, renderSystem, player);

		GameLoop loop = new GameLoop(canvas, tickSystem);
		loop.start();

		layout.getChildren().add(canvas);
		Scene scene = new Scene(layout);

		input.listen(scene);

		stage.setScene(scene);
		stage.show();
	}
}
