/*
     ������岼��
*/
package window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import friend.FriendClass;
import main.Main;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public class FriendsPane extends JPanel {
	private JPanel p = new JPanel();// ������
	private JPanel friendsPane = new JPanel();// �������
	private JScrollPane fs = new JScrollPane(friendsPane);
	public static ArrayList<FriendClass> friendsData = FriendClass.getMyFriendsData();// ���к�������
	private HashMap<Character, Integer> letter = new HashMap<Character, Integer>();// ��������ĸ��ͬ�ĸ���
	private HanyuPinyinOutputFormat defaultFormat;
	// private int letterSite = 0;// ��ĸ������

	public FriendsPane() {
		fs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		fs.setBorder(null);
		// fs.setBounds(0, 0, width, height);
		friendsPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		friendsPane.setBorder(null);
		friendsPane.setPreferredSize(new Dimension(0, friendsData.size() * 60));
		p.setPreferredSize(new Dimension(0, 60));// �������Ĵ�С

		this.setLayout(new BorderLayout());
		this.add(p, BorderLayout.NORTH);
		this.add(fs, BorderLayout.CENTER);
		this.setBackground(new Color(228, 223, 223));
		init();
		addFriend(p);
	}

	private void init() {
		defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		for (int i = 0; i < friendsData.size(); i++) {
			addFriend(friendsData.get(i));
		}
		friendsPane.repaint();
	}

	public void addFriend(FriendClass friend) {
		ImageIcon icon = null;
		if ((new File("image\\" + friend.getId() + ".png")).exists())
			icon = new ImageIcon("image\\" + friend.getId() + ".png");
		else
			icon = new ImageIcon("image\\weixin.png");
		icon.setImage(icon.getImage().getScaledInstance(36, 36, Image.SCALE_AREA_AVERAGING));
		String name;
		char oneLetter = 0;
		String[] pinyin = null;
		int friendSite = 0;// ����������
		if (friend.getNote().equals(null) || friend.getNote().equals(""))// �ж���û�б�ע
			name = friend.getName();
		else
			name = friend.getNote();
		if (!name.equals("")) {// ���������������λ��
			pinyin = PinyinHelper.toHanyuPinyinStringArray(name.charAt(0));
			if (pinyin != null)// �ж���������ĸ
				oneLetter = Character.toUpperCase(pinyin[0].charAt(0));
			else
				oneLetter = Character.toUpperCase(name.charAt(0));
			if (Character.isLetter(oneLetter)) {
				if (letter.containsKey(oneLetter))
					letter.put(oneLetter, letter.get(oneLetter) + 1);
				else {
					letter.put(oneLetter, 2);
					for (int j = 65; j < (byte) oneLetter; j++) {// �ж������ĸǰ���ж��ٸ������
						if (letter.containsKey((char) j))
							friendSite += letter.get((char) j);
					}
					setLetterPane(oneLetter, friendSite);
				}
			} else {
				oneLetter = '[';
				if (letter.containsKey(oneLetter))// ������λ���Ǻ��Ӷ�Ӧ�ĸ�����1
					letter.put(oneLetter, letter.get(oneLetter) + 1);
				else {
					letter.put(oneLetter, 2);
					for (int j = 65; j < 91; j++) {// �ж������ĸǰ���ж��ٸ������
						if (letter.containsKey((char) j))
							friendSite += letter.get((char) j);
					}
					setLetterPane('#', friendSite);
				}
			}
		} else {
			oneLetter = '[';
			if (letter.containsKey(oneLetter))// ������λ���Ǻ��Ӷ�Ӧ�ĸ�����1
				letter.put(oneLetter, letter.get(oneLetter) + 1);
			else {
				letter.put(oneLetter, 2);
				for (int j = 65; j < 91; j++) {// �ж������ĸǰ���ж��ٸ������
					if (letter.containsKey((char) j))
						friendSite += letter.get((char) j);
				}
				setLetterPane('#', friendSite);
			}
		}
		JPanel panel = new JPanel();
		JButton button = new JButton();
		JLabel imagel = new JLabel(icon);
		JLabel namel = new JLabel(friend.getName(), JLabel.LEFT);
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(240, 60));
		button.setBounds(0, 0, 240, 60);
		button.setBackground(getBackground());
		button.setOpaque(false); // ͸��
		button.setBorder(null);// ȥ���߿�
		imagel.setBounds(10, 10, 36, 36);
		namel.setBounds(60, 8, 170, 40);
		namel.setOpaque(false); // ͸��
		namel.setFont(new Font("΢���ź�", Font.BOLD, 20));
		panel.add(imagel);
		panel.add(namel);
		panel.add(button);
		friendSite = 0;
		for (int j = 65; j < (byte) oneLetter; j++) {// �ж������ĸǰ���ж��ٸ������
			if (letter.containsKey((char) j))
				friendSite += letter.get((char) j);
		}
		panel.repaint();
		friendsPane.add(panel, friendSite + 1);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = "";
				boolean bool = false;
				for (int i = 0; i < MessagePane.cattingRecordsData.size(); i++)//�ж���Ϣ�����û���ؼ�¼
					if (MessagePane.cattingRecordsData.get(i).get(0).getFriendId() == friend.getId())
						bool = true;
				if (!bool)
					MyFrame.messagePane.updateMessagePane(friend.getId(), message, Main.getNetworkTime(), true);
				Main.myFrame.cutCentrePane();
				Main.myFrame.openChatPeanl(friend.getId(), name);
				Main.myFrame.functionPane.friends.setEnabled(true);
				Main.myFrame.functionPane.word.setEnabled(false);
			}
		});
	}

//

	private void addFriend(JPanel addpane) {
		JButton addButton = new JButton();
		addButton.setText("�µĺ���");
		addButton.setBorder(null);
		addButton.setFont(new Font("΢���ź�", Font.BOLD, 17));
		addButton.setBounds(60, 15, 120, 30);
		addButton.setMargin(new Insets(0, 0, 0, 0));// �ı����ֱ߾�v
		addButton.setBackground(new Color(211, 212, 212));
		p.add(addButton);
		p.setLayout(null);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddFriendDialog().disposeFriend();
			}
		});
	}

	private void setLetterPane(char letterName, int friendSite) {
		JPanel panel = new JPanel();
		JLabel lettl = new JLabel(Character.toString(letterName), JLabel.LEFT);
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(240, 30));
		lettl.setBounds(10, 8, 20, 30);
		lettl.setFont(new Font("����", Font.BOLD, 17));
		panel.add(lettl);
		friendsPane.add(panel, friendSite);
		// letter.put(letterName, 0);
	}

}
