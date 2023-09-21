package bvkb.boulder;

import java.util.concurrent.TimeUnit;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

/**
 * @author w.deborger@gmail.com
 *
 * place where all specific formatting is collected
 */
public class PresentationUtil {

	
	public static String formatTime(int time) {
		//seconds ROUND UP!
		int seconds = (int)Math.ceil( (time%6000)/100.0 );
		int minutes = time/6000;
		if(seconds==60){
			seconds=0;
			minutes++;
		}
		return String.format("%02d:%02d", minutes,seconds);
	}
	
	
	private static final String[] timeNames = new String[]    {	"m","s","h"};
	private static final TimeUnit[] times = new TimeUnit[]	 {	TimeUnit.MINUTES, TimeUnit.SECONDS, TimeUnit.HOURS};
	private final static Pattern timepattern = Pattern.compile("([0-9.,]+)([a-zA-Z ]*)");
	
	public static long getTime(String input, String msg) {
		TimeUnit t = TimeUnit.MINUTES;
		
		Matcher m = timepattern.matcher(input);
		if(!m.matches())
			throw new IllegalArgumentException(msg);
		
		MatchResult mr = m.toMatchResult();
		
		int nr =  Integer.parseInt(mr.group(1));
		
		String unit = mr.group(2);
		
		for (int i = 0; i < timeNames.length; i++) {
			if(unit.toLowerCase().contains(timeNames[i])){
				t = times[i];
				break;
			}
		}
		
		return t.toMillis(nr)/10;
		
		
		
	}
}
