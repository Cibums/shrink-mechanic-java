package dev.lucasfransson.shrinkmechanic.core;

import dev.lucasfransson.shrinkmechanic.engine.CollisionSystem;
import dev.lucasfransson.shrinkmechanic.engine.Cursor;
import dev.lucasfransson.shrinkmechanic.engine.ObjectRegistry;
import dev.lucasfransson.shrinkmechanic.engine.input.InputManager;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Camera;
import dev.lucasfransson.shrinkmechanic.engine.rendering.CameraSystem;
import dev.lucasfransson.shrinkmechanic.engine.rendering.GameCanvas;
import dev.lucasfransson.shrinkmechanic.engine.rendering.RenderSystem;
import dev.lucasfransson.shrinkmechanic.engine.rendering.Sprite;
import dev.lucasfransson.shrinkmechanic.engine.tick.TickSystem;
import dev.lucasfransson.shrinkmechanic.entities.EntitySystem;
import dev.lucasfransson.shrinkmechanic.entities.LocalPlayer;
import dev.lucasfransson.shrinkmechanic.entities.RemotePlayer;
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

		stage.getIcons().add(Sprite.getTextureFromPath("/items/log.png"));
		stage.setTitle("Shrink Mechanic");

		VBox layout = new VBox();

		InputManager input = new InputManager();

		TickSystem tickSystem = new TickSystem();
		RenderSystem renderSystem = new RenderSystem();
		CollisionSystem collisionSystem = new CollisionSystem();
		EntitySystem entitySystem = new EntitySystem();
		CameraSystem cameraSystem = new CameraSystem();
		ObjectRegistry registry = new ObjectRegistry(tickSystem, renderSystem,
				collisionSystem, entitySystem, cameraSystem);

		GameWorld world = new GameWorld(registry);

		LocalPlayer player = registry.instantiate(new LocalPlayer(input));
		Camera camera = registry.instantiate(new Camera(player, input));

		registry.instantiate(new RemotePlayer());

		GameCanvas canvas = new GameCanvas(stage, renderSystem, camera);

		registry.instantiate(new Cursor(input, canvas));
		registry.instantiate(new PlayerInteraction(input, canvas, world, registry));

		GameLoop loop = new GameLoop(canvas, tickSystem, renderSystem,
				collisionSystem, camera, world);

		loop.start();

		layout.getChildren().add(canvas);
		Scene scene = new Scene(layout);

		input.listen(scene);
		input.listenMouseClicks(canvas);

		stage.setScene(scene);
		stage.show();
	}
}
