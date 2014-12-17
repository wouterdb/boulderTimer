package bvkb.boulder;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SportTimer {

	public class SlaveStopAction extends Action{

		private SlaveTimer timer;

		public SlaveStopAction(SlaveTimer s) {
			super(0);
			this.timer = s;
		}

		@Override
		public void run() {
			timer.halt(0);
		}

	}


	public class ActionTask extends TimerTask {

		private Action action;

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
	private List<Action> actions;
	private int period;
	private SlaveTimer slave;
	private boolean running = false;

	public SportTimer(int period, List<Action> actions) {
		this.period = period;
		this.actions = actions;
	}

	public SportTimer(int period) {
		this.period = period;
		this.actions = new LinkedList<>();
	}

	public void add(Action a) {
		actions.add(a);
	}

	public void setSlave(SlaveTimer s) {
		if (this.slave != null)
			this.slave.close();
		this.slave = s;
		if(slave==null)
			return;
		s.open();
		
		
		SlaveStopAction stop = new SlaveStopAction(s);
		
		
		add(stop);
		
		if (running && getTimeLeft() > 0){
			s.runFrom(stopTime);
			loadAction(stop);
		}else
			s.halt(0);
		
		
		
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
		int nr = (int) ((stopTime - System.currentTimeMillis()) / 10);
		if (nr < 0)
			return 0;
		return nr;
	}

	public void start() {
		running = true;
		long now = System.currentTimeMillis();
		startTime = now;
		stopTime = now + 10 * period;
		if(slave!=null)
			slave.runFrom(stopTime);
		t.cancel();
		t = new Timer();
		loadActions();
		// System.out.println(System.currentTimeMillis());
	}

	private void loadActions() {
		for (Action action : actions) {
			loadAction(action);
		}
	}

	private void loadAction(Action action) {
		int at = 10 * action.at();
		long time = 0;
		if (at <= 0)
			time = stopTime + at;
		else
			time = startTime + at-1;
		t.schedule(new ActionTask(action), new Date(time));
	}

	public void stop() {
		running = false;
		t.cancel();
		stopTime = System.currentTimeMillis();
		if(slave!=null)
			slave.halt(0);

	}

}
