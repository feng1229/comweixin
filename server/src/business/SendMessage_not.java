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

public class SendMessage_not {// ���������ղ�������Ϣ����ȥ
	public static void sendMessage_not(long id) {
		try {
			Thread.sleep(1000);
			PreparedStatement psql;
			LinkUDP udp = LinkUDP.getAddress(id);
			psql = Mysql.con.prepareStatement("select * from message_not where receive_id= ?");
			psql.setLong(1, id);
			ResultSet rs = psql.executeQuery();
			while (rs.next()) {// ���������ղ�������Ϣ����ȥ
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(baos);
				dos.writeByte(rs.getByte("type"));
				long sendID = rs.getLong("send_id");
				long time = rs.getLong("time");
				String message = rs.getString("message");
				dos.writeLong(sendID);
				dos.writeUTF(message);
				dos.writeLong(time);
				if (message.equals("6")) {// ��Ӻ��ѵ���Ϣ �ȴ�����ӷ�������ɾ��
					byte[] friendData = Mysql.getFriend(sendID);
					dos.writeInt(friendData.length);
					dos.write(friendData);
					System.out.println(sendID + "���" + id + "����Ϣ����ȥle");
				} else
					Mysql.deleteInMessage_not(sendID, id, time);// ɾ��������Ϣ
				dos.flush();
				byte[] b = baos.toByteArray();
				DatagramPacket dp = new DatagramPacket(b, b.length, udp.address, udp.udp_port);
				LinkUDP.socket.send(dp);
				dos.close();
			}
			psql.close();
		} catch (SQLException e) {
			System.out.println("����û������Ϣ����1");
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("����û������Ϣ����2");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("����û������Ϣ����3");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("����û������Ϣ����4");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
