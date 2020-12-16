package alarmEx;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class alarmPlay extends JFrame {

	static ArrayList <clock> AlarmList = new ArrayList <clock> ();
	ArrayList <clock> clockList = new ArrayList <clock> ();
	
	int year, month, day, hour, min;
	modAlarm ma;
	delAlarm da;
	alarmAlert ar;
	Container c;
	
	JPanel northPane = new JPanel();
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	JList <String> alarmL = new JList <String> ();
	
	JPanel centerPane = new JPanel();
	JTextField monTf = new JTextField(6);
	JTextField dayTf = new JTextField(6);
	JTextField hourTf = new JTextField(6);
	JTextField minTf = new JTextField(6);
	JLabel monLb = new JLabel("월  ");
	JLabel dayLb = new JLabel("일  ");
	JLabel hourLb = new JLabel("시  ");
	JLabel minLb = new JLabel("분  ");

	JButton addBtn = new JButton("등록");
	JButton modBtn = new JButton("수정");
	JButton delBtn = new JButton("삭제");
		
	RingAlarm ra = new RingAlarm();
	
	alarmPlay() {
		c = getContentPane();
		setTitle("알람 설정");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		northPane.add(new JScrollPane(alarmL), "Center");
		add(northPane, "North");
		
		centerPane.add(monTf);
		centerPane.add(monLb);
		centerPane.add(dayTf);
		centerPane.add(dayLb);
		centerPane.add(hourTf);
		centerPane.add(hourLb);
		centerPane.add(minTf);
		centerPane.add(minLb);
		
		centerPane.add(addBtn);
		centerPane.add(modBtn);
		centerPane.add(delBtn);

		add(centerPane, "Center");
		
		addBtn.addActionListener(new add());
		modBtn.addActionListener(new mod());
		delBtn.addActionListener(new del());
		
		setSize(400, 300);
		setVisible(true);
		ra.start();
	}
	
	class add implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int size = AlarmList.size();
			
			String monStr = monTf.getText();
			String dayStr = dayTf.getText();
			String hourStr = hourTf.getText();
			String minStr = minTf.getText();
			
			String AlarmStr = monStr + "월" + dayStr + "일   " + hourStr + ":" + minStr;
			
			int mon1 = Integer.parseInt(monStr);
			int day1 = Integer.parseInt(dayStr);
			int hour1 = Integer.parseInt(hourStr);
			int min1 = Integer.parseInt(minStr);
			
			if(mon1 >= 1 && mon1 <= 12
					&& day1 >= 1 &&day1 <= 31
					&& hour1 >= 0 && hour1 <= 23
					&& min1 >= 0 && min1 <= 59) {
				AddAlarm(2020, mon1, day1, hour1, min1);
			}
			
			if(AlarmList.size() == size + 1) {
				listModel.addElement(AlarmStr);
				alarmL.setModel(listModel);
				clockList.add(new clock(2020, mon1, day1, hour1, min1));
			}
				
			if(AlarmList.size() == 1)
				ra.startAlarm();			
			
			monTf.setText("");
			dayTf.setText("");
			hourTf.setText("");
			minTf.setText("");
		}
	}
	
	public void modify() {
		ma = new modAlarm(this, "수정");
		ma.setVisible(true);
	}
	
	public class modAlarm extends JDialog {
		JButton yes = new JButton("확인");
		JButton no = new JButton("취소");
		JLabel announce = new JLabel("정말로 알람을 수정하시겠습니까?");
		JPanel modPane = new JPanel();
		
		public modAlarm(JFrame frame, String title) {
			super(frame, title);
			setSize(200, 100);
			setVisible(true);
			modPane.add(announce, "Center");
			modPane.add(yes, "South");
			modPane.add(no, "South");
			add(modPane);
			
			yes.addActionListener(new ActionListener( ) {
				public void actionPerformed(ActionEvent e) {
					int indexA = alarmL.getSelectedIndex();
					
					String monStr = monTf.getText();
					String dayStr = dayTf.getText();
					String hourStr = hourTf.getText();
					String minStr = minTf.getText();
					
					String AlarmStr = monStr + "월" + dayStr + "일   " + hourStr + ":" + minStr;
					
					int mon1 = Integer.parseInt(monStr);
					int day1 = Integer.parseInt(dayStr);
					int hour1 = Integer.parseInt(hourStr);
					int min1 = Integer.parseInt(minStr);
					
					if(mon1 >= 1 && mon1 <= 12 && day1 >= 1 &&day1 <= 31 && hour1 >= 0 && hour1 <= 23 && min1 >= 0 && min1 <= 59) {				
						clock compare = new clock(2020, clockList.get(indexA).month, clockList.get(indexA).day, clockList.get(indexA).hour, clockList.get(indexA).min);
						
						int indexB = findIndex(AlarmList, compare);
						ModAlarm(indexB, 2020, mon1, day1, hour1, min1);				
						listModel.setElementAt(AlarmStr, indexA);
						alarmL.setModel(listModel);
						
						clockList.remove(indexA);
						clockList.add(indexA, new clock(2020, mon1, day1, hour1, min1));
						
						if(indexB == 0) {
							ra.interrupt();
						}
						
						monTf.setText("");
						dayTf.setText("");
						hourTf.setText("");
						minTf.setText("");
					}
					setVisible(false);
				}
			});
			
			no.addActionListener(new ActionListener( ) {
				public void actionPerformed(ActionEvent e) {				
					setVisible(false);
				}
			});		
		}
	}
	
	class mod implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			modify();
		}
	}
	
	public void delete() {
		da = new delAlarm(this, "삭제");
		da.setVisible(true);
	}
	
	public class delAlarm extends JDialog {
		JPanel delPane = new JPanel();
		JButton yes = new JButton("확인");
		JButton no = new JButton("취소");
		JLabel announce = new JLabel("정말로 알람을 삭제하시겠습니까?");
		
		public delAlarm(JFrame frame, String title) {
			super(frame, title);
			setSize(200, 100);
			setVisible(true);
			delPane.add(announce, "Center");
			delPane.add(yes, "South");
			delPane.add(no, "add");
			add(delPane);
			
			yes.addActionListener(new ActionListener( ) {
				public void actionPerformed(ActionEvent e) {
					int indexA = alarmL.getSelectedIndex();
					clock compare = new clock(2020, clockList.get(indexA).month, clockList.get(indexA).day, clockList.get(indexA).hour, clockList.get(indexA).min);
					int indexB = findIndex(AlarmList, compare);
					
					DelAlarm(indexB);
					
					listModel.removeElementAt(indexA);
					alarmL.setModel(listModel);
					
					clockList.remove(indexA);
					
					if(indexB == 0) {
						ra.interrupt();
					}
					setVisible(false);
				}
			});
			
			no.addActionListener(new ActionListener( ) {
				public void actionPerformed(ActionEvent e) {				
					setVisible(false);
				}
			});
		
		}
			
	}
	
	class del implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			delete();
		}
	}

	class RingAlarm extends Thread {
		boolean alarmExist = false;
		
		synchronized public void waitAlarm() {
			if(!alarmExist) {
				try {
					this.wait();
				}
				catch(Exception e) {}
			}
		}
		
		synchronized public void startAlarm() {
			alarmExist = true;
			this.notify();
		}
		
		public void run() {
			while(true) {
				waitAlarm();
				try {					
					year = AlarmList.get(0).year;
					month = AlarmList.get(0).month;
					day = AlarmList.get(0).day;
					hour = AlarmList.get(0).hour;
					min = AlarmList.get(0).min;					
					
					//저장된 시간까지 대기
					sleep(timeUntil(year, month, day, hour, min));
					
					//작업을 수행
					int indexA = findIndex(clockList, AlarmList.get(0));
					
					listModel.removeElementAt(indexA);
					alarmL.setModel(listModel);
					clockList.remove(indexA);
					
					ring();

					AlarmList.remove(0);
					
				} catch (InterruptedException e) {}
				
				if(AlarmList.isEmpty() == true)
					alarmExist = false;
			}
		}
	}

	public void ring(){
		ar = new alarmAlert(this, "Alert!");
		ar.setVisible(true);
	}
	
	public class alarmAlert extends JDialog {
		JButton stopAlarm = new JButton("알람 종료");
		ringAlarm music = new ringAlarm("alarmSound.wav");
		JLabel announce = new JLabel("알람이 울립니다!");
		
		public alarmAlert(JFrame frame, String title) {
			super(frame, title);
			setSize(200, 100);
			setVisible(true);
			add(announce, "Center");
			add(stopAlarm, "South");
			music.start();
			
			stopAlarm.addActionListener(new ActionListener( ) {
				public void actionPerformed(ActionEvent e) {
					music.close();
					setVisible(false);
				}
			});
		}		
	}
	
	public long timeUntil(int year, int month, int day, int hour, int min){
		Date now = new Date();
		Calendar calUntil = Calendar.getInstance();
		calUntil.clear();
		calUntil.set(year, month - 1, day, hour, min);
		Date until = calUntil.getTime();
		long sleep = until.getTime() - now.getTime();
		
		return sleep;
	}
	
	public static void AddAlarm(int year, int month, int day, int hour, int min) {
		boolean isAdd = false;
		if(AlarmList.size() == 0) {
			AlarmList.add(new clock(year, month, day, hour, min));
			isAdd = true;
		}
		else if(AlarmList.size() > 0 && AlarmList.size() < 11) {
			for(int i = 0; i < AlarmList.size(); i++) {
				if(month < AlarmList.get(i).month) {
					AlarmList.add(i, new clock(year, month, day, hour, min));
					isAdd = true;
					break;
				}
				else if(month == AlarmList.get(i).month) {
					if(day < AlarmList.get(i).day) {
						AlarmList.add(i, new clock(year, month, day, hour, min));
						isAdd = true;
						break;
					}
					else if(day == AlarmList.get(i).day) {
						if(hour < AlarmList.get(i).hour) {
							AlarmList.add(i, new clock(year, month, day, hour, min));
							isAdd = true;
							break;
						}
						else if(hour == AlarmList.get(i).hour) {
							if(min < AlarmList.get(i).min) {
								AlarmList.add(i, new clock(year, month, day, hour, min));
								isAdd = true;
								break;
							}
						}
					}
				}

				if(i == AlarmList.size() - 1 && isAdd == false) {
					AlarmList.add(i + 1, new clock(year, month, day, hour, min));
					isAdd = true;
					break;
				}					
			}
		}
	}

	public static void ModAlarm(int index, int year, int month, int day, int hour, int min) {
		AlarmList.remove(index);
		AddAlarm(year, month, day, hour, min);
	}

	public static void DelAlarm(int index) {
		AlarmList.remove(index);
	}
	
	public static int findIndex(ArrayList<clock> a, clock c) {
		int n = -1;
		for(int i = 0; i < a.size(); i++) {
			if(a.get(i).month == c.month
					&&a.get(i).day == c.day
					&&a.get(i).hour == c.hour
					&&a.get(i).min == c.min) {
				n = i;
				break;
			}			
		}
		return n;
	}
	
	public static void main(String [] args) {
		new alarmPlay();
	}
}

/*
참고
https://ismydream.tistory.com/151
https://www.youtube.com/watch?v=oFs7FPpf5-w
http://www.javazoom.net/javalayer/javalayer.html
*/