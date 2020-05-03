package main;

import business.LinkTCP;
import business.LinkUDP;
import business.Mysql;

public class Main {
	private static final String SERVER_HOST = "47.113.84.193";
	private static final int TCP_PORT = 23;
	private static final int UDP_PORT = 23;
	

	public static void main(String[] args) {
		Mysql.mysqllink();
		new LinkUDP();
		new LinkTCP();
	}
	
	public static String getServerHost() {
		return SERVER_HOST;
	}

	public static int getTcpPort() {
		return TCP_PORT;
	}

	public static int getUdpPort() {
		return UDP_PORT;
	}
	
}
