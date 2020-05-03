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
	private static final ArrayList<FriendClass> friendsData = FriendClass.getMyFriendsData();// ���к�������
	private CattingRecordsClass cattingRecords;
	private JPanel iconPanel = new JPanel();
	private JPanel panel = new JPanel();// ��Ϣ������ĵײ����
	private JTextArea textInput = new JTextArea();// �����
	public JPanel messagePane = new JPanel();// ��Ϣ���
	private JPanel inputPane = new JPanel();// �������
	private JScrollPane textScroll = new JScrollPane(textInput);// �������Ĺ�����
	private JScrollBar textBar = textScroll.getVerticalScrollBar();
	private JScrollPane messageScroll = new JScrollPane(messagePane);// ����Ϣ���Ĺ�����
	private JScrollBar messageBar = messageScroll.getVerticalScrollBar();// �����������ײ�
	private long id;
	private JPanel filePane = new JPanel();
	private JButton fileButton = new JButton();
	private JPanel sendPane = new JPanel();
	private JButton sendButton = new JButton("����");
	private int heigth;// �������ĸ�
	private String path;
	private ImageIcon icon;
	private ArrayList<Integer> byteNumber = new ArrayList<Integer>();// ��¼��Ϣ���ֽڸ��� ���ڼ�����Ϣ���ĳ���

	public ChatPane(long id) {
		this.setName(String.valueOf(id));
		setName(Long.toString(id));// �������� ��������Ϣ��ʱ����
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
		textBar.setValue(textBar.getMaximum());// �����������ײ�
		repaint();
	}

	private void init() {
		//messageBar.setValue(messageBar.getMaximum());// �����������ײ�
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
		textScroll.setBorder(null);// ȥ�߿�
		
		textInput.setFont(new Font("΢���ź�", Font.BOLD, 18));
		textInput.setLineWrap(true);		
		
		filePane.setOpaque(false);// ͸��
		filePane.setPreferredSize(new Dimension(0, 30));
		filePane.setLayout(new FlowLayout(FlowLayout.LEFT,20, 0));
		
		sendButton.setMargin(new Insets(0, 0, 0, 0));// �ı����ֱ߾�
		sendButton.setBackground(new Color(240, 240, 240));
		
		sendPane.setOpaque(false);// ͸��
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

	private void messageLoad(long id) {// ���ذ�������¼
		path = "weinxinid\\" + Main.getID() + "\\" + id + ".txt";
		File file = new File(path);
		if (!file.exists())// ����ļ������ڱ���������
			return;
		ArrayList<CattingRecordsClass> lcr = new CattingRecordsClass().getCattingRecords(path);
		for (int i = 0; i < lcr.size(); i++) {
			String message =  lcr.get(i).getMessage();// ��Ϣǰ����ж� aΪ�Լ����� bΪ���ѷ���
			long time = (long) lcr.get(i).getTime();
			updateMessagePane(lcr.get(i).getId(), message, time);
		}
		messagePane.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 340, heigth));
		textBar.setValue(textBar.getMaximum());// �����������ײ�
		messageScroll.repaint();
		messagePane.repaint();
		panel.repaint();
		repaint();
		this.setVisible(true);// ��ʾ
	}

	public void updateMessagePane(long id, String message, long time) {// ��������������ʾ

		JPanel timeP = new JPanel();// ��ʾʱ�����
		JPanel messageP = new JPanel();
		JPanel iconP = new JPanel();
		iconP.setLayout(null);		
		messageP.setLayout(null);
		
		int messagePHeight;
		int txtn = message.getBytes().length;// ��Ϣ���ֽ��� ����������Ϣ��Ĵ�С 8���ֽڳ�Ϊ90 ��Ϊ30
		int txtWidth = MyFrame.getMyFramewidth() - 500;
		int txtHeigth = (int) (txtn * 11.25/ txtWidth ) + 1;// ��Ϣ�������
		if (txtHeigth == 1)
			txtWidth = (int) (txtn * 11.25 + 1);
		JLabel timeT = new JLabel(new SimpleDateFormat("MM-dd HH:mm").format(time), JLabel.CENTER);
		JButton iconB = new JButton();
		JTextArea messageT = new JTextArea();
		timeT.setFont(new Font("�꿬��", Font.BOLD, 15));
		timeT.setBackground(new Color(230, 230, 230));
		 timeT.setOpaque(true);// ͸��
		 timeT.setBorder(BorderFactory.createEtchedBorder());
		timeT.setBorder(null);// �ޱ߿�

		messageT.setPreferredSize(new Dimension(txtWidth, txtHeigth * 30));
		messageT.setFont(new Font("�꿬��", Font.BOLD, 20));
		messageT.setBorder(null);// �ޱ߿�
		messageT.setText(message);
		messageT.setLineWrap(true);
		messageT.setEditable(false);
		timeP.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 315, 25));
		timeP.setLayout(new FlowLayout(FlowLayout.CENTER));

		if (id != Main.getID())// �ж���˭����
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
		iconB.setBorder(null);// �ޱ߿�
		timeP.setOpaque(false);// ͸��
		iconP.setOpaque(false);// ͸��
		messageP.setOpaque(false);// ͸��
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
		this.addComponentListener(new ComponentAdapter() {// �������ڸı�
			@Override
			public void componentResized(ComponentEvent e) {
				ChatPane.this.setPreferredSize(new Dimension( MyFrame.getMyFramewidth() - 315,MyFrame.getMyFrameHigth() - 40));
				messageScroll.setPreferredSize(new Dimension(0, MyFrame.getMyFrameHigth() - 245));
				int messagePHeight;
				int txtWidth = MyFrame.getMyFramewidth() - 500;
				int txtHeigth = (int) (txtn * 11.25/ txtWidth ) + 1;// ��Ϣ�������
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
			JOptionPane.showMessageDialog(ChatPane.this, "���ܷ��Ϳհ���Ϣ", "", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		long time = Main.getNetworkTime();
		CattingRecordsClass.setMessage(Main.getID(), message, time, id);// ׷�ӽ��ļ�
		ProcessMessages.sendMessage(time, id, message);// ����
		this.updateMessagePane(Main.getID(), message, time);// �����������
		MyFrame.messagePane.updateMessagePane(id, message, time, true);// ������������
		messagePane.setPreferredSize(new Dimension(MyFrame.getMyFramewidth() - 340, heigth));
		messagePane.repaint();
		return true;
	}

}
