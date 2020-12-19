package practical.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.parser.ParseException;

import practical.alarm.alarmPlay;
import practical.memo.memo01;
import practical.schedule.schedule_UI;
import practical.user.user_UI;

public class Mycalendar extends JFrame implements ActionListener {

	JPanel topPane = new JPanel();
	JButton prevBtn = new JButton("<");
	JButton nextBtn = new JButton(">");
	JLabel yearLbl = new JLabel("year");
	JLabel monthLbl = new JLabel("month");
	
	JComboBox<Integer> yearCombo = new JComboBox<Integer>();
	DefaultComboBoxModel<Integer> yearModel = new DefaultComboBoxModel<Integer>();
	JComboBox<Integer> monthCombo = new JComboBox<Integer>();
	DefaultComboBoxModel<Integer> monthModel = new DefaultComboBoxModel<Integer>();
	
	//Center
	JPanel centerPane = new JPanel(new BorderLayout());
	JPanel titlePane = new JPanel(new GridLayout(1, 7));
	String titlestr[] = {"Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat"};
	JPanel datePane = new JPanel(new GridLayout(0, 7));
	
	//bottom
	JButton userBtn = new JButton("user info");
	JButton memoBtn = new JButton("memo");
	JButton scheduleBtn = new JButton("schedule");
	JButton alarmBtn = new JButton("alarm");
	JPanel btnPane = new JPanel(new GridLayout(2,2));
	
	Calendar now;
	int year, month, date;
	
	public Mycalendar() throws IOException, ParseException {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		now = Calendar.getInstance(); //Today date
		year = now.get(Calendar.YEAR);
		month = now.get(Calendar.MONTH) + 1;
		date = now.get(Calendar.DATE);
		
		topPane.add(prevBtn);
		yearModel.addElement(year);
		
		yearCombo.setModel(yearModel);
		yearCombo.setSelectedItem(year); //Current year select
		topPane.add(yearCombo);

		topPane.add(yearLbl);
		
		for (int i = 1; i <= 12; i++)
			monthModel.addElement(i);
		
		monthCombo.setModel(monthModel);
		monthCombo.setSelectedItem(month); //Current month select
		topPane.add(monthCombo);
		
		topPane.add(monthLbl);
		
		topPane.add(nextBtn);
		
		topPane.setBackground(new Color(100, 200, 200));
		add(topPane, "North");
		
		//Center
		titlePane.setBackground(Color.white);
		for (int i = 0; i < titlestr.length; i++) {
			JLabel lbl = new JLabel(titlestr[i], JLabel.CENTER);
			
			if (i == 0)
				lbl.setForeground(Color.RED);
			else if (i == 6)
				lbl.setForeground(Color.BLUE);
			
			titlePane.add(lbl);
		}	
		centerPane.add(titlePane, "North");
		
		btnPane.add(userBtn);
		btnPane.add(memoBtn);
		btnPane.add(scheduleBtn);
		btnPane.add(alarmBtn);
		centerPane.add(btnPane, "South");
		
		//Print days
		dayPrint(year, month);
		centerPane.add(datePane, "Center");
		
		add(centerPane, "Center");
		
		setSize(400, 500);
		setVisible(true);
		
		userBtn.addActionListener(this);
		memoBtn.addActionListener(this);
		scheduleBtn.addActionListener(this);
		alarmBtn.addActionListener(this);
		
		prevBtn.addActionListener(this);
		nextBtn.addActionListener(this);
		yearCombo.addActionListener(this);
		monthCombo.addActionListener(this);
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if (obj instanceof JButton) {
			JButton eventBtn = (JButton)obj;
			int yy = (int)yearCombo.getSelectedItem();
			int mm = (int)monthCombo.getSelectedItem();
			
			if (eventBtn.equals(prevBtn)) { //Previous month
				if (mm != 1)
					mm--;
			}
			
			else if (eventBtn.equals(nextBtn)) { //Next month
				if (mm != 12)
					mm++;
			}
			
			else if (eventBtn.equals(userBtn)) {
				File file = new File("userinfo.txt");
				user_UI ui = new user_UI();

				if(ui.U != null) {
					try {
						BufferedWriter out = new BufferedWriter(new FileWriter("userinfo.txt"));
						out.write(ui.U.name);
						out.close();
					} catch (IOException err) {
						System.out.println("could not write text");
					}
				}
			}
			else if (eventBtn.equals(memoBtn)) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							memo01 window = new memo01();
							window.frame.setVisible(true);
						} catch (Exception err) {
							err.printStackTrace();
						}
					}
				});
			}
			else if (eventBtn.equals(scheduleBtn)) {
				new schedule_UI();
			}
			else if (eventBtn.equals(alarmBtn)) {
				new alarmPlay();
			}
			
			yearCombo.setSelectedItem(yy);
			monthCombo.setSelectedItem(mm);
		}
		
		else if (obj instanceof JComboBox)
			try {
				createDayStart();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	
	public void createDayStart() throws IOException, ParseException {
		datePane.setVisible(false); //Panel hide
		datePane.removeAll(); //날짜 출력한 label 지우기
		dayPrint((int)yearCombo.getSelectedItem(), (int)monthCombo.getSelectedItem());
		datePane.setVisible(true); //Panel print again
	}
	
	public void dayPrint(int year, int month) throws IOException, ParseException {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, 1); //출력할 첫 날의 object
		int week = cal.get(Calendar.DAY_OF_WEEK); //1일에 대한 요일
		int lastDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //그 달의 마지막 날
		int y = 0;
		JLabel lbl1 = new JLabel("");
		JLabel lbl2 = new JLabel("");
		
		for (int i = 1; i < week; i++) //날짜 출력 전까지의 공백 print
			datePane.add(new JLabel(" "));
		
		for (int i = 1; i <= lastDate; i++) {
			JLabel lbl = new JLabel(String.valueOf(i), JLabel.CENTER);
			cal.set(year, month-1, i);
			int outWeek = cal.get(Calendar.DAY_OF_WEEK);
			String mm, dd;
			
			if (month < 10)
				mm = "0" + Integer.toString(month);
			else
				mm = Integer.toString(month);
			
			if (i < 10)
				dd = "0" + Integer.toString(i);
			else
				dd = Integer.toString(i);
			
			String date = Integer.toString(year) + mm + dd;
			
			//올해 2020년의 경우에는 윤음력이 있어, 윤음력인 5/23 ~ 6/20은 공휴일에서 제외
			if ((month == 5 && i >= 23) || (month == 6 && i <= 20)) {
				if (outWeek == 1 || Holiday.LiftHoliday(date))
					lbl.setForeground(Color.RED);
				else if (outWeek == 7)
					lbl.setForeground(Color.BLUE);
			}
			
			else if (outWeek == 1 || Holiday.isLunar(date) || Holiday.LiftHoliday(date)) {
				lbl.setForeground(Color.RED);
			}
			else if (outWeek == 7)
				lbl.setForeground(Color.BLUE);
			
			datePane.add(lbl);
			
			Weather[] w_arr = Weather.weather();			
			if (outWeek == 7) {
				for (int j = 0; j < 7; j++) {
					int temp_day = i - (6 - j);
					
					if ((year == this.year) && (month == this.month) && (temp_day == this.date)) {
						int k;
						
						String currentProjPath = "";
						try {
							currentProjPath = new File(".").getCanonicalPath();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						JLabel rainLabel, sunnyLabel, lcLabel, mcLabel, cdLabel;
						
						for (k = 0; k < 3; k++) {
							if (w_arr[k].pop != 0) {
								ImageIcon rainIcon = new ImageIcon(getClass().getResource("rain.jpg"));
								Image img = rainIcon.getImage();
								Image changeImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
								ImageIcon changeIcon = new ImageIcon(changeImg);
								rainLabel = new JLabel(changeIcon);
								if ((j == 5 && k == 2) || (j == 6 && k == 1) || (j == 6 && k == 2)) {
									if ((j == 5 && k == 2) || (j == 6 && k == 1))
										lbl1 = new JLabel(changeIcon);
									else if (j == 6 && k == 2)
										lbl2 = new JLabel(changeIcon);
									y++;
									continue;
								}
								else
									datePane.add(rainLabel);
							} else if (w_arr[k].pop == 0 && w_arr[k].sky == 1) {
								ImageIcon sunnyIcon = new ImageIcon(getClass().getResource("Sunny.jpg"));
								Image img = sunnyIcon.getImage();
								Image changeImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
								ImageIcon changeIcon = new ImageIcon(changeImg);
								sunnyLabel = new JLabel(changeIcon);
								if ((j == 5 && k == 2) || (j == 6 && k == 1) || (j == 6 && k == 2)) {
									if ((j == 5 && k == 2) || (j == 6 && k == 1))
										lbl1 = new JLabel(changeIcon);
									else if (j == 6 && k == 2)
										lbl2 = new JLabel(changeIcon);
									y++;
									continue;
								}
								else
									datePane.add(sunnyLabel);
							} else if (w_arr[k].pop == 0 && w_arr[k].sky == 2) {
								ImageIcon lcIcon = new ImageIcon(getClass().getResource("little cloud.jpg"));
								Image img = lcIcon.getImage();
								Image changeImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
								ImageIcon changeIcon = new ImageIcon(changeImg);
								lcLabel = new JLabel(changeIcon);
								if ((j == 5 && k == 2) || (j == 6 && k == 1) || (j == 6 && k == 2)) {
									if ((j == 5 && k == 2) || (j == 6 && k == 1))
										lbl1 = new JLabel(changeIcon);
									else if (j == 6 && k == 2)
										lbl2 = new JLabel(changeIcon);
									y++;
									continue;
								}
								else
									datePane.add(lcLabel);
							} else if (w_arr[k].pop == 0 && w_arr[k].sky == 3) {
								ImageIcon mcIcon = new ImageIcon(getClass().getResource("many cloud.jpg"));
								Image img = mcIcon.getImage();
								Image changeImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
								ImageIcon changeIcon = new ImageIcon(changeImg);
								mcLabel = new JLabel(changeIcon);
								if ((j == 5 && k == 2) || (j == 6 && k == 1) || (j == 6 && k == 2)) {
									if ((j == 5 && k == 2) || (j == 6 && k == 1))
										lbl1 = new JLabel(changeIcon);
									else if (j == 6 && k == 2)
										lbl2 = new JLabel(changeIcon);
									y++;
									continue;
								}
								else
									datePane.add(mcLabel);
							} else if (w_arr[k].pop == 0 && w_arr[k].sky == 4) {
								ImageIcon cdIcon = new ImageIcon(getClass().getResource("cloudy.png"));
								Image img = cdIcon.getImage();
								Image changeImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
								ImageIcon changeIcon = new ImageIcon(changeImg);
								cdLabel = new JLabel(changeIcon);
								if ((j == 5 && k == 2) || (j == 6 && k == 1) || (j == 6 && k == 2)) {
									if ((j == 5 && k == 2) || (j == 6 && k == 1))
										lbl1 = new JLabel(changeIcon);
									else if (j == 6 && k == 2)
										lbl2 = new JLabel(changeIcon);
									y++;
									continue;
								}
								else
									datePane.add(cdLabel);
							} else {
								datePane.add(new JLabel(" "));
							}
						}
						
						j += 2;
					}
					
					else {
						if (y == 1 && j == 0) {
							datePane.add(lbl1);
							y = 0;
						}
						else if (y == 2) {
							if (j == 0)
								datePane.add(lbl1);
							else if (j == 1) {
								datePane.add(lbl2);
								y = 0;
							}
						}
						else
							datePane.add(new JLabel(" "));
					}
				}
			}
			
		}
		
		cal.set(year, month-1, lastDate);
		int ldweek = cal.get(Calendar.DAY_OF_WEEK);
		for (int i = 0; i < (7 - ldweek); i++)
			datePane.add(new JLabel(" "));
		
		for (int i = 0; i < 7; i++)
			datePane.add(new JLabel(" "));
	}

	public static void main(String[] args) throws IOException, ParseException {
		new Mycalendar();
	}

}