/*
 *
 *注册界面
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
	private JRadioButton boy = new JRadioButton("男", true);
	private JRadioButton girl = new JRadioButton("女");

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
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// 退出该窗口

	}

	private void view() {

		// 退出按钮
		exitButton = new JButton(new ImageIcon("C:\\myweixin\\weixinid\\image\\tuichu.png"));
		exitButton.setBackground(jPanel.getBackground());
		exitButton.setBorder(null);// 去掉边框
		exitButton.setBounds(455, 0, 25, 25);
		// minBtn.setHorizontalAlignment(JButton.CENTER);//图标对齐方式
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();// 退出注册窗口
			}
		});
		WindowFuntion.colorChange(exitButton, jPanel, Color.red);
		jPanel.add(exitButton);

		lname("微信号：", 30, 60, 100, 30);
		jtext(jText1, 120, 60, 230, 26);
		symbol(353, 63, 10, 10);

		jLabel = new JLabel("1到11位数之间");
		jLabel.setBounds(370, 55, 90, 40);
		jPanel.add(jLabel);

		lname("密码：", 30, 110, 100, 30);
		jtext(jText2, 120, 110, 230, 26);
		symbol(353, 113, 10, 10);

		lname("微信名：", 30, 160, 100, 30);
		jtext(jText3, 120, 160, 230, 26);

		/////////////////////////////// 后期优化
		lname("我的地址：", 30, 210, 100, 30);
		jtext(jText4, 120, 210, 230, 26);

		lname("性别：", 30, 260, 100, 30);
		sexGroup.add(boy);
		sexGroup.add(girl);
		boy.setFont(new Font("宋体", Font.PLAIN, 20));
		girl.setFont(new Font("宋体", Font.PLAIN, 20));
		boy.setBounds(150, 250, 50, 50);
		girl.setBounds(250, 250, 50, 50);
		jPanel.add(boy);
		jPanel.add(girl);

		lname("我的签名：", 30, 310, 100, 30);
		signature.setLineWrap(true);// 自动换行
//		jTextSymbol.setWrapStyleWord(true);//断行不断字
		signature.setFont(new Font("标楷体", Font.PLAIN, 17));
		textPanel = new JScrollPane(signature);
		textPanel.setBounds(120, 310, 300, 150);
		jPanel.add(textPanel);

		singinButton = new JButton("注册");
		singinButton.setBounds(100, 500, 60, 40);
		jPanel.add(singinButton);
		singinButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				singinButton.setEnabled(false);
				try {
					if (jText1.getText().isEmpty() || jText2.getText().isEmpty()) {
						printError("请输入微信号和密码");
						singinButton.setEnabled(true);
					} else if (Long.parseLong(jText1.getText()) > 99999999999L
							|| Long.parseLong(jText1.getText()) < 0) {
						printError("微信号在11位之间");
					} else {

						if (ConnectServer.signin(Long.parseLong(jText1.getText()), jText2.getText(), jText3.getText(),
								boy.isSelected(),jText4.getText(),  signature.getText())) {
							JOptionPane.showMessageDialog(SigninPane.this, jText1.getText() + "\n" + jText2.getText(),
									"注册成功", JOptionPane.INFORMATION_MESSAGE);
							singinButton.setEnabled(true);
						} else {
							printError("你的微信号已经有主");
							singinButton.setEnabled(true);
						}
					}
				} catch (NumberFormatException e1) {
					printError("微信号请输入数字");
					singinButton.setEnabled(true);
				} catch (UnknownHostException e1) {
					singinButton.setEnabled(true);
					printError("服务器没了");
					// e1.printStackTrace();
				} catch (IOException e1) {
					singinButton.setEnabled(true);
					printError("网络出错");
					// e1.printStackTrace();
				}

			}
		});

		returnButton = new JButton("返回");
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
		jLabel.setFont(new Font("宋体", Font.PLAIN, 20));
		jPanel.add(jLabel);
	}

	private void jtext(JTextField jText, int x, int y, int length, int wide) {
		jText.setBounds(x, y, length, wide);
		jPanel.add(jText);
	}

	private void symbol(int x, int y, int length, int wide) {

		jLabel = new JLabel("*");
		jLabel.setBounds(x, y, length, wide);
		jLabel.setFont(new Font("宋体", Font.BOLD, 15));
		jLabel.setForeground(Color.RED);// 字体颜色
		jPanel.add(jLabel);
	}

	public void printError(String s) {
		JLabel label = new JLabel(s, JLabel.CENTER);
		label.setBounds(150, 20, 200, 30);
		label.setFont(new Font("标楷体", Font.BOLD, 18));
		new Thread(() -> {
			jPanel.add(label);
			jPanel.validate();// 生效
			jPanel.repaint();// 刷新
			try {
				Thread.sleep(2000);
				jPanel.remove(label);
				jPanel.validate();// 生效
				jPanel.repaint();// 刷新
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}).start();
	}
}
