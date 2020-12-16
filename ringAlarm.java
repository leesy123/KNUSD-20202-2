package alarmEx;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class ringAlarm extends Thread {
	Player player;
	File file;
	FileInputStream fis;
	BufferedInputStream bis;
	
	public ringAlarm(String fn) {
		try {
			file = new File(fn);
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			player = new Player(bis);
		}
		catch(Exception e){}
	}
	
	public void close() {
		player.close();
	}
	
	@Override
	public void run() {
		try {
			player.play();
		}
		catch(Exception e) {}
	}
	
}
