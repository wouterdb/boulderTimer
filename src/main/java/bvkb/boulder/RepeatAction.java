package bvkb.boulder;

public class RepeatAction extends Action{

	private SportTimer target;
	private int count;

	public RepeatAction(SportTimer target, int at, int count) {
		super(at);
		this.target=target;
		this.count=count;
	}

	@Override
	public void run() {
		if(count>0){
			target.start();
			count--;
		}
	}

	public int getRepeatsLeft(){
		return count;
	}

	public void stop() {
		count=0;
	}
}
