package friend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import business.Mysql;

public class FriendClass implements Serializable {
	private static final long serialVersionUID = -2071565876962058344L;
	private long id;// 微信号
	private String name;// 微信名字
	private String note;// 备注名
	private byte sex;// 性别 男为1
	private String address;// 地址
	private String signature;// 签名

	public FriendClass() {

	}

	public FriendClass(long id, String name, String note, byte sex, String address, String signature) {
		this.id = id;
		this.name = name;
		this.note = note;
		this.sex = sex;
		this.address = address;
		this.signature = signature;
	}

	public static byte[] getLoginDateByte(long weixinid) {
		ArrayList<FriendClass> friendsData = new ArrayList<FriendClass>();
		ArrayList<Long> idN = new ArrayList<Long>();
		FileInputStream fiis;
		ByteArrayOutputStream baosImage=new ByteArrayOutputStream();
		DataOutputStream dos=new DataOutputStream(baosImage);
		File file;
		byte[] data = null;
		idN.add(weixinid);
		try {
			Statement statement = Mysql.con.createStatement();
			String sql = "select friend_id,note from w" + weixinid;
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				FriendClass friend = new FriendClass();
				friend.id = rs.getLong("friend_id");
				ResultSet rsid = Mysql.con.prepareStatement("select * from weixin_id where id=" + friend.id + ";")
						.executeQuery();
				rsid.next();
				friend.name = rsid.getString("name");
				friend.note = rs.getString("note");
				friend.sex = rsid.getByte("sex");
				friend.address = rsid.getString("address");
				friend.signature = rsid.getString("signature");
				friendsData.add(friend);
				idN.add(friend.id);
			}
			byte[] fdbyte = serialize(friendsData);
			try {
				
				dos.writeInt(fdbyte.length);
				dos.write(fdbyte);
				for (long id : idN) {// 上传所有好友的头像
					file = new File("C:" + File.separator + "image" + File.separator + id + ".png");
					if (!file.exists())
						file = new File("C:" + File.separator + "image" + File.separator + "weixin.png");
					fiis = new FileInputStream(file);
					dos.writeInt((int) file.length());
					dos.writeLong(id);
					byte[] b = new byte[1024];
					int len;
					while ((len = fiis.read(b)) != -1)
						dos.write(b, 0, len);System.out.println((int) file.length());
					fiis.close();
				}
				return baosImage.toByteArray();
			} catch (IOException e) {
				System.out.println("登录请求读取mysql数据录入出错");
				e.printStackTrace();
			} finally {
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println("登录请求读取mysql出错");
			e.printStackTrace();
		}
		return data; 
	}

	public static byte[] serialize(Object object) {// 序列化
		ObjectOutputStream objectOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		byte[] getByte = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(object);
			getByte = byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getByte;
	}

	private static Object deserizlize(byte[] binaryByte) {
		ObjectInputStream objectInputStream = null;
		ByteArrayInputStream byteArrayInputStream = null;
		Object readObject = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(binaryByte);
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			readObject = objectInputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return readObject;
	}

}