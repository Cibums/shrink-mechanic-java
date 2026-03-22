
public class GameObject {

	private Vector2 position;

	public GameObject() {
		this(Vector2.zero());
	}

	public GameObject(Vector2 position) {
		this.setPosition(position);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public void moveHorizontally(double speed) {
		this.move(speed, 0);
	}

	public void moveVertically(double speed) {
		this.move(0, speed);
	}

	public void move(double x, double y) {
		this.move(new Vector2(x, y));
	}

	public void move(Vector2 vector) {
		this.position = new Vector2(this.position.getX() + vector.getX(),
				this.position.getY() + vector.getY());
	}
}
