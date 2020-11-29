package example;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.Player;


public class alarmPlay extends JFrame {

	//알람 저장할 ArrayList
	static ArrayList <clock> AlarmList = new ArrayList <clock> ();
	//변수 선언
	int year, month, day, hour, min;
	alarmAlert ar;
	Container c;
	static boolean alarmExist = false;
	
	//임시용 알람 관련 실행창	
	alarmPlay() {

		RingAlarm ra = new RingAlarm();
		
		c = getContentPane();
		setTitle("Alarm");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 700);
		setVisible(true);
		ra.start();
	}
	
	class RingAlarm extends Thread {
		public void run() {
			while(true) {
				//저장된 알람이 없을때 대기
				if(!alarmExist) {
					continue;
				}
				//저장된 알람이 있을때 실행
				else if(alarmExist) {
					try {
						year = AlarmList.get(0).year;
						month = AlarmList.get(0).month;
						day = AlarmList.get(0).day;
						hour = AlarmList.get(0).hour;
						min = AlarmList.get(0).min;
										
						//저장된 시간까지 대기
						sleep(timeUntil(year, month, day, hour, min));				
						//작업을 수행
						ring();					
						
					} catch (InterruptedException e) {}	
					
					AlarmList.remove(0);
					if(AlarmList.isEmpty())
						alarmExist = false;
				}
			}
		}
	}
	
	public void ring(){
		ar = new alarmAlert(this, "Alert!");
		ar.setVisible(true);
	}
	
	//알람 음성 재생
	public static void AlarmSound(String fn) {
		try {
			FileInputStream file = new FileInputStream(new File(fn));
			Player player = new Player(file);
			player.play();
		} catch (Exception e) {
		}	
	}
	
	// 저장된 시간까지 남은 시간 계산
	public long timeUntil(int year, int month, int day, int hour, int min){
		Date now = new Date();
		Calendar calUntil = Calendar.getInstance();
		calUntil.set(Calendar.YEAR, year);
		calUntil.set(Calendar.MONTH, month - 1);
		calUntil.set(Calendar.DAY_OF_WEEK, day);
		calUntil.set(Calendar.HOUR_OF_DAY, hour);
		calUntil.set(Calendar.MINUTE, min);
		calUntil.set(Calendar.SECOND, 0);
		Date until = calUntil.getTime();
		long sleep = until.getTime() - now.getTime();
		return sleep;
	}
	
	//시간이 다 됐을때 띄워지는 팝업창
	public class alarmAlert extends JDialog {
		public alarmAlert(JFrame frame, String title) {
			super(frame, title);
			setSize(200, 100);
			setVisible(true);
			AlarmSound("alarmSound.wav");
			//창 닫으면서 음원 종료할 버튼 추가해야됨
		}		
	}
	
	//알람 추가
	public static void AddAlarm(int year, int month, int day, int hour, int min) {		
		boolean isAdd = false;
		if(AlarmList.size() == 0) {
			AlarmList.add(new clock(year, month, day, hour, min));
			isAdd = true;
			alarmExist = true;
		}
		else if(AlarmList.size() == 10) {
			//경고 팝업창 추가
			System.out.println("알람의 개수가 10개를 넘을 수 없습니다.");
		}
		else if(AlarmList.size() > 0 && AlarmList.size() < 11) {
			for(int i = 0; i < AlarmList.size(); i++) {
				if(month < AlarmList.get(i).month) {
					AlarmList.add(i, new clock(year, month, day, hour, min));
					isAdd = true;
					alarmExist = true;
					break;
				}
				else if(month == AlarmList.get(i).month) {
					if(day < AlarmList.get(i).day) {
						AlarmList.add(i, new clock(year, month, day, hour, min));
						isAdd = true;
						alarmExist = true;
						break;
					}
					else if(day == AlarmList.get(i).day) {
						if(hour < AlarmList.get(i).hour) {
							AlarmList.add(i, new clock(year, month, day, hour, min));
							isAdd = true;
							alarmExist = true;
							break;
						}
						else if(hour == AlarmList.get(i).hour) {
							if(min < AlarmList.get(i).min) {
								AlarmList.add(i, new clock(year, month, day, hour, min));
								isAdd = true;
								alarmExist = true;
								break;
							}
							else if(min == AlarmList.get(i).min) {
								//경고 팝업창 추가
								System.out.println("이미 등록된 알람입니다.");
								isAdd = true;
								break;
							}
						}
					}
				}
				
				if(i == AlarmList.size() - 1 && isAdd == false) {
					AlarmList.add(i + 1, new clock(year, month, day, hour, min));
					isAdd = true;
					alarmExist = true;
					break;
				}					
			}
		}		
	}
		
	//알람 수정(필요 변수 : 해당 알람이 속한 ArrayList의 index)
	public static void ModAlarm(int index, int year, int month, int day, int hour, int min) {
		AlarmList.remove(index - 1);
		AddAlarm(year, month, day, hour, min);
	}
	
	//알람 삭제(필요 변수 : 해당 알람이 속한 ArrayList의 index)
	public static void DelAlarm(int index) {
		AlarmList.remove(index - 1);
	}
}


/*
참고한 링크
https://ismydream.tistory.com/151
https://www.youtube.com/watch?v=oFs7FPpf5-w
http://www.javazoom.net/javalayer/javalayer.html

음원 출처
https://www.mewpot.com/
*/