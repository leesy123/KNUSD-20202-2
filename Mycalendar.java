package swproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Mycalendar extends JFrame implements ActionListener{

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
	
	Calendar now;
	int year, month, date;
	
	public Mycalendar() {
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
		
		//Print days
		dayPrint(year, month);
		centerPane.add(datePane, "Center");
		
		add(centerPane, "Center");
		
		setSize(400, 300);
		setVisible(true);
		
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
				if (mm == 1) {
					yy--;
					mm = 12;
				} else
					mm--;
			}
			
			else if (eventBtn.equals(nextBtn)) { //Next month
				if (mm == 12) {
					yy++;
					mm = 1;
				} else
					mm++;
			}
			
			yearCombo.setSelectedItem(yy);
			monthCombo.setSelectedItem(mm);
		}
		
		else if (obj instanceof JComboBox) //ComboBox 이벤트 발생 시
			createDayStart();
	}
	
	public void createDayStart() {
		datePane.setVisible(false); //Panel hide
		datePane.removeAll(); //날짜 출력한 label 지우기
		dayPrint((int)yearCombo.getSelectedItem(), (int)monthCombo.getSelectedItem());
		datePane.setVisible(true); //Panel print again
	}
	
	public void dayPrint(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, 1); //출력할 첫 날의 object
		int week = cal.get(Calendar.DAY_OF_WEEK); //1일에 대한 요일
		int lastDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH); //그 달의 마지막 날
		
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
				if (outWeek == 1)
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
		}
	}
	
	public static void main(String[] args) {
		new Mycalendar();
	}
	
}
