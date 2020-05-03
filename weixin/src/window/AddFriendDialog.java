package window;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import business.ProcessMessages;
import friend.FriendClass;
import main.Main;

public class AddFriendDialog extends JDialog {
	private int X;
	private int Y;
	private int width = 280;
	private int height = 450;
	private Container container;
	private JPanel disposeFriendPane;
	private JPanel addFriendPane = new JPanel();
	private long friendID;
	public static ArrayList<FriendClass> disposeFriend = new ArrayList<FriendClass>();
//	private static JPanel frienfPanel;

	public AddFriendDialog() {
		X = (int) MyFrame.getMyFramePoint().getX() + (MyFrame.getMyFramewidth() - width) / 2;
		Y = (int) MyFrame.getMyFramePoint().getY() + (MyFrame.getMyFrameHigth() - height) / 2;
		init();
	}

	private void init() {
		container = this.getContentPane();
		setLocation(X, Y);
		setSize(width, height);
		setLayout(null);
		this.setVisible(true);// ��ʾ
		this.setResizable(false);// ������С
	}

	public void addFrieng() {
		// frienfPanel = new JPanel();
		// frienfPanel.setBounds(0, 60, 280, 390);
		// frienfPanel.setLayout(null);
		this.setTitle("��Ӻ���");
		JTextField tf = new JTextField();
		JPanel tp = new JPanel();
		JButton addButton = new JButton("���");
		addButton.setBounds(190, 15, 40, 28);
		addButton.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		addButton.setMargin(new Insets(0, 0, 0, 0));// �ı����ֱ߾�v
		tp.setBounds(0, 0, 280, 60);
		tf.setBounds(20, 15, 150, 30);
		tp.setLayout(null);
		tp.add(tf);
		tp.add(addButton);
		container.add(tp);
		// container.add(frienfPanel);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					friendID = Long.parseLong(tf.getText());
					if (friendID > 99999999999L || friendID < 0||friendID==Main.getID())
						printError("��������ȷ��΢�ź�");
					else
						ProcessMessages.sendID(friendID);
				} catch (NumberFormatException e1) {
					printError("��������ȷ��΢�ź�");
				}
			}
		});
		// container.repaint();
	}

	public void disposeFriend() {
		disposeFriendPane = new JPanel();// �ײ����
		JScrollPane fs = new JScrollPane(disposeFriendPane);
		container.add(fs);
		fs.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		fs.setBorder(null);
		fs.setBounds(0, 0, 280, 540);//.setBounds(0, 0, 280, 60 * disposeFriend.size());
		disposeFriendPane.setPreferredSize(new Dimension(0, 60 * disposeFriend.size()));
		disposeFriendPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		disposeFriendPane.setBorder(null);
		this.setTitle("�µĺ���");
		for (int i = 0; i < disposeFriend.size(); i++) {
			JPanel friendPane = new JPanel();
			friendPane.setLayout(null);
			friendPane.setPreferredSize(new Dimension(270, 60));
			FriendClass friend = disposeFriend.get(i);
			JLabel label = new JLabel(friend.getName(), JLabel.LEFT);
			JButton addButton = new JButton("ͬ��");
			JButton refuseButton = new JButton("�ܽ�");
			// label.setHorizontalAlignment(JLabel.LEFT);
			label.setBounds(20, 20, 130, 30);
			label.setFont(new Font("�꿬��", Font.BOLD, 18));
			addButton.setBounds(170, 20, 35, 28);
			addButton.setFont(new Font("΢���ź�", Font.PLAIN, 14));
			addButton.setMargin(new Insets(0, 0, 0, 0));// �ı����ֱ߾�v
			refuseButton.setBounds(215, 20, 35, 28);
			refuseButton.setFont(new Font("΢���ź�", Font.PLAIN, 14));
			refuseButton.setMargin(new Insets(0, 0, 0, 0));// �ı����ֱ߾�v
			friendPane.add(label);
			friendPane.add(addButton);
			friendPane.add(refuseButton);
			disposeFriendPane.add(friendPane);
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ProcessMessages.AddFrieng(friend.getId(), "1");
					disposeFriendPane.remove(friendPane);
					disposeFriendPane.repaint();// ˢ��
					MyFrame.friendsPane.addFriend(friend);
					MyFrame.friendsPane.repaint();// ˢ��
					printError("�����");
				}
			});
			refuseButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ProcessMessages.AddFrieng(friend.getId(), "0");
					disposeFriendPane.remove(friendPane);
					disposeFriendPane.repaint();// ˢ��
					printError("�Ѿܾ�");
				}
			});

		}
		container.repaint();
	}

	public void showFrieng(byte[] data) {
		// ByteArrayInputStream bais = new ByteArrayInputStream(b);
		// DataInputStream dis = new DataInputStream(bais);
		FriendClass friend=(FriendClass)FriendClass.deserizlize(data);
		JButton addButton = new JButton("���");
		JLabel label = new JLabel(friend.getName(), JLabel.LEFT);
		// try {
		// String name = dis.readUTF();
		// label.setText(name);
		label.setBounds(20, 80, 180, 30);
		label.setFont(new Font("�꿬��", Font.BOLD, 18));
		addButton.setBounds(190, 80, 40, 28);
		addButton.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		addButton.setMargin(new Insets(0, 0, 0, 0));// �ı����ֱ߾�v
		// dis.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		addFriendPane.setLayout(null);
		addFriendPane.setBounds(0, 60, 280, 60);
		container.add(label);
		container.add(addButton);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ProcessMessages.AddFrieng(friendID, "6");
				container.remove(label);
				container.remove(addButton);
				container.repaint();// ˢ��
				printError("�����");
			}
		});
		container.repaint();
	}

	public void printError(String s) {
		JLabel label = new JLabel(s, JLabel.CENTER);
		label.setBounds(20, 60, 200, 30);
		label.setFont(new Font("�꿬��", Font.BOLD, 15));
		new Thread(() -> {
			container.add(label);
//			jPanel.validate();// ��Ч
			container.repaint();// ˢ��
			try {
				Thread.sleep(2000);
				container.remove(label);
				container.repaint();// ˢ��
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
}
