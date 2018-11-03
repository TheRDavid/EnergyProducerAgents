package environmemnt;

public abstract class EnvironmentFactor<E> {
	public abstract void update();

	protected E value;

	public E value() {
		return value;
	}
}
