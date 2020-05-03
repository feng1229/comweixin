package business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.Main;

public class LinkUDP {
	public static DatagramSocket socket;
	InetAddress address;
	int udp_port;

	public LinkUDP(InetAddress address, int udp_port) {
		this.address = address;
		this.udp_port = udp_port;
	}

	public LinkUDP() {
		new Thread(() -> init()).start();

	}

	public static void init() {
		try {
			// InetAddress serverAddress = InetAddress.getByName(Main.getServerHost());
			socket = new DatagramSocket(Main.getUdpPort());
			while (true) {// 一直接受
				try {
					byte[] b = new byte[1024];
					DatagramPacket dp = new DatagramPacket(b, b.length);
					socket.receive(dp);
					long time = System.currentTimeMillis();
					ByteArrayInputStream bais = new ByteArrayInputStream(dp.getData());
					DataInputStream dis = new DataInputStream(bais);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					DataOutputStream dos = new DataOutputStream(baos);
					byte[] sendData;
					LinkUDP udp;
					DatagramPacket sendPacket;
					byte type = dis.readByte();
					long sendID = dis.readLong();
					int clientPort = dp.getPort();
					InetAddress clientAddress = dp.getAddress();
					Mysql.setUDPPort(clientPort, sendID);// 设置发送者的udp_port
					switch (type) {
					case 0:
						long addID = dis.readLong();
						if (!LinkTCP.Signin(addID)) {// 判断id是否存在 存在返回假
							dos.writeByte(0);
							dos.writeBoolean(true);
							byte[] friendData = Mysql.getFriend(addID);
							dos.writeInt(friendData.length);
							dos.write(friendData);
							sendData = baos.toByteArray();
							dos.flush();
							sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
							socket.send(sendPacket);
							dos.close();
						} else {
							dos.writeByte(0);
							dos.writeBoolean(false);
							sendData = baos.toByteArray();
							sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
							socket.send(sendPacket);
							dos.close();
						}
						break;
					case 1:// 接收文字信息
						long receiveid = dis.readLong();
						String message = dis.readUTF();
						udp = getAddress(receiveid);
						dos.writeByte(1);
						dos.writeLong(sendID);
						dos.writeUTF(message);
						dos.writeLong(time);
						sendData = baos.toByteArray();
						sendPacket = new DatagramPacket(sendData, sendData.length, udp.address, udp.udp_port);
						socket.send(sendPacket);
						Mysql.addInMessage_not(type, sendID, receiveid, time, message);// 存进数据库 message_not
						dos.close();
						break;
					case 5:// 确认收到那条消息 然后删除未发送消息表的消息
						Mysql.deleteInMessage_not(sendID, dis.readLong(), dis.readLong());
						break;
					case 6:// 处理添加好友
						long addid = dis.readLong();
						String disposeFriend = dis.readUTF();
						udp = getAddress(addid);
						switch (disposeFriend) {
						case "1":
							dos.writeByte(7);
							dos.writeLong(sendID);
							dos.writeBoolean(true);
							byte[] addfriendData = Mysql.getFriend(sendID);
							dos.writeInt(addfriendData.length);
							dos.write(addfriendData);
							sendData = baos.toByteArray();
							sendPacket = new DatagramPacket(sendData, sendData.length, udp.address, udp.udp_port);
							socket.send(sendPacket);
							sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
							socket.send(sendPacket);
							Mysql.addFriend(sendID, addid);
							Mysql.deleteInMessage_not(addid, sendID, 0L);
							dos.close();
							break;
						case "0":
							dos.writeByte(7);
							dos.writeLong(sendID);
							dos.writeBoolean(false);
							sendData = baos.toByteArray();
							sendPacket = new DatagramPacket(sendData, sendData.length, udp.address, udp.udp_port);
							socket.send(sendPacket);
							sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
							socket.send(sendPacket);
							Mysql.deleteInMessage_not(addid, sendID, 0L);
							dos.close();
							break;
						default:
							dos.writeByte(6);
							if (Mysql.existAddFriend(sendID, addid)) {// 判断是否已添加过了
								dos.writeLong(sendID);
								byte[] friendData = Mysql.getFriend(sendID);
								dos.writeInt(friendData.length);
								dos.write(friendData);
							} else
								dos.writeLong(1L);
							sendData = baos.toByteArray();
							sendPacket = new DatagramPacket(sendData, sendData.length, udp.address, udp.udp_port);
							socket.send(sendPacket);
							Mysql.addInMessage_not(type, sendID, addid,0L, disposeFriend);// 存进数据库 message_not
							dos.close();
							break;
						}
						break;
					case 10:
						break;
					}
					dis.close();

				} catch (IOException e) {
					System.out.println("1udp出错");
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
			System.out.println("2udp出错");
			e.printStackTrace();
		}
	}

	public static LinkUDP getAddress(long id) {
		Statement statement;
		try {
			statement = Mysql.con.createStatement();
			String sql = "select host,udp_port,name from weixin_id where id=" + id;
			ResultSet rs = statement.executeQuery(sql);
			String receiveHost = null;
			int udp_port = 0;
			InetAddress address = null;
			while (rs.next()) {
				receiveHost = rs.getString("host");
				udp_port = rs.getInt("udp_port");
				// Blob addressBlob = rs.getBlob("host");
				// InputStream addressIn = addressBlob.getBinaryStream();
				// ByteArrayOutputStream out = new ByteArrayOutputStream();
				// byte[] buf = new byte[1024];
				// int len;
				// while ((len=addressIn.read(buf))!=-1) {
				// out.write(buf,0,len);
				// }
				System.out.println(receiveHost);
				address = InetAddress.getByName(receiveHost);
			}
			return new LinkUDP(address, udp_port);
		} catch (UnknownHostException e) {
			System.out.println("获取udp失败1");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("获取udp失败2");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
