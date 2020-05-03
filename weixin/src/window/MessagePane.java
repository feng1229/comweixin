/*
消息面板布局
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import friend.CattingRecordsClass;
import main.Main;

public class MessagePane extends JPanel {

	public static ArrayList<ArrayList<CattingRecordsClass>> cattingRecordsData = (new CattingRecordsClass())
			.getCattingRecordsData();// 聊天页面的数据
	private JPanel p = new JPanel();// 搜索栏
	private JPanel friendsPane = new JPanel();// 好友面板
	private JScrollPane fs = new JScrollPane(friendsPane);
	public static AddFriendDialog addFriendDialog;

	public MessagePane() {

		fs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		fs.setBorder(null);

		friendsPane.setPreferredSize(new Dimension(0, 60 * Main.getMessageNumber()));
		friendsPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		friendsPane.setBorder(null);
		p.setPreferredSize(new Dimension(0, 60));// 搜索栏的大小

		// this.setPreferredSize(new Dimension(240, 0));
		this.setLayout(new BorderLayout());
		this.add(p, BorderLayout.NORTH);
		this.add(fs, BorderLayout.CENTER);
		this.setBackground(new Color(228, 223, 223));
		getFriendTable();
		updateUI();
		repaint();
		addFriend(p);
	}

	public void getFriendTable() {

		for (int i = 0; i < cattingRecordsData.size(); i++) {
			long id = cattingRecordsData.get(i).get(0).getFriendId();
			int mNumber = cattingRecordsData.get(i).size();// 信息条数
			long time = cattingRecordsData.get(i).get(mNumber - 1).getTime();
			String message = cattingRecordsData.get(i).get(mNumber - 1).getMessage();
			updateMessagePane(id, message, time, false);
		}
	}

	public void updateMessagePane(long id, String message, long time, boolean bool) {// 真为刚更新消息，假为读文件的消息记录
		JPanel pane = null;
		boolean b = true;
		for (int i = 0; i < friendsPane.getComponentCount(); i++)// 判断这个面板是否已经存在
			if (friendsPane.getComponent(i).getName().equals(Long.toString(id))) {
				pane = (JPanel) friendsPane.getComponent(i);
				b = false;
				break;
			}
		if (b) {
			pane = new JPanel();
			pane.setName(Long.toString(id));// 把这个消息对应的id赋给面板
		}else
			return;
	///	pane.removeAll();
		ImageIcon icon = null;
		if (new File("image\\" + id + ".png").exists())
			icon = new ImageIcon("image\\" + id + ".png");
		else
			icon = new ImageIcon("image\\weixin.png");
		icon.setImage(icon.getImage().getScaledInstance(36, 36, Image.SCALE_AREA_AVERAGING));
		String date = null;
		String name = null;// 好友微信名
		int site = 0;// 根据消息时间判断位置
		for (int j = 0; j < FriendsPane.friendsData.size(); j++) {// 找出对应微信号的头像和名字
			if (FriendsPane.friendsData.get(j).getId() == id) {
				if (FriendsPane.friendsData.get(j).getNote().equals(""))
					name = FriendsPane.friendsData.get(j).getName();
				else
					name = FriendsPane.friendsData.get(j).getNote();
				break;
			}
		}
		if (!bool) {
			if ((Main.getNetworkTime() / 1000 / 60 / 60 / 24 - time / 1000 / 60 / 60 / 24) == 0)// 判断是否当天
				date = new SimpleDateFormat("HH:mm").format(time);
			else
				date = new SimpleDateFormat("MM/dd").format(time);
			for (int j = 0; j < friendsPane.getComponentCount(); j++) // 根据消息时间判断位置
				if (cattingRecordsData.get(j).get(cattingRecordsData.get(j).size() - 1).getTime() < time) {
					System.out.println("site++");
					System.out.println(cattingRecordsData.get(j).get(cattingRecordsData.get(j).size() - 1).getTime());
					System.out.println(time);
					site++;
				}
		} else
			date = new SimpleDateFormat("HH:mm").format(time);
		JButton button = new JButton();
		JLabel imagel = new JLabel(icon);
		JLabel namel = new JLabel(name, JLabel.LEFT);
		JLabel messagel = new JLabel(message, JLabel.LEFT);
		JLabel datel = new JLabel(date, JLabel.LEFT);
		button.setBounds(0, 0, 240, 60);
		button.setBackground(getBackground());
		button.setOpaque(false); // 透明
		button.setBorder(null);// 去掉边框
		imagel.setBounds(10, 12, 36, 36);
		namel.setBounds(60, 9, 130, 20);
		namel.setOpaque(false); // 透明
		namel.setFont(new Font("微软雅黑", Font.BOLD, 15));
		messagel.setBounds(60, 32, 130, 16);
		messagel.setFont(new Font(null, 0, 13));
		messagel.setOpaque(false);
		datel.setBounds(190, 12, 58, 12);
		datel.setOpaque(false);
		pane.setLayout(null);
		pane.setPreferredSize(new Dimension(240, 60));
		pane.add(imagel);
		pane.add(namel);
		pane.add(messagel);
		pane.add(datel);
		pane.add(button);
		pane.repaint();
		friendsPane.add(pane, site);
		friendsPane.repaint();
		fs.repaint();
		String s=name;
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long q=id;
				Main.myFrame.openChatPeanl(id,s);
			}
		});
	}

	private void addFriend(JPanel addpane) {
		JButton addButton = new JButton();
		addButton.setText("添加好友");
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
				addFriendDialog = new AddFriendDialog();
				addFriendDialog.addFrieng();
			}
		});
	}

	public JPanel getFriendsPane() {
		return friendsPane;
	}

}
