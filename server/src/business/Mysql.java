package business;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import friend.FriendClass;

public class Mysql {
	// 声明Connection对象
	public static Connection con;

	public static void mysqllink() {
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名mydata
		String url = "jdbc:mysql://localhost:3306/myweixin?useSSL=false";

		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "";
		// 遍历查询结果集
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库！！
			con = DriverManager.getConnection(url, user, password);
			// if (!con.isClosed())
			// System.out.println("Succeeded connecting to the Database!");
			// 2.创建statement类对象，用来执行SQL语句！！
			// Statement statement = con.createStatement();
			// if (con != null)
			// con.close();
		} catch (ClassNotFoundException e) {
			// 数据库驱动类异常处理
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			// 数据库连接失败异常处理
			System.out.println("Sorry");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Sorry,can`t ");
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.out.println("数据库链接成功！！");
		}
	}

	public static void creatTable(long id) {// 创建对应id的表格 存放所有好友和对应的备注名
		try {
			// 创建statement类对象，用来执行SQL语句！！
			Statement statement = con.createStatement();
			// 要执行的SQL语句
			String sql = "CREATE TABLE IF NOT EXISTS w" + id + "( " // 所有id数据
					+ "friend_id bigint UNSIGNED not null, " + "note VARCHAR(50) null," + "time bigint not null,"
					+ "PRIMARY KEY ( friend_id ) " + ")ENGINE=InnoDB DEFAULT CHARSET=utf8";
			statement.executeLargeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			System.out.println("创建表格出错");
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
			psql.executeUpdate(); // 执行更新
			psql.close();
		} catch (SQLException e) {
			System.out.println("录入message出错");
			e.printStackTrace();
		}
	}

	public static boolean existAddFriend(long sendid, long receiveid) {// 不存在返回真
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
			System.out.println("录入message出错");
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

	public static void setUDPPort(int udp_port, long id) {// 更新udp_port
		try {
			PreparedStatement psql;
			psql = con.prepareStatement("update weixin_id set udp_port = ? where id = ?");
			psql.setInt(1, udp_port);
			psql.setLong(2, id);
			psql.executeUpdate();
			psql.close();
		} catch (SQLException e) {
			System.out.println("更新udp port出错");
			e.printStackTrace();
		}
	}

	public static byte[] getFriend(long id) {// 代优化
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
			System.out.println("更新udp port出错");
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
			System.out.println("更新udp port出错");
			e.printStackTrace();
		}
	}

}
