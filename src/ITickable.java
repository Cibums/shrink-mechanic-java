public interface ITickable {
	void update();

	default void registerAsTickable() {
		GameState.getInstance().registerTickable(this);
	}
}