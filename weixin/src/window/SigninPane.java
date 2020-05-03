/*
 *
 *ע�����
 *
 */
package window;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import business.ConnectServer;
import main.Main;

public class SigninPane extends JFrame {
	private static String host = Main.getServerHost();
	private static int port = Main.getServerPort();
	private JLabel jLabel;
	private JButton exitButton;
	private JButton singinButton;
	private JButton returnButton;
	private JScrollPane textPanel;
	private JTextField jText1 = new JTextField();
	private JTextField jText2 = new JTextField();
	private JTextField jText3 = new JTextField();
	private JTextField jText4 = new JTextField();
	private JTextArea signature = new JTextArea();
	private JPanel jPanel = new JPanel();
	private ButtonGroup sexGroup = new ButtonGroup();
	private JRadioButton boy = new JRadioButton("��", true);
	private JRadioButton girl = new JRadioButton("Ů");

	public SigninPane() {

//		jpanel.setSize(500,650);
		jPanel.setBackground(new Color(239, 238, 238));
		view();
		WindowFuntion.drag(this);
		jPanel.setLayout(null);
		this.setLocation(750, 230);
		this.setSize(480, 600);
		this.getContentPane().add(jPanel);
		this.setUndecorated(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// �˳��ô���

	}

	private void view() {

		// �˳���ť
		exitButton = new JButton(new ImageIcon("C:\\myweixin\\weixinid\\image\\tuichu.png"));
		exitButton.setBackground(jPanel.getBackground());
		exitButton.setBorder(null);// ȥ���߿�
		exitButton.setBounds(455, 0, 25, 25);
		// minBtn.setHorizontalAlignment(JButton.CENTER);//ͼ����뷽ʽ
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();// �˳�ע�ᴰ��
			}
		});
		WindowFuntion.colorChange(exitButton, jPanel, Color.red);
		jPanel.add(exitButton);

		lname("΢�źţ�", 30, 60, 100, 30);
		jtext(jText1, 120, 60, 230, 26);
		symbol(353, 63, 10, 10);

		jLabel = new JLabel("1��11λ��֮��");
		jLabel.setBounds(370, 55, 90, 40);
		jPanel.add(jLabel);

		lname("���룺", 30, 110, 100, 30);
		jtext(jText2, 120, 110, 230, 26);
		symbol(353, 113, 10, 10);

		lname("΢������", 30, 160, 100, 30);
		jtext(jText3, 120, 160, 230, 26);

		/////////////////////////////// �����Ż�
		lname("�ҵĵ�ַ��", 30, 210, 100, 30);
		jtext(jText4, 120, 210, 230, 26);

		lname("�Ա�", 30, 260, 100, 30);
		sexGroup.add(boy);
		sexGroup.add(girl);
		boy.setFont(new Font("����", Font.PLAIN, 20));
		girl.setFont(new Font("����", Font.PLAIN, 20));
		boy.setBounds(150, 250, 50, 50);
		girl.setBounds(250, 250, 50, 50);
		jPanel.add(boy);
		jPanel.add(girl);

		lname("�ҵ�ǩ����", 30, 310, 100, 30);
		signature.setLineWrap(true);// �Զ�����
//		jTextSymbol.setWrapStyleWord(true);//���в�����
		signature.setFont(new Font("�꿬��", Font.PLAIN, 17));
		textPanel = new JScrollPane(signature);
		textPanel.setBounds(120, 310, 300, 150);
		jPanel.add(textPanel);

		singinButton = new JButton("ע��");
		singinButton.setBounds(100, 500, 60, 40);
		jPanel.add(singinButton);
		singinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				singinButton.setEnabled(false);
				try {
					if (jText1.getText().isEmpty() || jText2.getText().isEmpty()) {
						printError("������΢�źź�����");
						singinButton.setEnabled(true);
					} else if (Long.parseLong(jText1.getText()) > 99999999999L
							|| Long.parseLong(jText1.getText()) < 0) {
						printError("΢�ź���11λ֮��");
					} else {

						if (ConnectServer.signin(Long.parseLong(jText1.getText()), jText2.getText(), jText3.getText(),
								boy.isSelected(),jText4.getText(),  signature.getText())) {
							JOptionPane.showMessageDialog(SigninPane.this, jText1.getText() + "\n" + jText2.getText(),
									"ע��ɹ�", JOptionPane.INFORMATION_MESSAGE);
							singinButton.setEnabled(true);
						} else {
							printError("���΢�ź��Ѿ�����");
							singinButton.setEnabled(true);
						}
					}
				} catch (NumberFormatException e1) {
					printError("΢�ź�����������");
					singinButton.setEnabled(true);
				} catch (UnknownHostException e1) {
					singinButton.setEnabled(true);
					printError("������û��");
					// e1.printStackTrace();
				} catch (IOException e1) {
					singinButton.setEnabled(true);
					printError("�������");
					// e1.printStackTrace();
				}

			}
		});

		returnButton = new JButton("����");
		returnButton.setBounds(300, 500, 60, 40);
		jPanel.add(returnButton);
		returnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

	}

	private void lname(String name, int x, int y, int length, int wide) {
		jLabel = new JLabel(name, JLabel.RIGHT);
		jLabel.setBounds(x, y, length, wide);
		jLabel.setFont(new Font("����", Font.PLAIN, 20));
		jPanel.add(jLabel);
	}

	private void jtext(JTextField jText, int x, int y, int length, int wide) {
		jText.setBounds(x, y, length, wide);
		jPanel.add(jText);
	}

	private void symbol(int x, int y, int length, int wide) {

		jLabel = new JLabel("*");
		jLabel.setBounds(x, y, length, wide);
		jLabel.setFont(new Font("����", Font.BOLD, 15));
		jLabel.setForeground(Color.RED);// ������ɫ
		jPanel.add(jLabel);
	}

	public void printError(String s) {
		JLabel label = new JLabel(s, JLabel.CENTER);
		label.setBounds(150, 20, 200, 30);
		label.setFont(new Font("�꿬��", Font.BOLD, 18));
		new Thread(() -> {
			jPanel.add(label);
			jPanel.validate();// ��Ч
			jPanel.repaint();// ˢ��
			try {
				Thread.sleep(2000);
				jPanel.remove(label);
				jPanel.validate();// ��Ч
				jPanel.repaint();// ˢ��
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}).start();
	}
}
