package business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import friend.CattingRecordsClass;
import friend.FriendClass;
import main.Main;
import window.AddFriendDialog;
import window.FriendsPane;
import window.MessagePane;
import window.MyFrame;

public class ProcessMessages {
	private static String host = Main.getServerHost();
	private static int port = Main.getServerPort();
	public static InetAddress address;
	private static DatagramSocket socket;

	public ProcessMessages() {
		try {
			address = InetAddress.getByName(Main.getServerHost());
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private
	public static void sendMessage(long time, long id, String message) {// 时间戳 id 信息
		DatagramPacket dp;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeByte(1);// 代表发送的是文字消息
			dos.writeLong(Main.getID());
			dos.writeLong(id);
			dos.writeUTF(message);						
			dos.flush();
			byte[] b = baos.toByteArray();
			dp = new DatagramPacket(b, b.length, address, port);
			socket.send(dp);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void receiveMessage() {
		ByteArrayInputStream bais;
		DataInputStream dis;
		DataOutputStream dos;
		ByteArrayOutputStream baos;
		long time;
		long id;
		String message;
		while (true) {// 一直接受
			try {
				// sendUDP_port(Main.getID());
				byte[] b1 = new byte[1024];
				DatagramPacket dp1 = new DatagramPacket(b1, b1.length);
				socket.receive(dp1);
				bais = new ByteArrayInputStream(dp1.getData());
				dis = new DataInputStream(bais);
				byte type = dis.readByte();
				ByteArrayOutputStream baos1;
				DataOutputStream dos1;
				switch (type) {
				case 0:// 添加好友 判断有没有这个微信号
					if (dis.readBoolean()) {
						int len = dis.readInt();
						byte[] data = new byte[len];
						dis.read(data, 0, len);
						MessagePane.addFriendDialog.showFrieng(data);
					} else
						MessagePane.addFriendDialog.printError("没有这个微信号");
					dis.close();
					break;
				case 1:// 文字信息
					id = dis.readLong();
					message = dis.readUTF();
					time = dis.readLong();
					dis.close();
					MyFrame.getMessagePane().updateMessagePane(id, message, time, true);
					MyFrame.updateChat(id, message, time);
					CattingRecordsClass.setMessage(id, message, time,id);// 追加进文件

					baos1 = new ByteArrayOutputStream();
					dos1 = new DataOutputStream(baos1);

					dos1.writeByte(5);// 代表接收到了发送的消息
					dos1.writeLong(id);
					dos1.writeLong(Main.getID());
					dos1.writeLong(time);
					dos1.flush();
					dos1.close();
					byte[] b11 = baos1.toByteArray();
					DatagramPacket responsePacket = new DatagramPacket(b11, b11.length, address, port);
					socket.send(responsePacket);
					break;
				case 6:
					id = dis.readLong();
					if (id == 1L)
						MessagePane.addFriendDialog.printError("已经添加过了");
					else {
						int len = dis.readInt();
						System.out.println("144");
						byte[] data = new byte[len];
						dis.read(data);
						FriendClass friend = (FriendClass) (FriendClass) FriendClass.deserizlize(data);
						AddFriendDialog.disposeFriend.add(friend);
					}
					dis.close();
					break;
				case 7:
					id=dis.readLong();
					if(dis.readBoolean()) {
						int l=dis.readInt();
						byte[] b=new byte[l];
						dis.read(b);
						FriendClass friend=(FriendClass)FriendClass.deserizlize(b);
						FriendsPane.friendsData.add(friend);
						MyFrame.friendsPane.addFriend(friend);
						MyFrame.messagePane.getFriendsPane().repaint();;
						Main.myFrame.repaint();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void AddFrieng(long id, String dispose) {// 添加好友请求
		DatagramPacket dp;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeByte(6);// 代表好友请求
			dos.writeLong(Main.getID());
			dos.writeLong(id);
			dos.writeUTF(dispose);
			dos.flush();
			byte[] b = baos.toByteArray();
			dp = new DatagramPacket(b, b.length, address, port);
			socket.send(dp);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendID(long id) {
		DatagramPacket dp;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeByte(0);
			dos.writeLong(Main.getID());
			dos.writeLong(id);
			byte[] b = baos.toByteArray();
			dp = new DatagramPacket(b, b.length, address, port);
			socket.send(dp);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendUDP_port(long id) {
		DatagramPacket dp;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeByte(10);
			dos.writeLong(id);
			byte[] b = baos.toByteArray();
			dp = new DatagramPacket(b, b.length, address, port);
			socket.send(dp);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
