package business;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import friend.FriendClass;
import main.Main;

public class LinkTCP {
	public LinkTCP() {
		new Thread(() -> {
			try {
				init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void init() throws Exception {
		ServerSocket server = null;
		try {
			server = new ServerSocket(Main.getTcpPort());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {
			System.out.println("a");
			Socket socket = server.accept();
		//	ExecutorService threadPool = Executors.newFixedThreadPool(100);
		//	Runnable runnable = () -> {
				try {
					System.out.println("b");
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					byte type = dis.readByte();
					long id = dis.readLong();
					byte[] data;
					switch (type) {
					case 1:// ��¼����
						if (login(id, dis.readUTF())) {
							System.out.println(id + "  ������");
							socket.shutdownInput();
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
							// data = FriendClass.getLoginDateByte(dis.readLong());
							dos.writeBoolean(true);
							// dos.writeInt(data.length);
							dos.write(FriendClass.getLoginDateByte(id));

							PreparedStatement psql;// �������ݿ����Ϣ �ͻ��˵�ַ��TCP�˿�
							// byte[] addressBytes = socket.getInetAddress().getAddress();
							// ByteArrayInputStream addressIn=new ByteArrayInputStream(addressBytes);
							String clientHost = socket.getInetAddress().getHostName();
							int clientPort = socket.getPort();
							try {
								psql = Mysql.con.prepareStatement(
										"update weixin_id set online= ?, host= ? ,tcp_port= ? where id = " + id);
								psql.setByte(1, (byte) 1);
								// psql.setBlob(2,addressIn);
								psql.setString(2, clientHost);
								psql.setInt(3, clientPort);
								psql.executeUpdate();
								psql.close();
							} catch (SQLException e) {
								System.out.println("��¼��������mysql����");
								e.printStackTrace();
							}
							dos.flush();
							socket.shutdownOutput();
							;
							dos.close();
							new Thread(() -> SendMessage_not.sendMessage_not(id)).start();
						} else {
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
							dos.writeBoolean(false);
							socket.shutdownOutput();
							dos.close();
						}
						break;
					case 2:// ע������
						if (Signin(id)) {// �ж�id�Ƿ���ע�� ��ע�᷵�ؼ�
							SigninLogging(id, dis);// ¼��ע����Ϣ
							DataOutputStream signindos = new DataOutputStream(socket.getOutputStream());
							signindos.writeBoolean(true);
							socket.shutdownOutput();
							signindos.close();
						} else {
							DataOutputStream signindos = new DataOutputStream(socket.getOutputStream());
							signindos.writeBoolean(false);
							socket.shutdownOutput();
							signindos.close();
						}
					}
					dis.close();
					if (socket != null) {
						socket.close();
					}
				} catch (IOException e) {
					System.out.println("��¼�����ѹ���ݰ�����");
					e.printStackTrace();
				}
		//	};
	//		threadPool.submit(runnable);
		}
	}

	private boolean login(long id, String password) {// �ж��˺������Ƿ���ȷ
		try {
			Statement statement = Mysql.con.createStatement();
			String sql = "select id,password from weixin_id";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				if (id == rs.getLong("id") && password.equals(rs.getString("password")))
					return true;
			}
			statement.close();
			return false;
		} catch (SQLException e) {
			System.out.println("�˺���������mysql����");
			e.printStackTrace();
		}
		return false;
	}

	public static boolean Signin(long id) {// �ж�id�Ƿ���ע�� ��ע�᷵�ؼ�
		try {
			Statement statement = Mysql.con.createStatement();
			String sql = "select id from weixin_id";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				if (id == rs.getLong("id"))
					return false;
			}
			statement.close();
			return true;
		} catch (SQLException e) {
			System.out.println("ע����������mysql����");
			e.printStackTrace();
		}
		return true;
	}

	private void SigninLogging(long id, DataInputStream dis) {// ¼��ע����Ϣ
		PreparedStatement psql = null;
		// Ԥ����������ݣ���������������--������
		try {
			psql = Mysql.con.prepareStatement(
					"insert into weixin_id (id,password,name,sex,address,signature,online,time,udp_port) "
							+ "values(?,?,?,?,?,?,?,?,?)");

			psql.setLong(1, id); // ���ò���1
			psql.setString(2, dis.readUTF());
			if (dis.readBoolean())
				psql.setString(3, dis.readUTF());
			else
				psql.setString(3, " ");
			if (dis.readBoolean())
				psql.setByte(4, (byte) 1);
			else
				psql.setByte(4, (byte) 0);
			if (dis.readBoolean())
				psql.setString(5, dis.readUTF());
			else
				psql.setString(5, " ");
			if (dis.readBoolean())
				psql.setString(6, dis.readUTF());
			else
				psql.setString(6, " ");
			psql.setByte(7, (byte) 1);
			psql.setLong(8, getNetworkTime());
			psql.setInt(9, dis.readInt());
			psql.executeUpdate(); // ִ�и���
			psql.close();
			Mysql.creatTable(id);// ������Ӧid�ı�� ������к��ѺͶ�Ӧ�ı�ע��
		} catch (SQLException e) {
			System.out.println("¼��ע����Ϣmysql����");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("¼��ע����Ϣmysql2����");
			e.printStackTrace();
		}
	}

	public static long getNetworkTime() {// ��ȡ����ʱ��

		try {
			URLConnection conn = new URL("http://www.baidu.com").openConnection();
			conn.connect();
			long dateL = conn.getDate();
			return dateL;
		} catch (MalformedURLException e) {
			System.out.println("��ȡ����ʱ�����");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("��ȡ����ʱ�����");
			e.printStackTrace();
		}
		return 0L;
	}
}
