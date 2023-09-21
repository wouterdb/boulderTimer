package bvkb.boulder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SportTimer {

	public class ActionTask extends TimerTask {

		private final Action action;

		public ActionTask(Action action) {
			this.action = action;
		}

		@Override
		public void run() {
			action.run();

		}

	}

	private long startTime;
	private long stopTime;
	private Timer t = new Timer();
	private final List<Action> actions;
	private final int period;

	public SportTimer(int period, List<Action> actions) {
		this.period = period;
		this.actions = actions;
	}
	
	public SportTimer(int period) {
		this.period = period;
		this.actions = new LinkedList<>();
	}
	
	public void add(Action a){
		actions.add(a);
	}
	
	/**
	 * @return time in 100th of a second
	 */
	public int getTime() {
		return (int) ((System.currentTimeMillis() - startTime) / 10);
	}
	
	/**
	 * @return time in 100th of a second
	 */
	public int getTimeLeft() {
		int nr =  (int) ((stopTime- System.currentTimeMillis()) / 10);
        return Math.max(nr, 0);
    }
	
	
	public void start(){
		long now = System.currentTimeMillis();
		startTime = now;
		stopTime=now+10*period;
		t.cancel();
		t = new Timer();
		loadActions();
		//System.out.println(System.currentTimeMillis());
	}

	private void loadActions() {
		for (Action action : actions) {
			int at = 10* action.at();
			long time = 0;
			if(at <=0 )
				time = stopTime+at;
			else
				time = startTime+at;
			t.schedule(new ActionTask(action), new Date(time));
		}
	}

	public void stop() {
		t.cancel();
		stopTime=System.currentTimeMillis();
		
	}
	
	
}
