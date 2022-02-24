package com.example.paperlessmeeting_demo.sharefile;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
	public static void send(byte[] data,InetAddress address,int port)throws IOException{
			DatagramPacket packet=new DatagramPacket(data,data.length,address,port);
			//创建DatagramSocket,实现数据发送和接收
			DatagramSocket socket=new DatagramSocket();
			//向服务器端发送数据报
			socket.send(packet);
			socket.close();
	}
	
}
