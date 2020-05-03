/*
     好友面板布局
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
	private JPanel p = new JPanel();// 搜索栏
	private JPanel friendsPane = new JPanel();// 好友面板
	private JScrollPane fs = new JScrollPane(friendsPane);
	public static ArrayList<FriendClass> friendsData = FriendClass.getMyFriendsData();// 所有好友数据
	private HashMap<Character, Integer> letter = new HashMap<Character, Integer>();// 好友首字母相同的个数
	private HanyuPinyinOutputFormat defaultFormat;
	// private int letterSite = 0;// 字母面板个数

	public FriendsPane() {
		fs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		fs.setBorder(null);
		// fs.setBounds(0, 0, width, height);
		friendsPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		friendsPane.setBorder(null);
		friendsPane.setPreferredSize(new Dimension(0, friendsData.size() * 60));
		p.setPreferredSize(new Dimension(0, 60));// 搜索栏的大小

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
		int friendSite = 0;// 好友面板个数
		if (friend.getNote().equals(null) || friend.getNote().equals(""))// 判断有没有备注
			name = friend.getName();
		else
			name = friend.getNote();
		if (!name.equals("")) {// 计算各个好友面板的位置
			pinyin = PinyinHelper.toHanyuPinyinStringArray(name.charAt(0));
			if (pinyin != null)// 判断名字首字母
				oneLetter = Character.toUpperCase(pinyin[0].charAt(0));
			else
				oneLetter = Character.toUpperCase(name.charAt(0));
			if (Character.isLetter(oneLetter)) {
				if (letter.containsKey(oneLetter))
					letter.put(oneLetter, letter.get(oneLetter) + 1);
				else {
					letter.put(oneLetter, 2);
					for (int j = 65; j < (byte) oneLetter; j++) {// 判断这个字母前面有多少个面板项
						if (letter.containsKey((char) j))
							friendSite += letter.get((char) j);
					}
					setLetterPane(oneLetter, friendSite);
				}
			} else {
				oneLetter = '[';
				if (letter.containsKey(oneLetter))// 名字首位不是汉子对应的个数加1
					letter.put(oneLetter, letter.get(oneLetter) + 1);
				else {
					letter.put(oneLetter, 2);
					for (int j = 65; j < 91; j++) {// 判断这个字母前面有多少个面板项
						if (letter.containsKey((char) j))
							friendSite += letter.get((char) j);
					}
					setLetterPane('#', friendSite);
				}
			}
		} else {
			oneLetter = '[';
			if (letter.containsKey(oneLetter))// 名字首位不是汉子对应的个数加1
				letter.put(oneLetter, letter.get(oneLetter) + 1);
			else {
				letter.put(oneLetter, 2);
				for (int j = 65; j < 91; j++) {// 判断这个字母前面有多少个面板项
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
		button.setOpaque(false); // 透明
		button.setBorder(null);// 去掉边框
		imagel.setBounds(10, 10, 36, 36);
		namel.setBounds(60, 8, 170, 40);
		namel.setOpaque(false); // 透明
		namel.setFont(new Font("微软雅黑", Font.BOLD, 20));
		panel.add(imagel);
		panel.add(namel);
		panel.add(button);
		friendSite = 0;
		for (int j = 65; j < (byte) oneLetter; j++) {// 判断这个字母前面有多少个面板项
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
				for (int i = 0; i < MessagePane.cattingRecordsData.size(); i++)//判断信息面板有没本地记录
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
		addButton.setText("新的好友");
		addButton.setBorder(null);
		addButton.setFont(new Font("微软雅黑", Font.BOLD, 17));
		addButton.setBounds(60, 15, 120, 30);
		addButton.setMargin(new Insets(0, 0, 0, 0));// 改变文字边距v
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
		lettl.setFont(new Font("宋体", Font.BOLD, 17));
		panel.add(lettl);
		friendsPane.add(panel, friendSite);
		// letter.put(letterName, 0);
	}

}
