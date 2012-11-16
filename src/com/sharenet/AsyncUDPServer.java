package com.sharenet;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

public class AsyncUDPServer {
	
	public static int cnt = 0 ;
	public static long start = 0;
	public static long used = 0;
	public static long total = 1000000 ;
	public static long step = 2000 ;
	public static int port = 8080;
	public static String[] s = new String[(int)total];
	public static CharsetDecoder decoder = Charset.forName("utf-8").newDecoder();
	
	static class Handler extends IoHandlerAdapter {
		
		
		@Override
		public void exceptionCaught(IoSession session, Throwable cause)
				throws Exception {
			cause.printStackTrace();
			session.close(true);
		}

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			if (message instanceof IoBuffer) {
				
				if ( start == 0 ) start = System.currentTimeMillis() ; 
				
				IoBuffer buffer = (IoBuffer) message;
				String msg = buffer.getString(decoder);
				//s[cnt] = msg;
				try{
					Thread.sleep(1);	
				}catch(Exception e){
					System.out.println("ERROR");
				}
				
				cnt ++;
				if ( cnt % (step) == 0 ){
					used = System.currentTimeMillis() - start;
					System.out.println("recive package:" + cnt + " through-put:" + ((double) cnt / (double) used)  * 1000 );
					System.out.println(msg);
				}
				
				if( cnt == total){
					used = System.currentTimeMillis() - start ;
					System.out.print("used time: " +  used + "ms total: " + cnt + " pkgs through-put:" +  ((double) cnt / (double) used)  * 1000 + " req/s lost-package:" + (total - cnt) );
					System.exit(0);
				}
			}
			
		}


		@Override
		public void sessionIdle(IoSession session, IdleStatus status)
				throws Exception {
			System.out.println("Session idle...");
		}

		@Override
		public void sessionOpened(IoSession session) throws Exception {
			System.out.println("Session Opened...");
		}
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
        NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
        acceptor.setHandler(new Handler());

        DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        chain.addLast("threadpool", new ExecutorFilter());

        DatagramSessionConfig dcfg = acceptor.getSessionConfig();
        dcfg.setReuseAddress(true);
        dcfg.setReceiveBufferSize(1048576);
        
        System.out.println("ReceiveBufferSize : " + dcfg.getReceiveBufferSize());

        acceptor.bind(new InetSocketAddress(8080));
        System.out.println("UDPServer listening on port " + 8080);
	}

}
