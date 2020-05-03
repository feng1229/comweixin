package main;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import business.ProcessMessages;
import window.LoginPane;
import window.MyFrame;

public class Main {
	private static long ID = 0;// 微信号
	private static int messageNumber = 0;// 本地保存消息的好友数量
	private static int friendNumber = 0;// 好友数量
	private static final String SERVER_HOST = "47.113.84.193";
	private static final int SERVER_PORT = 23;
    public static MyFrame myFrame;

	public static void main(String[] args) {
		new ProcessMessages();
		System.out.println(getNetworkTime());
		new LoginPane();
//		new MyFrame();
	}

	public static int getMessageNumber() {
		return messageNumber;
	}

	public static void setMessageNumber(int messageNumber) {
		Main.messageNumber = messageNumber;
	}

	public static int getFriendNumber() {
		return friendNumber;
	}

	public static void setFriendNumber(int friendNumber) {
		Main.friendNumber = friendNumber;
	}

	public static long getID() {
		return ID;
	}

	public static void setID(long iD) {
		ID = iD;
	}

	public static String getServerHost() {
		return SERVER_HOST;
	}

	public static int getServerPort() {
		return SERVER_PORT;
	}

	public static long getNetworkTime() {// 获取网络时间

		try {
			URLConnection conn = new URL("http://www.baidu.com").openConnection();
			conn.connect();
			long dateL = conn.getDate();
			return dateL;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;
	}

}
