package business;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import friend.FriendClass;

public class Mysql {
	// ����Connection����
	public static Connection con;

	public static void mysqllink() {
		// ����������
		String driver = "com.mysql.jdbc.Driver";
		// URLָ��Ҫ���ʵ����ݿ���mydata
		String url = "jdbc:mysql://localhost:3306/myweixin?useSSL=false";

		// MySQL����ʱ���û���
		String user = "root";
		// MySQL����ʱ������
		String password = "";
		// ������ѯ�����
		try {
			// ������������
			Class.forName(driver);
			// 1.getConnection()����������MySQL���ݿ⣡��
			con = DriverManager.getConnection(url, user, password);
			// if (!con.isClosed())
			// System.out.println("Succeeded connecting to the Database!");
			// 2.����statement���������ִ��SQL��䣡��
			// Statement statement = con.createStatement();
			// if (con != null)
			// con.close();
		} catch (ClassNotFoundException e) {
			// ���ݿ��������쳣����
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// ���ݿ�����ʧ���쳣����
			System.out.println("Sorry");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Sorry,can`t ");
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("���ݿ����ӳɹ�����");
		}
	}

	public static void creatTable(long id) {// ������Ӧid�ı�� ������к��ѺͶ�Ӧ�ı�ע��
		try {
			// ����statement���������ִ��SQL��䣡��
			Statement statement = con.createStatement();
			// Ҫִ�е�SQL���
			String sql = "CREATE TABLE IF NOT EXISTS w" + id + "( " // ����id����
					+ "friend_id bigint UNSIGNED not null, " + "note VARCHAR(50) null," + "time bigint not null,"
					+ "PRIMARY KEY ( friend_id ) " + ")ENGINE=InnoDB DEFAULT CHARSET=utf8";
			statement.executeLargeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			System.out.println("����������");
			e.printStackTrace();
		}
	}

	public static void addInMessage_not(byte type, long sendid, long receiveid, long time, String message) {
		PreparedStatement psql = null;
		try {
			psql = Mysql.con.prepareStatement(
					"insert into message_not (type,send_id,receive_id,time,message) " + "values(?,?,?,?,?)");
			psql.setByte(1, type);
			psql.setLong(2, sendid);
			psql.setLong(3, receiveid);
			psql.setLong(4, time);
			psql.setString(5, message);
			psql.executeUpdate(); // ִ�и���
			psql.close();
		} catch (SQLException e) {
			System.out.println("¼��message����");
			e.printStackTrace();
		}
	}

	public static boolean existAddFriend(long sendid, long receiveid) {// �����ڷ�����
		PreparedStatement psql = null;
		try {
			psql = Mysql.con
					.prepareStatement("select count(*) as ct from message_not where send_id = ? and receive_id = ?;");
			psql.setLong(1, sendid);
			psql.setLong(2, receiveid);
			ResultSet Judge = psql.executeQuery();
			Judge.next();
			int ct = Judge.getInt("ct");
			if (ct == 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("¼��message����");
			e.printStackTrace();
		} finally {
			try {
				psql.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static void deleteInMessage_not(long send_id, long receive_id, long time) {
		try {
			PreparedStatement deletesql;
			deletesql = Mysql.con
					.prepareStatement("delete from message_not where send_id= ? and receive_id= ? and time= ?");
			deletesql.setLong(1, send_id);
			deletesql.setLong(2, receive_id);
			deletesql.setLong(3, time);
			deletesql.executeUpdate();
			deletesql.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setUDPPort(int udp_port, long id) {// ����udp_port
		try {
			PreparedStatement psql;
			psql = con.prepareStatement("update weixin_id set udp_port = ? where id = ?");
			psql.setInt(1, udp_port);
			psql.setLong(2, id);
			psql.executeUpdate();
			psql.close();
		} catch (SQLException e) {
			System.out.println("����udp port����");
			e.printStackTrace();
		}
	}

	public static byte[] getFriend(long id) {// ���Ż�
		try {
			PreparedStatement psql;
			psql = con.prepareStatement("select * from weixin_id where id = ?");
			psql.setLong(1, id);
			ResultSet rs = psql.executeQuery();
			String name = null;
			FriendClass friend = null;
			while (rs.next())
				friend = new FriendClass(rs.getLong("id"), rs.getString("name"), "", rs.getByte("sex"),
						rs.getString("address"), rs.getString("signature"));
			psql.close();
			return FriendClass.serialize(friend);
		} catch (SQLException e) {
			System.out.println("����udp port����");
			e.printStackTrace();
		}
		return null;
	}
	public static void addFriend(long send_id,long add_id) {
		try {
			PreparedStatement psql;
			psql = con.prepareStatement("insert into w"+send_id+" (friend_id,note,time) values (?,?,?)");
			psql.setLong(1, add_id);
			psql.setString(2, "");
			psql.setLong(3, System.currentTimeMillis());
			psql.executeUpdate();
			psql = con.prepareStatement("insert into w"+add_id+" (friend_id,note,time) values (?,?,?)");
			psql.setLong(1, send_id);
			psql.setString(2, "");
			psql.setLong(3, System.currentTimeMillis());
			psql.executeUpdate();
			psql.close();
		} catch (SQLException e) {
			System.out.println("����udp port����");
			e.printStackTrace();
		}
	}

}
