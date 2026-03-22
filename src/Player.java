import javafx.scene.input.KeyCode;

public class Player extends Renderable implements ITickable {

	private InputManager input;
	private double walkSpeed = 0.1;

	public Player(InputManager input) {
		super(Renderable.getTextureFromPath("/player.png"));
		this.input = input;
		this.setRenderingLayer(100);
	}

	@Override
	public void update() {

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
