/*
 *
 *左边菜单功能面板
 *
 */
package window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.Main;

public class FunctionPane extends JPanel {
	private final int width = 36;
	private final int height = 36;
	private JButton portrait = new JButton();
	public JButton word = new JButton();
	public JButton friends = new JButton();
	private JButton file = new JButton();
	private JButton set = new JButton();
	private JPanel p = new JPanel();
	private JPanel ps = new JPanel();// 设置面板

	public FunctionPane(MyFrame frame) {

		this.setBackground(new Color(55, 53, 53));
		this.setLayout(new BorderLayout());
		this.add("South", ps);
		this.add("Center", p);
		init(frame);
		// event(frame);

	}

	private void init(MyFrame frame) {

		ps.setPreferredSize(new Dimension(0, 60));// 设置底下面板的大小
		ps.setOpaque(false);// 透明
		p.setOpaque(false);
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		ImageIcon icon = new ImageIcon("image\\" + Main.getID() + ".png");
		icon.setImage(icon.getImage().getScaledInstance(36, 36, Image.SCALE_AREA_AVERAGING));
		portrait.setIcon(icon);
		word.setIcon(new ImageIcon("image\\massage1.png"));
		word.setDisabledIcon(new ImageIcon("image\\massage2.png"));// 禁用时显示的图片
		friends.setIcon(new ImageIcon("image\\word1.png"));
		friends.setDisabledIcon(new ImageIcon("image\\word2.png"));
		// file.setIcon(new ImageIcon("image\\"+Main.getID()+"\\"+Main.getID()+".txt"));

		portrait.setBounds(12, 20, width, height);
		portrait.setBorder(null);
		word.setBounds(12, 72, width, height);
		word.setBackground(getBackground());
		word.setBorder(null);// 去掉边框
		word.setEnabled(false);// 禁用状态
		friends.setBounds(12, 124, width, height);
		friends.setBackground(getBackground());
		friends.setBorder(null);
		file.setBounds(12, 176, width, height);
		file.setBackground(getBackground());
		file.setBorder(null);

		p.setLayout(null);
		p.add(portrait);
		p.add(word);
		p.add(friends);
		p.add(file);

		set.setPreferredSize(new Dimension(width, height));
		set.setBackground(getBackground());
		set.setBorder(null);
		ps.add(set);
		word.addActionListener(new ActionListener() {// 监听窗口改变

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.cutCentrePane();
				word.setEnabled(false);
				friends.setEnabled(true);// 启用状态
				repaint();
				// validate();
			}
		});
		friends.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.cutCentrePane();
				friends.setEnabled(false);
				word.setEnabled(true);// 启用状态
				repaint();
			}
		});
	}
}
