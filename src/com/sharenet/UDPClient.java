package com.sharenet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {
	public static long cnt = 0;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		long total = 1000000 ;
		long step = 1000 ;
	    String host = "192.168.1.107";
	    int port = 8080;
	    
	    // Get the internet address of the specified host
	    InetAddress address = InetAddress.getByName(host);
	    // Initialize a datagram packet with data and address
	    // Create a datagram socket, send the packet through it, close it.
        DatagramSocket dsocket = new DatagramSocket();
        dsocket.setSendBufferSize(1024 * 1024 * 4);
        long start = System.currentTimeMillis() , len = 0;
        
		for(int i = 0 ; i< total ; i++){
			byte[] message = Integer.toString(i).getBytes();	
			DatagramPacket packet = new DatagramPacket(message, message.length,
			          address, port);
			len += packet.getLength();
			
		    dsocket.send(packet);
			cnt ++;
			
			if ( cnt % step == 0 ){
				long used = System.currentTimeMillis() - start ;		
				System.out.println("send package:" + cnt + " len:" + len + " pkgs through-put:" +  ( (double)cnt / (double)used  * 1000) );
				Thread.sleep(40);
			}
			
		}
		
		long used = System.currentTimeMillis() - start ;
		System.out.print("used time: " +  used + "ms total: " + cnt + " pkgs through-put:" +  ( (double)cnt / (double)used  * 1000) + " req/s ");
	}

}
