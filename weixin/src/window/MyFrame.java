/*
 *
 *΢��������
 *
 */
package window;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Main;

public class MyFrame extends JFrame {
	private JPanel blankPane = new JPanel();
	public static FunctionPane functionPane;
	private static int myFrameHigth = 620;
	private static int myFramewidth = 850;
	private static Point myFramePoint;
	private CardLayout card = new CardLayout();// �м俨Ƭ
	private JPanel centrePanel = new JPanel(card);
	private static CardLayout chatCard = new CardLayout();// �����Ƭ
	private static JPanel chatPane = new JPanel(chatCard);
	private static CardLayout rightCard = new CardLayout();// �ұ߿�Ƭ
	private static JPanel rightPane = new JPanel(rightCard);
	public static MessagePane messagePane;
	public static FriendsPane friendsPane;
	private static HashSet<Long> cathID = new HashSet<Long>();// �Ѿ��򿪵�������id��Ҳ�����ڿ�Ƭ��������

	public MyFrame() {
		// WindowFuntion.drag(this);
		this.setLocation(535, 225);
		this.setSize(850, 620);Toolkit.getDefaultToolkit().getScreenSize();
//		 this.setUndecorated(true);// ȥ���߿�
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// �˳�Ӧ�ó���
		this.setResizable(true);// ������С
		this.setMinimumSize(new Dimension(750, 520));// ������Сֵ
		this.setVisible(true);// ��ʾ
		// this.add(chatPane);
		this.setLayout(null);
		blankPane.setBackground(new Color(255, 255, 255));
		// blankPane.setPreferredSize(new Dimension(300, 50));
		myFramePoint = getContentPane().getLocationOnScreen();
		// chatPane.setBounds(300, 0, myFramewidth - 315, myFrameHigth - 40);
		chatPane.setBackground(new Color(248, 248, 248));
		chatPane.add("0", blankPane);
		init();
	}

	private void init() {
		// �м��������� 1 60 4 9
		friendsPane = new FriendsPane();
		messagePane = new MessagePane();
		functionPane = new FunctionPane(this);
		messagePane.setPreferredSize(new Dimension(240, 0));
		friendsPane.setPreferredSize(new Dimension(240, 0));
		chatPane.setPreferredSize(new Dimension( myFramewidth - 315,myFrameHigth - 40));
		add(functionPane);
		this.addComponentListener(new ComponentAdapter() {// �������ڸı�
			@Override
			public void componentResized(ComponentEvent e) {
				myFrameHigth = (int) getSize().getHeight();
				myFramewidth = (int) getSize().getWidth();
				myFramePoint = getContentPane().getLocationOnScreen();
				chatPane.setPreferredSize(new Dimension( myFramewidth - 315,myFrameHigth - 40));
				functionPane.setBounds(0, 0, 60, myFrameHigth - 40);
				centrePanel.setBounds(60, 0, 240, myFrameHigth - 40);
				rightPane.setBounds(300, 0, myFramewidth - 315, myFrameHigth - 40);
			}
		});
		functionPane.setBounds(0, 0, 60, myFrameHigth - 34);

		this.add(centrePanel);
		centrePanel.add(messagePane);
		centrePanel.add(friendsPane);
		this.add(rightPane);
		rightPane.add(chatPane);
		rightPane.add(blankPane);
		rightPane.setBounds(300, 0, myFramewidth - 315, myFrameHigth - 40);
		rightPane.setBackground(new Color(255, 255, 255));

		this.addWindowStateListener(new WindowStateListener() {// �������
			@Override
			public void windowStateChanged(WindowEvent e) {
				myFrameHigth = (int) getSize().getHeight();
				myFramewidth = (int) getSize().getWidth();
				myFramePoint = getContentPane().getLocationOnScreen();
				chatPane.setPreferredSize(new Dimension( myFramewidth - 315,myFrameHigth - 40));
				centrePanel.setBounds(60, 0, 240, myFrameHigth - 40);
				functionPane.setBounds(0, 0, 60, myFrameHigth - 40);
				rightPane.setBounds(300, 0, myFramewidth - 315, myFrameHigth - 40);
			}
		});
		this.addComponentListener(new ComponentAdapter() {// ���������϶�
			@Override
			public void componentMoved(ComponentEvent e) {
				myFramePoint = getContentPane().getLocationOnScreen();
			}
		});

//	CentrePane.setBounds(60, 0, 240, myFrameHigth);
		// CentrePane.add(messagePane);
		centrePanel.setBounds(60, 0, 240, myFrameHigth - 34);
	}

	public void cutCentrePane() {

		card.next(centrePanel);
		rightCard.next(rightPane);
		// messagePane.setBounds(60, 0, 240, myFrameHigth - 34);
	}

	public static int getMyFrameHigth() {
		return myFrameHigth;
	}

	public static int getMyFramewidth() {
		return myFramewidth;
	}

	public static void openChatPeanl(long id,String name) {// �������
		if (cathID.add(id))// �ж�������Ƿ��Ѵ�
		{
			ChatPane p = new ChatPane(id,name);
			p.repaint();
			chatPane.add(String.valueOf(id), p);
		}
		chatCard.show(chatPane, String.valueOf(id));
		chatPane.repaint();
		rightPane.repaint();
		Main.myFrame.repaint();
	}

	public static MessagePane getMessagePane() {
		return messagePane;
	}

	public static void updateChat(long id, String message, long time) {
		for (int i = 0; i < chatPane.getComponentCount(); i++) {
			if (chatPane.getComponent(i).getName().equals(String.valueOf(id))) {
				((ChatPane) chatPane.getComponent(i)).updateMessagePane(id, message, time);
				((ChatPane) chatPane.getComponent(i)).messagePane.repaint();
				return;
			}
		}
	}

	public static Point getMyFramePoint() {
		return myFramePoint;
	}

	public static void repaintFriendsPane() {
		friendsPane.repaint();
	}

	public static void repaintMessagePane() {
		messagePane.repaint();
	}

}
