package bvkb.boulder;

/**
 * @author w.deborger@gmail.com
 *
 * action to be performed at a certain time 
 */
public abstract class Action {
	
	
	public Action(int at) {
		this.at=at;
	}

	private final int at;

	/**
	 * <=0   => from end back
	 * 
	 * in 100th of a second
	 */
	public int at() {
		return at;
	}

	public abstract void run();

}
