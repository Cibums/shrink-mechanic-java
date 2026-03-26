package dev.lucasfransson.shrinkmechanic.core;

import dev.lucasfransson.shrinkmechanic.engine.CollisionSystem;
import dev.lucasfransson.shrinkmechanic.engine.Cursor;
import dev.lucasfransson.shrinkmechanic.engine.ObjectRegistry;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Camera;
import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.rendering.RenderSystem;
import dev.lucasfransson.shrinkmechanic.engine.tick.TickSystem;
import dev.lucasfransson.shrinkmechanic.entities.Player;
import dev.lucasfransson.shrinkmechanic.world.GameWorld;
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
		CollisionSystem collisionSystem = new CollisionSystem();
		ObjectRegistry registry = new ObjectRegistry(tickSystem, renderSystem,
				collisionSystem);

		GameWorld world = new GameWorld(registry);
		Player player = registry.instantiate(new Player(input));
		Camera camera = registry.instantiate(new Camera(player));
		GameCanvas canvas = new GameCanvas(stage, renderSystem, camera, input);

		registry.instantiate(new Cursor(input, canvas));

		GameLoop loop = new GameLoop(canvas, tickSystem, renderSystem, camera,
				world, input);
		loop.start();

		layout.getChildren().add(canvas);
		Scene scene = new Scene(layout);

		input.listen(scene);
		input.listenMouseClicks(canvas);

		stage.setScene(scene);
		stage.show();
	}
}
