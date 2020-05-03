/*
 *
 *���ӷ�����
 *
 */
package business;

import main.Main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.stream.FileImageOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ConnectServer {
	private static String host = Main.getServerHost();
	private static int port = Main.getServerPort();

	// ��֤�˺������Ƿ���ȷ����ȷ������
	public static boolean login(long id, String password) throws UnknownHostException, IOException {
		Socket socket = new Socket(host, port);
		boolean bool;
		File file;

		FileOutputStream fileOut;
		String path = "weixinid";
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		out.writeByte(1);// ��ʾ�ǵ�¼����
		out.writeLong(id);
		out.writeUTF(password);
		out.flush();
		socket.shutdownOutput();
		DataInputStream in = new DataInputStream(socket.getInputStream());
		bool = in.readBoolean();
		if (bool) {
			new Thread(() -> ProcessMessages.receiveMessage()).start();// �ٿ�ʼ���߳̽���udp
			ProcessMessages.sendUDP_port(id);// ���ͱ�����udp_port
			if (!new File(path).exists())
				new File(path).mkdir();
			file = new File(path + File.separator + id + ".txt");
			fileOut = new FileOutputStream(file);
			int len = in.readInt();
			byte[] data = new byte[len];
			in.read(data);
			fileOut.write(data);
			fileOut.flush();
			fileOut.close();
			int l;
			while ((l = in.read()) != -1) {// ѭ������ͼƬ
				int icon_len = (l << 24) + (in.read() << 16) + (in.read() << 8) + (in.read() << 0);// ͼƬ���ݳ���
				byte[] icon_byte = new byte[1024];
				long icon_id = in.readLong();// ͼƬ��Ӧ��ad
				FileImageOutputStream iconOut = new FileImageOutputStream(
						new File("image" + File.separator + icon_id + ".png"));
				int datalen = 0;
				while (true) {
					datalen = in.read(icon_byte);
					iconOut.write(icon_byte, 0, datalen);
					icon_len -= datalen;
					if (icon_len < 1025)
						icon_byte = new byte[icon_len];
					if (icon_len == 0)
						break;
				}
				iconOut.flush();
				iconOut.close();
			}
			socket.shutdownInput();
			Main.setID(id);// ����̬������ֵ ����ȡ�����¼
		}
		if (socket != null)
			socket.close();
		return bool;
	}

	public static boolean signin(long id, String password, String name, boolean sex, String address, String signature)
			throws UnknownHostException, IOException {// ע��

		int nameLength = name.length();
		int addressLength = address.length();
		int signatureLength = signature.length();
		boolean bool;
		Socket socket = new Socket(host, port);
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		out.writeByte(2);// ��ʾ��ע������
		out.writeLong(id);
		out.writeUTF(password);
		if (nameLength == 0)
			out.writeBoolean(false);
		else {
			out.writeBoolean(true);
			out.writeUTF(name);
		}
		out.writeBoolean(sex);
		if (addressLength == 0)
			out.writeBoolean(false);
		else {
			out.writeBoolean(true);
			out.writeUTF(address);
		}
		if (signatureLength == 0)
			out.writeBoolean(false);
		else {
			out.writeBoolean(true);
			out.writeUTF(signature);
		}
		out.writeInt(new DatagramSocket(0).getLocalPort());
		socket.shutdownOutput();
		bool = in.readBoolean();
		in.close();
		socket.close();
		return bool;
	}

}
