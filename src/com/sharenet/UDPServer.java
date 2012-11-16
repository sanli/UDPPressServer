package com.sharenet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class UDPServer {


	public static int cnt = 0;
	
	public static void main(String[] args) throws IOException {

		long start = System.currentTimeMillis() ;
		long used = 0;
		long total = 10000000 ;
		long step = 2000 ;
		
		int port = 8080;

		// Create a socket to listen on the port.
		DatagramSocket dsocket = new DatagramSocket(port,InetAddress.getByName("192.168.1.107"));
		// Create a buffer to read datagrams into. If a
		// packet is larger than this buffer, the
		// excess will simply be discarded!
		byte[] buffer = new byte[2048];
		// Create a packet to receive data into the buffer
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		//String[] s = new String[(int)total];

		// Now loop forever, waiting to receive packets and printing them.
		dsocket.setSoTimeout(1000);
		// actually useless
		dsocket.setReceiveBufferSize(3145728);
		System.out.println("dsocket.getReceiveBufferSize():" + dsocket.getReceiveBufferSize());
		
		while (used < 30000 ) {
			// Wait to receive a datagram
			try{
				dsocket.receive(packet);
			}catch(Exception e){ 
				//continue ;
				used = System.currentTimeMillis() - start ;
				continue ;
			}
			

			// Convert the contents to a string, and display them
			String msg = new String(buffer, 0, packet.getLength());
			//s[cnt] = msg;
			// Reset the length of the packet before reusing it.
			packet.setLength(buffer.length);
			
			cnt ++;
			if ( cnt % (step) == 0 ){
				used = System.currentTimeMillis() - start;
				System.out.println("recive package:" + cnt + " through-put:" + ((double) cnt / (double) used)  * 1000 );
				System.out.println(msg);
			}
			
			if( cnt == total)
				break ;
		}
		
		used = System.currentTimeMillis() - start ;
		System.out.print("used time: " +  used + "ms total: " + cnt + " pkgs through-put:" +  ((double) cnt / (double) used)  * 1000 + " req/s lost-package:" + (total - cnt) );
	}
	
	static public int systemUDPRecived(){
		
		return 0 ;
	}
	
}
