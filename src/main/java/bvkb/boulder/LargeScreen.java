package bvkb.boulder;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class LargeScreen extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JLabel label;
	private Timer t ;
	private SportTimer source;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					SportTimer spt = new SportTimer(100000);
					spt.start();
					LargeScreen frame = new LargeScreen(spt);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LargeScreen(SportTimer source) {
		setDefaultCloseOperation(HIDE_ON_CLOSE );
		setBounds(100, 100, 450, 300);
		setExtendedState(getExtendedState()|JFrame.MAXIMIZED_BOTH );
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		label = new FastLabel();
		contentPane.add(label,BorderLayout.CENTER);
		
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		
		this.source  = source;
		t = new Timer(17, this);
		t.setRepeats(true);
		t.start();
	}

	long last;
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		long now = System.currentTimeMillis();
//		int fps = (int) (1000/(now-last));
//		last=now;
		
		label.setText(PresentationUtil.formatTime(source.getTimeLeft()));
		//label.repaint(10);
	}
	

	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
	}

	

	public void setTimer(SportTimer timer) {
		this.source=timer;
		
	}

	
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		label.setFont(f);
	}
}
