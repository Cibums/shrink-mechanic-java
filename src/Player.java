import javafx.scene.input.KeyCode;

public class Player extends Renderable implements ITickable {

	private double walkSpeed = 0.1;

	public Player() {
		super(Renderable.getTextureFromPath("/player.png"));
		this.setRenderingLayer(100);
		this.registerAsTickable();
	}

	@Override
	public void update() {

		InputManager input = GameState.getInstance().getInputManager();

		if (input.isKeyHeld(KeyCode.W)) {
			this.moveVertically(walkSpeed);
		}

		if (input.isKeyHeld(KeyCode.S)) {
			this.moveVertically(-walkSpeed);
		}

		if (input.isKeyHeld(KeyCode.A)) {
			this.moveHorizontally(-walkSpeed);
		}

		if (input.isKeyHeld(KeyCode.D)) {
			this.moveHorizontally(walkSpeed);
		}

		System.out.println("X: " + this.getPosition().getX() + "; Y: "
				+ this.getPosition().getY());
	}

	public void setWalkSpeed(double walkSpeed) {
		this.walkSpeed = walkSpeed;
	}
}
