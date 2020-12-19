package practical.user;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;
import javax.swing.*;

import practical.memo.Hashing;

public class user_UI extends JFrame{
	public user U;
	File file = new File("userinfo.txt");
	Scanner s;

	Container contentpane = getContentPane();
	JPanel prevp;
	JPanel p;
	JButton input;
	JButton set;
	JButton cancel;
	JButton modify;
	JButton secret;
	JButton secretset;
	JLabel[] l = new JLabel[4];
	JLabel name;
	JLabel birth;
	JLabel lsecret;
	String[] lname = {"name", "year", "month", "day"};
	JTextField[] t = new JTextField[4];
	JTextField num;

	Toolkit kit = Toolkit.getDefaultToolkit();
	Dimension size = kit.getScreenSize();

	public user_UI() {
		setSize(500, 400);
		setLocation(size.width/2 - getSize().width/2, size.height/2 - getSize().height/2);

		if(file.exists()) {
			try {
				U = new user();
				s = new Scanner(new FileReader("userinfo.txt"));
				U.input(s.next(), s.nextInt(), s.nextInt(), s.nextInt());
				U.secretNum = s.next();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for(int i=0; i<4; i++) {
			l[i] = new JLabel(lname[i]);
			l[i].setBounds(50, 70+20*i, 100, 20);
			t[i] = new JTextField();
			t[i].setBounds(110, 70+20*i, 300, 20);
		}
		set = new JButton("set");
		set.setBounds(50, 200, 140, 80);
		set.addMouseListener(new ButtonListener());
		cancel = new JButton("cancel");
		cancel.setBounds(310, 200, 140, 80);
		cancel.addMouseListener(new ButtonListener());
		modify = new JButton("modify");
		modify.setBounds(50, 200, 140, 80);
		modify.addMouseListener(new ButtonListener());
		secret = new JButton("secret");
		secret.setBounds(310, 200, 140, 80);
		secret.addMouseListener(new ButtonListener());
		secretset = new JButton("set");
		secretset.setBounds(50, 200, 140, 80);
		secretset.addMouseListener(new ButtonListener());

		p = new JPanel();
		p.setBackground(Color.white);
		p.setLayout(null);

		if(U == null) {
			input = new JButton();
			input.setBounds(180, 140, 140, 80);
			input.setText("input user info");
			input.addMouseListener(new ButtonListener());

			p.add(input);
		}
		else {
			name = new JLabel("name:      "+U.name);
			birth = new JLabel("birthday: "+Integer.toString(U.year)+"."+Integer.toString(U.month)+"."
					+Integer.toString(U.day));
			name.setBounds(50, 100, 200, 20);
			birth.setBounds(50, 120, 200, 20);

			p.add(modify);
			p.add(secret);
			p.add(name);
			p.add(birth);
		}

		contentpane.add(p);
		setVisible(true);
		}

	/*
	public static void main(String[] args) {
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
	}*/





	class ButtonListener implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(input == (JButton) e.getSource() || modify == (JButton) e.getSource()) {
				prevp = p;
				remove(p);

				p = new JPanel();
				p.setBackground(Color.white);
				p.setLayout(null);
				for(int i=0; i<l.length; i++) {
					t[i].setText(null);
					p.add(l[i]);
					p.add(t[i]);
				}
				p.add(set);
				p.add(cancel);

				contentpane.add(p);
				setVisible(true);
			}
			else if(cancel == (JButton) e.getSource()) {
				remove(p);
				p = prevp;
				contentpane.add(prevp);
				repaint();
				setVisible(true);
			}
			else if(set == (JButton) e.getSource()) {
				try {
					U = new user();
					U.input(t[0].getText(), Integer.parseInt(t[1].getText()), 
							Integer.parseInt(t[2].getText()),Integer.parseInt(t[3].getText()));
					remove(p);
					p = new JPanel();
					p.setBackground(Color.white);
					p.setLayout(null);

					name = new JLabel("name:      "+U.name);
					birth = new JLabel("birthday: "+Integer.toString(U.year)+"."+Integer.toString(U.month)+"."
							+Integer.toString(U.day));
					name.setBounds(50, 100, 200, 20);
					birth.setBounds(50, 120, 200, 20);

					p.add(modify);
					p.add(secret);
					p.add(name);
					p.add(birth);

					contentpane.add(p);
					repaint();
					setVisible(true);

				}catch(NumberFormatException e1) {
					JOptionPane.showMessageDialog(set, "생년월일은 숫자를 입력하세요", "잘못된 입력", JOptionPane.WARNING_MESSAGE);
				}
			}
			else if(secret == (JButton) e.getSource()) {
				prevp = p;
				remove(p);

				p = new JPanel();
				p.setBackground(Color.white);
				p.setLayout(null);

				num = new JTextField();
				num.setBounds(200, 120, 150, 20);
				if(U.secretNum == null){
					lsecret = new JLabel("새 비밀번호 입력: ");
					lsecret.setBounds(50, 120, 120, 20);	
				}
				else {
					lsecret = new JLabel("기존 비밀번호 입력: ");
					lsecret.setBounds(50, 120, 120, 20);	
				}

				p.add(lsecret);
				p.add(num);
				p.add(secretset);
				p.add(cancel);

				contentpane.add(p);
				setVisible(true);
			}
			else if(secretset == (JButton) e.getSource()) {
				try {
					if(U.secretNum == null) {
						U.secretNum = Hashing.bytesToHex(Hashing.sha256(num.getText()));
						//해싱된 비밀번호만 저장되게 바꿨다.

						remove(p);
						p = new JPanel();
						p.setBackground(Color.white);
						p.setLayout(null);

						name = new JLabel("name:      "+U.name);
						birth = new JLabel("birthday: "+Integer.toString(U.year)+"."+Integer.toString(U.month)+"."
								+Integer.toString(U.day));
						name.setBounds(50, 100, 200, 20);
						birth.setBounds(50, 120, 200, 20);

						p.add(modify);
						p.add(secret);
						p.add(name);
						p.add(birth);

						contentpane.add(p);
						repaint();
						setVisible(true);
					}
					else {
						if(U.secretNum.equals(Hashing.bytesToHex(Hashing.sha256(num.getText())))) {
							//입력한 비밀번호의 해싱된 값을 비교하게 바꿨다. 
							remove(p);

							U.secretNum = null;
							p = new JPanel();
							p.setBackground(Color.white);
							p.setLayout(null);

							num = new JTextField();
							num.setBounds(200, 120, 150, 20);
							lsecret = new JLabel("새 비밀번호 입력: ");
							lsecret.setBounds(50, 120, 120, 20);	
							p.add(lsecret);
							p.add(num);
							p.add(secretset);
							p.add(cancel);

							contentpane.add(p);
							setVisible(true);
						}
						else {
							remove(p);

							JOptionPane.showMessageDialog(secretset, "비밀번호가 틀렸습니다.", "잘못된 입력", JOptionPane.WARNING_MESSAGE);
							p = new JPanel();
							p.setBackground(Color.white);
							p.setLayout(null);

							num = new JTextField();
							num.setBounds(200, 120, 150, 20);
							lsecret = new JLabel("기존 비밀번호 입력: ");
							lsecret.setBounds(50, 120, 120, 20);	
							p.add(lsecret);
							p.add(num);
							p.add(secretset);
							p.add(cancel);

							contentpane.add(p);
							setVisible(true);
						}
					}

				}catch(NumberFormatException e1) {
					JOptionPane.showMessageDialog(set, "생년월일은 숫자를 입력하세요", "잘못된 입력", JOptionPane.WARNING_MESSAGE);
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
}