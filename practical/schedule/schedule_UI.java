package practical.schedule;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;

import practical.alarm.alarmPlay;

public class schedule_UI extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<schedule> Schedule = new ArrayList<schedule>();
	ArrayList<JLabel> name = new ArrayList<JLabel>();
	JPanel p;
	JPanel prevp;
	JPanel lp;
	
	JButton add;
	JButton set;
	JButton modify;
	JButton cancel;
	
	JLabel temp;
	
	JTextField tname;
	JTextField tyear;
	JTextField tmonth;
	JTextField tday;
	JTextField thour;
	JTextField tcontent;
	
	Checkbox box;
	
	schedule click;
	
	Toolkit kit = Toolkit.getDefaultToolkit();
	Dimension size = kit.getScreenSize();
	
	public schedule_UI() {
		setSize(500, 400);
		setLocation(size.width/2 - getSize().width/2, size.height/2 - getSize().height/2);
		setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		p = new JPanel();
		p.setLayout(null);
		p.setBounds(0, 0, 500, 400);
		add(p);
		
		lp = new JPanel();
		lp.setBounds(10, 10, 460, 280);
		lp.setBackground(Color.white);
		for(int i=0; i<name.size(); i++) {
			name.get(i).setBounds(10, 10+20*i, 460, 20);
			lp.add(name.get(i));
		}
		p.add(lp);
		
		add = new JButton("add");
		add.setBounds(200, 300, 70, 40);
		add.addMouseListener(new ButtonListener());
		p.add(add);
		
		set = new JButton("set");
		set.setBounds(100, 300, 80, 40);
		set.addMouseListener(new ButtonListener());
		modify = new JButton("modify");
		modify.setBounds(100, 300, 80, 40);
		modify.addMouseListener(new ButtonListener());
		cancel = new JButton("cancel");
		cancel.setBounds(300, 300, 80, 40);
		cancel.addMouseListener(new ButtonListener());

	}
	
	
	/*
	public static void main(String[] args) {
		new schedule_UI();
	}
	*/
	
	
	//일정으로 알람 만드는 함수입니다.
	public void makeAlarm(schedule s) {
		if(s.have_alarm) {
			alarmPlay.AddAlarm(s.year, s.month, s.day, s.hour, 33);	//min 입력이 없음..
			System.out.println(s.year + "/" + s.month + "/" + s.day + "/" + s.hour + "/" + "33");
		}
	}
	
	
	
	
	class ButtonListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			if(add == (JButton) e.getSource()) {
				prevp = p;
				remove(p);
				p = new JPanel();
				p.setLayout(null);
				p.setBounds(0, 0, 500, 400);
				
				lp = new JPanel();
				lp.setBounds(10, 10, 460, 280);
				lp.setBackground(Color.white);
				lp.setLayout(new GridLayout(6,2));
				
				temp = new JLabel("name");
				temp.setBounds(20, 20, 80, 20);
				lp.add(temp);
				temp = new JLabel("year");
				temp.setBounds(20, 60, 80, 20);
				lp.add(temp);
				temp = new JLabel("month");
				temp.setBounds(20, 100, 80, 20);
				lp.add(temp);
				temp = new JLabel("day");
				temp.setBounds(20, 140, 80, 20);
				lp.add(temp);
				temp = new JLabel("hour");
				temp.setBounds(20, 180, 80, 20);
				lp.add(temp);
				temp = new JLabel("content");
				temp.setBounds(20, 220, 80, 20);
				lp.add(temp);
				
				tname = new JTextField();
				tname.setBounds(100, 20, 340, 20);
				lp.add(tname);
				tyear = new JTextField();
				tyear.setBounds(100, 60, 340, 20);
				lp.add(tyear);
				tmonth = new JTextField();
				tmonth.setBounds(100, 100, 340, 20);
				lp.add(tmonth);
				tday = new JTextField();
				tday.setBounds(100, 140, 340, 20);
				lp.add(tday);
				thour = new JTextField();
				thour.setBounds(100, 180, 340, 20);
				lp.add(thour);
				tcontent = new JTextField();
				tcontent.setBounds(100, 220, 340, 20);		
				lp.add(tcontent);
				box = new Checkbox("have alarm?");
				box.setBounds(100, 250, 100, 20);
				lp.add(box);
				
				p.add(set);
				p.add(cancel);
				
				p.add(lp);
				add(p);
				repaint();
			}
			else if(cancel == (JButton) e.getSource()) {
				remove(p);
				p = prevp;
				add(prevp);
				repaint();
			}
			else if(set == (JButton) e.getSource()) {
				try {
					schedule a = new schedule();
					
					a.name = tname.getText();
					a.contents = tcontent.getText();
					if(tday.getText().isEmpty())
						a.day = 0;
					else
						a.day = Integer.parseInt(tday.getText());
					if(thour.getText().isEmpty())
						a.hour = 0;
					else
						a.hour = Integer.parseInt(thour.getText());
					if(tmonth.getText().isEmpty())
						a.month = 0;
					else
						a.month = Integer.parseInt(tmonth.getText());
					if(tyear.getText().isEmpty())
						a.year = 0;
					else
						a.year = Integer.parseInt(tyear.getText());
					
					
					if(box.getState()) {
						a.have_alarm = true;
						makeAlarm(a);
					}
					else
						a.have_alarm = false;
					Schedule.add(a);
					JLabel tl = new JLabel(a.name);
					tl.addMouseListener(new mouseListener());
					name.add(tl);
					
					remove(p);
					p = new JPanel();
					p.setLayout(null);
					p.setBounds(0, 0, 500, 400);
					add(p);
				
					lp = new JPanel();
					lp.setBounds(10, 10, 460, 280);
					lp.setBackground(Color.white);
					for(int i=0; i<name.size(); i++) {
						name.get(i).setBounds(10, 10+20*i, 460, 20);
						lp.add(name.get(i));
					}
					p.add(lp);
					p.add(add);
				
					repaint();
				}catch(NumberFormatException e1) {
					JOptionPane.showMessageDialog(set, "시간엔 숫자를 입력하세요", "잘못된 입력", JOptionPane.WARNING_MESSAGE);
				}
			}
			else if(modify == (JButton) e.getSource()) {
				try {
					click.name = tname.getText();
					click.contents = tcontent.getText();
					if(tday.getText().isEmpty())
						click.day = 0;
					else
						click.day = Integer.parseInt(tday.getText());
					if(thour.getText().isEmpty())
						click.hour = 0;
					else
						click.hour = Integer.parseInt(thour.getText());
					if(tmonth.getText().isEmpty())
						click.month = 0;
					else
						click.month = Integer.parseInt(tmonth.getText());
					if(tyear.getText().isEmpty())
						click.year = 0;
					else
						click.year = Integer.parseInt(tyear.getText());
						
						
					if(box.getState()) {
						click.have_alarm = true;
						makeAlarm(click);
					}
					else
						click.have_alarm = false;
					
						
					remove(p);
					p = new JPanel();
					p.setLayout(null);
					p.setBounds(0, 0, 500, 400);
					add(p);
					
					lp = new JPanel();
					lp.setBounds(10, 10, 460, 280);
					lp.setBackground(Color.white);
					for(int i=0; i<name.size(); i++) {
						name.get(i).setBounds(10, 10+20*i, 460, 20);
						lp.add(name.get(i));
					}
					p.add(lp);
					p.add(add);
					
					repaint();
				}catch(NumberFormatException e1) {
					JOptionPane.showMessageDialog(set, "시간엔 숫자를 입력하세요", "잘못된 입력", JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
		
	}
	
	
	class mouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				prevp = p;
				remove(p);
				p = new JPanel();
				p.setLayout(null);
				p.setBounds(0, 0, 500, 400);
				
				lp = new JPanel();
				lp.setBounds(10, 10, 460, 280);
				lp.setBackground(Color.white);
				lp.setLayout(new GridLayout(6,2));
				
				temp = new JLabel("name");
				temp.setBounds(20, 20, 80, 20);
				lp.add(temp);
				temp = new JLabel("year");
				temp.setBounds(20, 60, 80, 20);
				lp.add(temp);
				temp = new JLabel("month");
				temp.setBounds(20, 100, 80, 20);
				lp.add(temp);
				temp = new JLabel("day");
				temp.setBounds(20, 140, 80, 20);
				lp.add(temp);
				temp = new JLabel("hour");
				temp.setBounds(20, 180, 80, 20);
				lp.add(temp);
				temp = new JLabel("content");
				temp.setBounds(20, 220, 80, 20);
				lp.add(temp);
				
				tname = new JTextField();
				tname.setBounds(100, 20, 340, 20);
				lp.add(tname);
				tyear = new JTextField();
				tyear.setBounds(100, 60, 340, 20);
				lp.add(tyear);
				tmonth = new JTextField();
				tmonth.setBounds(100, 100, 340, 20);
				lp.add(tmonth);
				tday = new JTextField();
				tday.setBounds(100, 140, 340, 20);
				lp.add(tday);
				thour = new JTextField();
				thour.setBounds(100, 180, 340, 20);
				lp.add(thour);
				tcontent = new JTextField();
				tcontent.setBounds(100, 220, 340, 20);		
				lp.add(tcontent);
				box = new Checkbox("have alarm?");
				box.setBounds(100, 250, 100, 20);
				lp.add(box);

				
				for(int i=0; i<Schedule.size(); i++) {
					if(name.get(i) == (JLabel)e.getSource()) {
						click = Schedule.get(i);
						tname.setText(click.name);
						tcontent.setText(click.contents);
						tyear.setText(Integer.toString(click.year));
						tmonth.setText(Integer.toString(click.month));
						tday.setText(Integer.toString(click.day));
						thour.setText(Integer.toString(click.hour));
						if(click.have_alarm)
							box.setState(true);
					}
				}
				
				p.add(modify);
				p.add(cancel);
				
				p.add(lp);
				add(p);
				repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
		
	}
}