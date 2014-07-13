package bvkb.boulder;

import java.awt.Toolkit;

import javax.swing.JLabel;

public class FastLabel extends JLabel {
	long last;
	
	
	@Override
	public void paintImmediately(int x, int y, int w, int h) {
//		long now = System.currentTimeMillis();
//		int fps = (int) (1000/(now-last));
//		last=now;
//		
//		System.out.println(fps);
		super.paintImmediately(x, y, w, h);
		Toolkit.getDefaultToolkit().sync();
	}

}
