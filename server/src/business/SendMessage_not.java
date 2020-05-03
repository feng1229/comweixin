package business;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SendMessage_not {// 把下线是收不到的信息发出去
	public static void sendMessage_not(long id) {
		try {
			Thread.sleep(1000);
			PreparedStatement psql;
			LinkUDP udp = LinkUDP.getAddress(id);
			psql = Mysql.con.prepareStatement("select * from message_not where receive_id= ?");
			psql.setLong(1, id);
			ResultSet rs = psql.executeQuery();
			while (rs.next()) {// 把下线是收不到的信息发出去
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(baos);
				dos.writeByte(rs.getByte("type"));
				long sendID = rs.getLong("send_id");
				long time = rs.getLong("time");
				String message = rs.getString("message");
				dos.writeLong(sendID);
				dos.writeUTF(message);
				dos.writeLong(time);
				if (message.equals("6")) {// 添加好友的信息 等待被添加方处理再删除
					byte[] friendData = Mysql.getFriend(sendID);
					dos.writeInt(friendData.length);
					dos.write(friendData);
					System.out.println(sendID + "添加" + id + "的消息发出去le");
				} else
					Mysql.deleteInMessage_not(sendID, id, time);// 删除文字消息
				dos.flush();
				byte[] b = baos.toByteArray();
				DatagramPacket dp = new DatagramPacket(b, b.length, udp.address, udp.udp_port);
				LinkUDP.socket.send(dp);
				dos.close();
			}
			psql.close();
		} catch (SQLException e) {
			System.out.println("发送没处理信息出错1");
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("发送没处理信息出错2");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("发送没处理信息出错3");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("发送没处理信息出错4");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
