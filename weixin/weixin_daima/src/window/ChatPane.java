package window;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import business.ProcessMessages;

import java.util.ArrayList;
import friend.CattingRecordsClass;
import friend.FriendClass;
import main.Main;
import java.io.File;
import java.text.SimpleDateFormat;

public class ChatPane extends JPanel {
	private static final ArrayList<FriendClass> friendsData = FriendClass.getMyFriendsData();// 所有好友数据
	private CattingRecordsClass cattingRecords;
	private JPanel iconPanel = new JPanel();
	private JPanel panel = new JPanel();// 信息和输入的底层面板
	private JTextArea textInput = new JTextArea();// 输入框
	public JPanel messagePane = new JPanel();// 信息面板
	private JPanel inputPane = new JPanel();// 输入面板
	private JScrollPane textScroll = new JScrollPane(textInput);// 放输入框的滚动条
	private JScrollBar textBar = textScroll.getVerticalScrollBar();
	private JScrollPane messageScroll = new JScrollPane(messagePane);// 放信息面板的滚动条
	private JScrollBar messageBar = messageScroll.getVerticalScrollBar();// 滚动条拉倒底部
	private long id;
	private JPanel filePane = new JPanel();
	private JButton fileButton = new JButton();
	private JPanel sendPane = new JPanel();
	private JButton sendButton = new JButton("发送");
	private int heigth;// 聊天面板的高
	private String path;
	private ImageIcon icon;
	private ArrayList<Integer> byteNumber = new ArrayList<Integer>();// 记录信息的字节个数 用于计算信息面板的长度

	public ChatPane(long id) {
		this.setName(String.valueOf(id));
		setName(Long.toString(id));// 设置名字 用于有消息来时更新
		this.id = id;
		this.setBackground(new Color(248, 248, 248));
		this.setPreferredSize(new Dimension( MyFrame.getMyFramewidth() - 315,MyFrame.getMyFrameHigth() - 40));
		this.setLayout(new BorderLayout());
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(id);
				repaint();
				validate();
			}
		});
		init();
		messageLoad(id);
		textBar.setValue(textBar.getMaximum());// 滚动条拉倒底部
		repaint();
	}

	private void init() {
		//messageBar.setValue(messageBar.getMaximum());// 滚动条拉倒底部
		messageScroll.setPreferredSize(new Dimension(0, MyFrame.getMyFrameHigth() - 245));
		messageScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		messageScroll.setBorder(null);
		
		messagePane.setBackground(new Color(240, 240, 240));
		messagePane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		//	panel.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 315, MyFrame.getMyFrameHigth() - 245));
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		
		iconPanel.setPreferredSize(new Dimension(0, 45));
		iconPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		iconPanel.setOpaque(false);
		
		textScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	//	textScroll.setPreferredSize(new Dimension(0, 80));
		textScroll.setBorder(null);// 去边框
		
		textInput.setFont(new Font("微软雅黑", Font.BOLD, 18));
		textInput.setLineWrap(true);		
		
		filePane.setOpaque(false);// 透明
		filePane.setPreferredSize(new Dimension(0, 30));
		filePane.setLayout(new FlowLayout(FlowLayout.LEFT,20, 0));
		
		sendButton.setMargin(new Insets(0, 0, 0, 0));// 改变文字边距
		sendButton.setBackground(new Color(240, 240, 240));
		
		sendPane.setOpaque(false);// 透明
		sendPane.setPreferredSize(new Dimension(0, 30));
		sendPane.setLayout(new FlowLayout(FlowLayout.RIGHT,20, 0));
		sendPane.add(sendButton);
		
		inputPane.setLayout(new BorderLayout());
		inputPane.setBackground(new Color(255, 255, 255));
		inputPane.setPreferredSize(new Dimension(0, 140));
		
		inputPane.add(filePane, BorderLayout.NORTH);
		inputPane.add(textScroll, BorderLayout.CENTER);
		inputPane.add(sendPane, BorderLayout.SOUTH);
		
		panel.add(inputPane, BorderLayout.SOUTH);
		panel.add(messageScroll, BorderLayout.CENTER);
		
		this.add(iconPanel,BorderLayout.NORTH);
		this.add( panel, BorderLayout.CENTER);
	}

	private void messageLoad(long id) {// 加载班底聊天记录
		path = "weinxinid\\" + Main.getID() + "\\" + id + ".txt";
		File file = new File(path);
		if (!file.exists())// 如果文件不存在本方法结束
			return;
		ArrayList<CattingRecordsClass> lcr = new CattingRecordsClass().getCattingRecords(path);
		for (int i = 0; i < lcr.size(); i++) {
			String message =  lcr.get(i).getMessage();// 消息前面带判断 a为自己发的 b为好友发的
			long time = (long) lcr.get(i).getTime();
			updateMessagePane(lcr.get(i).getId(), message, time);
		}
		messagePane.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 340, heigth));
		textBar.setValue(textBar.getMaximum());// 滚动条拉倒底部
		messageScroll.repaint();
		messagePane.repaint();
		panel.repaint();
		repaint();
		this.setVisible(true);// 显示
	}

	public void updateMessagePane(long id, String message, long time) {// 设置聊天面板的显示

		JPanel timeP = new JPanel();// 显示时间面板
		JPanel messageP = new JPanel();
		JPanel iconP = new JPanel();
		iconP.setLayout(null);		
		messageP.setLayout(null);
		
		int messagePHeight;
		int txtn = message.getBytes().length;// 信息的字节数 用来计算信息框的大小 8个字节长为90 宽为30
		int txtWidth = MyFrame.getMyFramewidth() - 500;
		int txtHeigth = (int) (txtn * 11.25/ txtWidth ) + 1;// 信息框的行数
		if (txtHeigth == 1)
			txtWidth = (int) (txtn * 11.25 + 1);
		JLabel timeT = new JLabel(new SimpleDateFormat("MM-dd HH:mm").format(time), JLabel.CENTER);
		JButton iconB = new JButton();
		JTextArea messageT = new JTextArea();
		timeT.setFont(new Font("标楷体", Font.BOLD, 15));
		timeT.setBackground(new Color(230, 230, 230));
		 timeT.setOpaque(true);// 透明
		 timeT.setBorder(BorderFactory.createEtchedBorder());
		timeT.setBorder(null);// 无边框

		messageT.setPreferredSize(new Dimension(txtWidth, txtHeigth * 30));
		messageT.setFont(new Font("标楷体", Font.BOLD, 20));
		messageT.setBorder(null);// 无边框
		messageT.setText(message);
		messageT.setLineWrap(true);
		messageT.setEditable(false);
		timeP.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 315, 25));
		timeP.setLayout(new FlowLayout(FlowLayout.CENTER));

		if (id != Main.getID())// 判断是谁发的
		{
			icon = new ImageIcon("image\\" + id + ".png");			
			messageT.setBackground(new Color(147, 221, 144));
			iconB.setBounds(10, 0, 36, 36);
			messageP.setLayout(new FlowLayout(FlowLayout.RIGHT,15, 0));
		} else {
			icon = new ImageIcon("image\\" + Main.getID() + ".png");
			messageT.setBackground(new Color(255, 255, 255));
			messageP.setLayout(new FlowLayout(FlowLayout.LEFT,15, 0));
		}
		icon.setImage(icon.getImage().getScaledInstance(36, 36, Image.SCALE_AREA_AVERAGING));
		if (txtHeigth < 2) {
			messagePHeight = 40;
			messageP.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 315, messagePHeight));
			iconP.setPreferredSize(new Dimension(36,messagePHeight));
		} else {
			messagePHeight = txtHeigth * 30;
			messageP.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 315, messagePHeight));
			iconP.setPreferredSize(new Dimension(36,messagePHeight));
		}
		iconB.setIcon(icon);
		iconB.setBounds(0, 0, 36, 36);
		iconB.setBorder(null);// 无边框
		timeP.setOpaque(false);// 透明
		iconP.setOpaque(false);// 透明
		messageP.setOpaque(false);// 透明
		timeP.add(timeT);
		iconP.add(iconB);
		messageP.add(iconP);
		messageP.add(messageT);
		iconB.repaint();
		messageP.repaint();
		messagePane.repaint();
		messagePane.add(timeP);
		messagePane.add(messageP);
		heigth += 25;
		heigth += messagePHeight;
		byteNumber.add(txtn);
		this.addComponentListener(new ComponentAdapter() {// 监听窗口改变
			@Override
			public void componentResized(ComponentEvent e) {
				ChatPane.this.setPreferredSize(new Dimension( MyFrame.getMyFramewidth() - 315,MyFrame.getMyFrameHigth() - 40));
				messageScroll.setPreferredSize(new Dimension(0, MyFrame.getMyFrameHigth() - 245));
				int messagePHeight;
				int txtWidth = MyFrame.getMyFramewidth() - 500;
				int txtHeigth = (int) (txtn * 11.25/ txtWidth ) + 1;// 信息框的行数
				System.out.println("111sdf"+txtHeigth);
				if (txtHeigth == 1)
					txtWidth = (int) (txtn * 11.25 + 1);
				if (txtHeigth < 2) {
					messagePHeight = 40;
					messageP.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 315, messagePHeight));
				} else {
					messagePHeight = txtHeigth * 30;
					messageP.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 315, messagePHeight));
				}
				messageT.setPreferredSize(new Dimension(txtWidth, txtHeigth * 30));
				timeP.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 315, 25));
				messageT.repaint();
				messageP.repaint();
				messagePane.repaint();
			}
		});
	}

//	public 
	private boolean sendMessage(long id) {
		String message = textInput.getText();
		if (message == null) {
			JOptionPane.showMessageDialog(ChatPane.this, "不能发送空白消息", "", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		long time = Main.getNetworkTime();
		CattingRecordsClass.setMessage(Main.getID(), message, time, id);// 追加进文件
		ProcessMessages.sendMessage(time, id, message);// 发送
		this.updateMessagePane(Main.getID(), message, time);// 更新聊天面板
		MyFrame.messagePane.updateMessagePane(id, message, time, true);// 更新聊天内容
		messagePane.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 340, heigth));
		messagePane.repaint();
		return true;
	}

}
