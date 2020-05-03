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

public class CattingRecordsClass implements Serializable {// ͨ���ļ�׷�ӷ�ʽ�����¼
	private long id;
	private String message;// ��Ϣ��ǰ��a�����Լ����� b�������
	private long time;//
	private long friendID;

	public CattingRecordsClass() {// �����л� ����������¼������
	}

	public CattingRecordsClass(long id, String message, long time,long friendID) {// �����л� ����������¼������
		this.id = id;
		this.message = message;
		this.time = time;
		this.friendID = friendID;
	}

//	public CattingRecordsClass(String path) {
	// getCattingRecords(path);
	// }

	public ArrayList<ArrayList<CattingRecordsClass>> getCattingRecordsData() {// �����л� ����������������¼������
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

	public ArrayList<CattingRecordsClass> getCattingRecords(String path) {// �����л� ���һ�����������¼
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

	public static void setMessage(long sendid, String message, long time,long id) {// ���л������¼ ׷��
		CattingRecordsClass cattingRecords = new CattingRecordsClass(sendid, message, time,id);
		String path = "weinxinid\\" + Main.getID();
		if (!new File(path).exists())
			new File(path).mkdir();
		File file = new File(path + "\\" + id + ".txt");
		try {
			boolean isexist = false;// ����һ�������ж��ļ��Ƿ���Ҫ�ص�ͷaced 0005��
			if (file.exists()) { // �ļ��Ƿ����
				isexist = true;
				FileOutputStream fo = new FileOutputStream(file, true);
				ObjectOutputStream oos = new ObjectOutputStream(fo);
				long pos = 0;
				if (isexist) {
					pos = fo.getChannel().position() - 4;// ׷�ӵ�ʱ��ȥ��ͷ��aced 0005
					fo.getChannel().truncate(pos);
				}
				oos.writeObject(cattingRecords);// �������л�
				System.out.println("׷�ӳɹ�");
			} else {// �ļ�������
				file.createNewFile();
				FileOutputStream fo = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fo);
				oos.writeObject(cattingRecords);// �������л�
				oos.close();
				System.out.println("�״ζ������л��ɹ���");
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
