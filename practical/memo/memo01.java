package practical.memo;

import practical.user.user;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class memo01 {
	//임시 비밀번호. 나중에 어디선가 미리 입력받아서 해싱된 값(hashedPass)만 저장됨.
	static String testPass = "1q2w3e4r";
	static String hashedPass = Hashing.bytesToHex(Hashing.sha256(testPass));
	public JFrame frame;
	private JTextField wrtTitle;

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
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
	}*/

	/**
	 * Create the application.
	 */
	public memo01() {
		initialize();
	}

	//디렉토리의 텍스트 파일 배열로 읽어오기 .txt를 때서 배열에 저장함.
	public static String[] getFile() {
		File dir = new File(".");
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File f, String name) {
		        return name.endsWith("txt");
		    }
		};
		String files[] = dir.list(filter);
		for(int i=0;i<files.length;i++) {
			files[i] = files[i].replaceAll(".txt", "");
		}
		return files;
	}
	
	//현재 파일 (삭제할때 사옹)
	File crntFile = null;
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 358);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		//=======메모 작성/수정
		JPanel wrtPanel = new JPanel();
		wrtPanel.setBounds(12, 10, 410, 299);
		frame.getContentPane().add(wrtPanel);
		wrtPanel.setLayout(null);
		
		JTextArea wrtArea = new JTextArea();
		wrtArea.setBounds(0, 20, 410, 198);
		wrtPanel.add(wrtArea);
		
		JButton btnSave = new JButton("save");
		btnSave.setBounds(52, 266, 97, 23);
		wrtPanel.add(btnSave);
		
		JButton btnQuit = new JButton("quit");
		btnQuit.setBounds(264, 266, 97, 23);
		wrtPanel.add(btnQuit);
		
		JLabel lblTitle = new JLabel("Title : ");
		lblTitle.setBounds(0, 0, 45, 17);
		wrtPanel.add(lblTitle);
		
		wrtTitle = new JTextField();
		wrtTitle.setBounds(41, -2, 369, 23);
		wrtPanel.add(wrtTitle);
		wrtTitle.setColumns(10);
		
		JCheckBox wrtLckCheck = new JCheckBox("Lock this memo");
		wrtLckCheck.setBounds(144, 237, 124, 23);
		wrtPanel.add(wrtLckCheck);
		
		JButton btnDelete = new JButton("delete");
		btnDelete.setBounds(155, 266, 97, 23);
		wrtPanel.add(btnDelete);

		//=======메모 목록
		JPanel listPanel = new JPanel();
		listPanel.setBounds(12, 10, 410, 299);
		frame.getContentPane().add(listPanel);
		listPanel.setLayout(null);
		
		JButton btnWrite = new JButton("Write");
		btnWrite.setBounds(49, 276, 97, 23);
		listPanel.add(btnWrite);
		
		JButton btnClose = new JButton("Close");
		btnClose.setBounds(265, 276, 97, 23);
		listPanel.add(btnClose);
		
		//메모 리스트에 파일 목록 넣기
		JList memoList = new JList();
		memoList.setModel(new AbstractListModel() {
			String[] values = getFile();
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		memoList.setBounds(0, 0, 410, 266);
		listPanel.add(memoList);
		listPanel.setVisible(true);
		wrtPanel.setVisible(false);
		
		
		//=====================================이벤트 리스너================================================
		
		//메모 목록 장 리스너
		//Write
		btnWrite.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnSave.setText("Write");
				wrtArea.setText("");
				wrtTitle.setText("");
				btnDelete.setVisible(false);
				listPanel.setVisible(false);
				wrtPanel.setVisible(true);
			}
			
		});
		
		//Close
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		//메모 목록
		
		memoList.addMouseListener(new MouseListener() {

	         @Override
	         public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount()==2) {   //더블클릭 때만 작동

					String fname = (String) memoList.getSelectedValue();
					Path fpath = Paths.get(fname + ".txt");
					crntFile = new File(fname + ".txt");		//삭제시 사용할 지금 경로(제목 수정후 삭제 에러 방지)
					try {
						BufferedReader reader = Files.newBufferedReader(fpath);
						
						//메모 수정 창에서 텍스트 띄움
						String mdfText = "";
						wrtTitle.setText(fname);		//제목 설정

						String temp = reader.readLine();	//맨 윗줄을 읽어 잠금 설정 확인
						if(temp.equals("1")) {	//잠금 메모 인경우(맷 윗줄이 1)
							String ans = (String) JOptionPane.showInputDialog(
									frame, "비밀번호를 입력하세요.", "Password", JOptionPane.PLAIN_MESSAGE);
							String hashedAns = Hashing.bytesToHex(Hashing.sha256(ans));
							
							if (!hashedAns.equals(user.secretNum)) {	//비밀 번호 틀림
								JOptionPane.showMessageDialog(
										frame, "잘못된 비밀번호 입니다.", "경고", JOptionPane.WARNING_MESSAGE);
								return;
							}
							else {		//비밀번호 맞음.
								wrtLckCheck.setSelected(true);
								temp = reader.readLine();
								try {
									mdfText += Encrypt.decrypt(temp);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							} 
						}
						else if(temp.equals("0")) {
							wrtLckCheck.setSelected(false);
							temp = reader.readLine();			//본문 시작
							while(temp != null) {
								mdfText += (temp + "\n");
								temp = reader.readLine();
							}
						}
						else{System.out.println("?");}
						
						wrtArea.setText(mdfText);		//본문 설정
						btnSave.setText("Modify");		//목록에서 작성창을 열면 작성버튼이 수정버튼이 됨.
						btnDelete.setVisible(true);		//목록에서 작성창을 열면 삭제 버튼이 활성화 됨.
						listPanel.setVisible(false);
						wrtPanel.setVisible(true);
						
					} catch (IOException err) {
						System.out.println("Could not read file");
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		
		
		//메모 작성/수정 창 리스너
		//Save
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String title = wrtTitle.getText();
				
				//제목 검사 (제목이 빈칸은 아닌지
				
				if(title.isEmpty()) {
					//경고창 띄우기 "제목을 작성해주세요"
					JOptionPane.showMessageDialog(
							frame, "제목을 작성해주세요.", "빈 제목", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else {
					String[] titles = getFile();
					for(int i=0;i<titles.length;i++) {
						if (title.equals(titles[i]) && btnSave.getText().equals("Write")) {
							//경고창 띄우기 "다른 제목을 작성해주세요"
							JOptionPane.showMessageDialog(
									frame, "다른 제목을 작성해주세요.", "중복된 제목", JOptionPane.WARNING_MESSAGE);
							return;
						}
					}
				}
				
				Path note = Paths.get(title + ".txt");
				String text = wrtArea.getText();
				String lockCheck = "0";
				
				//잠금 설정 하면 저장될 내용을 암호화함.
				if(wrtLckCheck.isSelected()) {
					
					//비밀번호 설정이 되어있지 않으면 잠금 저장 못함.
					if(user.secretNum == null) {
						JOptionPane.showMessageDialog(
								frame, "user_info에서 비밀번호를 설정해야 합니다.", "설정된 비밀번호 없음", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					
					lockCheck = "1";
					try {
						text = Encrypt.encrypt(text);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				// 파일 저장
				try {
					BufferedWriter writer = Files.newBufferedWriter(note, StandardCharsets.UTF_8);
					writer.write(lockCheck +"\n");	//잠금 설정 안하면 맨 윗줄에 0이 저장됨
					writer.write(text);
					writer.flush();
					writer.close();
				} catch (IOException err) {
					System.out.println("could not write text");
				}
				
				//메모 목록으로 돌아와야함.
				wrtArea.setText("");
				wrtTitle.setText("");
				memoList.setListData(getFile());	//새 메모를 써서 목록 갱신
				listPanel.setVisible(true);
				wrtPanel.setVisible(false);
			}
			
		});
		
		//Quit
		btnQuit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				wrtArea.setText("");
				wrtTitle.setText("");
				listPanel.setVisible(true);
				wrtPanel.setVisible(false);
			}
			
		});
		
		//Delete
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ans = JOptionPane.showConfirmDialog(frame, "Are you sure?", "메모 삭제", 
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (ans == 0) {	//삭제
					if(crntFile.exists()) {
						crntFile.delete();
						memoList.setListData(getFile());	//메모를 삭제후 목록 갱신
						listPanel.setVisible(true);
						wrtPanel.setVisible(false);
					}
					
				}
			}
			
		});
	}
}


//참고 자료
//https://www.youtube.com/channel/UCU2RDu6Vhlgu3YV0puxz66A
//https://www.youtube.com/watch?v=BlMttZXA2cc
//https://movefast.tistory.com/62
//https://codechacha.com/ko/java-list-files/
//https://m.blog.naver.com/PostView.nhn?blogId=simpolor&logNo=221238831700&categoryNo=59&proxyReferer=https:%2F%2Fwww.google.com%2F
//https://m.blog.naver.com/PostView.nhn?blogId=scyan2011&logNo=221687232608&proxyReferer=https:%2F%2Fwww.google.com%2F
//https://movefast.tistory.com/69
//http://cris.joongbu.ac.kr/course/java/api/javax/swing/JOptionPane.html
//https://javacrush.tistory.com/entry/JOptionPane-%EC%9E%85%EC%B6%9C%EB%A0%A5
//https://velog.io/@bang9dev/AES-256-%EC%97%90-%EA%B4%80%ED%95%98%EC%97%AC
//https://dololak.tistory.com/449
//https://okky.kr/article/673437