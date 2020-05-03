/*
 *
 *��¼����
 *
 */
package window;

import main.Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import business.ConnectServer;
import business.ProcessMessages;

public class LoginPane extends JFrame {

	private JButton exitButton;
	private JButton loginButton;
	private JButton signinButton;
	private JButton retrievePasswordButton;
	private ImageIcon icon;
	private JLabel jLabel;
	private JLabel iconLabel;
	private JPanel jPanel = new JPanel();
	private JTextField jText;
	private JPasswordField jPassword;

	public LoginPane() {
//		printError("�˺Ż��������");

		jPanel.setBackground(new Color(239, 238, 238));
		jPanel.setLayout(null);
		init();
		WindowFuntion.drag(this);
		this.setLocation(850, 365);
		this.setSize(300, 450);
		this.getContentPane().add(jPanel);
		this.setUndecorated(true);
		this.setVisible(true);
		this.getContentPane().setBackground(Color.WHITE);// ������ɫ
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// �˳�Ӧ�ó���
//		jpanel.setOpaque(false);

	}

	public void init() {
//		jPanel.setOpaque(false);

		// �˳���ť
		icon = new ImageIcon("image\\tuichu.png");
		exitButton = new JButton(icon);
		exitButton.setBackground(jPanel.getBackground());
		exitButton.setBorder(null);// ȥ���߿�
		exitButton.setBounds(275, 0, 25, 25);
		// minBtn.setHorizontalAlignment(JButton.CENTER);//ͼ����뷽ʽ
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);// �رճ���
			}
		});
		WindowFuntion.colorChange(exitButton, jPanel, Color.red);
		jPanel.add(exitButton);

		// ����
		jLabel = new JLabel("΢��", JLabel.LEFT);
		jLabel.setFont(new Font("����", Font.PLAIN, 20));
		jLabel.setBounds(10, 0, 50, 30);
		jPanel.add(jLabel);

		// ͼƬ
		icon = new ImageIcon("image\\weixin.png");
		icon.setImage(icon.getImage().getScaledInstance(-1, 130, Image.SCALE_AREA_AVERAGING));
		iconLabel = new JLabel(icon);
		iconLabel.setBounds(85, 70, 130, 130);
		jPanel.add(iconLabel);

		// �˺�����
		jLabel = new JLabel("΢�źţ�", JLabel.RIGHT);
		jLabel.setBounds(45, 270, 60, 30);
		jLabel.setFont(new Font("����", Font.PLAIN, 15));
		jPanel.add(jLabel);
		jText = new JTextField();
		jText.setBounds(105, 270, 130, 25);
		jPanel.add(jText);

		jLabel = new JLabel("���룺", JLabel.RIGHT);
		jLabel.setBounds(45, 305, 60, 30);
		jLabel.setFont(new Font("����", Font.PLAIN, 15));
		jPanel.add(jLabel);
		jPassword = new JPasswordField();
		jPassword.setBounds(105, 305, 130, 25);
		jPanel.add(jPassword);

		// ��¼
		loginButton = new JButton("��¼");
		loginButton.setFont(new Font("����", Font.BOLD, 12));
		loginButton.setBounds(25, 365, 70, 27);
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginButton.setEnabled(false);
				try {
					if (ConnectServer.login(Long.parseLong(jText.getText()), new String(jPassword.getPassword()))) {
						dispose();
						Main.myFrame=new MyFrame();
					} else {
						loginButton.setEnabled(true);
						printError("�˺Ż��������");
					}
				} catch (NumberFormatException e1) {
					loginButton.setEnabled(true);
					printError("��������ȷ��΢�ź�");
					// e1.printStackTrace();
				} catch (UnknownHostException e1) {
					loginButton.setEnabled(true);
					printError("������û��");
					// e1.printStackTrace();
				} catch (IOException e1) {
					loginButton.setEnabled(true);
					printError("�������");
					// e1.printStackTrace();
				}
			}
		});
		jPanel.add(loginButton);
		// ע��
		signinButton = new JButton("ע��");
		signinButton.setFont(new Font("����", Font.BOLD, 12));
		signinButton.setBounds(115, 365, 70, 27);
		signinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SigninPane();
			}
		});
		jPanel.add(signinButton);
		// �һ�����
		retrievePasswordButton = new JButton("�һ�����");
//		retrievePasswordButton.setBorderPainted(false);//ȥ���߿�
		retrievePasswordButton.setMargin(new Insets(0, 0, 0, 0));// �ı����ֱ߾�
		retrievePasswordButton.setFont(new Font("����", Font.BOLD, 12));
		retrievePasswordButton.setBounds(205, 365, 70, 27);
		retrievePasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				JOptionPane.showMessageDialog(LoginPane.this, "�����ڴ�", "", JOptionPane.INFORMATION_MESSAGE);
//				dispose();// �ر��Ӵ�
			}
		});
		jPanel.add(retrievePasswordButton);
	}

	public void printError(String s) {
		JLabel label = new JLabel(s, JLabel.CENTER);
		label.setBounds(50, 230, 200, 30);
		label.setFont(new Font("�꿬��", Font.BOLD, 18));
		new Thread(() -> {
			jPanel.add(label);
//			jPanel.validate();// ��Ч
			repaint();// ˢ��
			try {
				Thread.sleep(2000);
				jPanel.remove(label);
				jPanel.repaint();// ˢ��
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}).start();
	}
}
