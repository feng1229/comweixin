package friend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import main.Main;

public class CattingRecordsClass implements Serializable {// 通过文件追加方式放入记录
	private long id;
	private String message;// 消息面前加a代表自己发的 b代表好友
	private long time;//
	private long friendID;

	public CattingRecordsClass() {// 反序列化 结果是聊天记录的数组
	}

	public CattingRecordsClass(long id, String message, long time,long friendID) {// 反序列化 结果是聊天记录的数组
		this.id = id;
		this.message = message;
		this.time = time;
		this.friendID = friendID;
	}

//	public CattingRecordsClass(String path) {
	// getCattingRecords(path);
	// }

	public ArrayList<ArrayList<CattingRecordsClass>> getCattingRecordsData() {// 反序列化 结果是所有人聊天记录的数组
		ArrayList<ArrayList<CattingRecordsClass>> cattingRecordsData = new ArrayList<ArrayList<CattingRecordsClass>>();
		String path = "weinxinid\\" + Main.getID();
		String filePath;
		String[] s = (new File(path)).list();
		if (s != null) {
			for (int i = 0; i < s.length; i++) {
				filePath = path + "\\" + s[i];
				cattingRecordsData.add(getCattingRecords(filePath));
			}
		}
		return cattingRecordsData;
	}

	public ArrayList<CattingRecordsClass> getCattingRecords(String path) {// 反序列化 结果一个人是聊天记录
		ArrayList<CattingRecordsClass> cattingRecords = new ArrayList<CattingRecordsClass>();
		try {
			FileInputStream fi = new FileInputStream(new File(path));
			ObjectInputStream in = new ObjectInputStream(fi);
			try {
				while (fi.available() > 0)
					cattingRecords.add((CattingRecordsClass) in.readObject());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cattingRecords;
	}

	public static void setMessage(long sendid, String message, long time,long id) {// 序列化聊天记录 追加
		CattingRecordsClass cattingRecords = new CattingRecordsClass(sendid, message, time,id);
		String path = "weinxinid\\" + Main.getID();
		if (!new File(path).exists())
			new File(path).mkdir();
		File file = new File(path + "\\" + id + ".txt");
		try {
			boolean isexist = false;// 定义一个用来判断文件是否需要截掉头aced 0005的
			if (file.exists()) { // 文件是否存在
				isexist = true;
				FileOutputStream fo = new FileOutputStream(file, true);
				ObjectOutputStream oos = new ObjectOutputStream(fo);
				long pos = 0;
				if (isexist) {
					pos = fo.getChannel().position() - 4;// 追加的时候去掉头部aced 0005
					fo.getChannel().truncate(pos);
				}
				oos.writeObject(cattingRecords);// 进行序列化
				System.out.println("追加成功");
			} else {// 文件不存在
				file.createNewFile();
				FileOutputStream fo = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fo);
				oos.writeObject(cattingRecords);// 进行序列化
				oos.close();
				System.out.println("首次对象序列化成功！");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

	}

	public long getId() {
		return id;
	}
	public long getFriendId() {
		return friendID;
	}

	public long getTime() {
		return time;
	}

	public String getMessage() {
		return message;
	}

}
