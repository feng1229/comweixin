/*
 *
 *��������
 *
 */
package friend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import main.Main;

public class FriendClass implements Serializable {
	private static final long serialVersionUID = -2071565876962058344L;
	private long id;// ΢�ź�
	private String name;// ΢������
	private String note;// ��ע��
	private byte sex;// �Ա� ��Ϊ1
	private String address;// ��ַ
	private String signature;// ǩ��

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

	public static ArrayList<FriendClass> getMyFriendsData() {// �����л��õ�����
		String path = "weixinid\\" + Main.getID() + ".txt";
		ArrayList<FriendClass> friendsData = new ArrayList<FriendClass>();
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(path)));
			friendsData = (ArrayList<FriendClass>) in.readObject();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return friendsData;
	}
	private static byte[] serialize(Object object) {// ���л�
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

	public static Object deserizlize(byte[] binaryByte) {
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

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setNaame(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
