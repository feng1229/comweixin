//package server;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//public class mysql {
//	
//	public static void main(String[] args) {
//		new mysql();
//	}
//	
//	public mysql() {
//		// ����Connection����
//		Connection con;
//		// ����������
//		String driver = "com.mysql.jdbc.Driver";
//		// URLָ��Ҫ���ʵ����ݿ���mydata
//		String url = "jdbc:mysql://localhost:3306/myweixin";
//
//		// MySQL����ʱ���û���
//		String user = "root";
//		// MySQL����ʱ������
//		String password = "";
//		// ������ѯ�����
//		try {
//			// ������������
//			Class.forName(driver);
//			// 1.getConnection()����������MySQL���ݿ⣡��
//			con = DriverManager.getConnection(url, user, password);
//			if (!con.isClosed())
//				System.out.println("Succeeded connecting to the Database!");
//			// 2.����statement���������ִ��SQL��䣡��
//			Statement statement = con.createStatement();
////			// Ҫִ�е�SQL���
//			String sql = "CREATE TABLE IF NOT EXISTS weixin_id( " //����id����
//			        + "id bigint UNSIGNED not null, "					
//					+ "password bigint null," 
//					+ "name VARCHAR(30) null," 
//					+ "sex TINYINT not null default 1, "//1Ϊ��
//					+ "address VARCHAR(50) null,"
//					+ "icon MEDIUMBLOB not null,"
//					+ "signature TINYTEXT null,"
//					+ "online TINYINT not null,"					
//					+ "time BIGINT not null,"					
//					+ "host char(25) ,"					
//					+ "tcp_port int,"					
//					+ "udp_port int,"									
//					+ "PRIMARY KEY ( id ) " 
//					+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
//
//			System.out.println(statement.executeLargeUpdate(sql));
//			String sqll = "CREATE TABLE IF NOT EXISTS message_not( " //
//					+ "id int UNSIGNED AUTO_INCREMENT, "					
//					+ "type tinyint null," 
//					+ "send_id bigint not null," 
//					+ "receive_id bigint not null," 
//					+ "time bigint not null," 
//					+ "message VARCHAR(500) not null,"									
//					+ "PRIMARY KEY ( id ) ," 
//					+ "INDEX ( receive_id ) " 
//					+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8;";
//			
//			System.out.println(statement.executeLargeUpdate(sqll));
//			// 3.ResultSet�࣬������Ż�ȡ�Ľ��������
////			ResultSet rs = statement.executeQuery(sql);
////			System.out.println("-----------------");
////			System.out.println("ִ�н��������ʾ:");
////			System.out.println("-----------------");
////			System.out.println("����" + "\t" + "ְ��");
////			System.out.println("-----------------");
////
////			String job = null;
////			String id = null;
////			while (rs.next()) {
////				// ��ȡstuname��������
////				job = rs.getString("id");
////				// ��ȡstuid��������
////				id = rs.getString("url");
////
////				// ������
////				System.out.println(id + "\t" + job);
////				System.out.println(Integer.parseInt("22222"));
////			}
////			rs.close();
//			con.close();
//		} catch (ClassNotFoundException e) {
//			// ���ݿ��������쳣����
//			System.out.println("Sorry,can`t find the Driver!");
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// ���ݿ�����ʧ���쳣����
//			System.out.println("Sorry");
//			e.printStackTrace();
//		} catch (Exception e) {
//			System.out.println("Sorry,can`t ");
//			// TODO: handle exception
//			e.printStackTrace();
//		} finally {
//			System.out.println("���ݿ����ݳɹ���ȡ����");
//		}
//	}
//
//}