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

	private int at;

	/**
	 * <=0   => from end back
	 * 
	 * in 100th of a second
	 * 
	 * @return
	 * 
	 */
	public int at() {
		return at;
	}

	public abstract void run();

}
