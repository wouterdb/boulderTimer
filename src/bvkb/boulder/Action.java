package bvkb.boulder;

public abstract class Action {
	
	
	public Action(int at) {
		this.at=at;
	}

	private int at;

	/**
	 * <=0   => from end back
	 * 
	 * @return
	 * 
	 */
	public int at() {
		return at;
	}

	public abstract void run();

}
