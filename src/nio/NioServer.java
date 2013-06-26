package nio;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

public class NioServer {

	
	
	public static Queue<Socket> queue = new LinkedList<Socket>();
	
	public static void main(String ar[])throws IOException{
		ServerSocket server = new ServerSocket(80);
		

        while( true )
        {
           Socket connectionSocket = server.accept();
           synchronized(queue){
        	   queue.add( connectionSocket );
           }
           new Thread(new Runnable() {
			@Override
			public void run() {
				Socket connectionSocket = null;
				synchronized (queue) {
					connectionSocket = queue.poll();
				}
				if( connectionSocket == null) return;
				
				InputStream in;
				try{
					in = connectionSocket.getInputStream();
					DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
					
					ByteBuffer send_buff = ByteBuffer.allocate(4096);
					
					while( true ){
						 byte[] buff = new byte[4096];
						 send_buff.clear();
					}
					
				}catch(Exception e){
					
				}
				
			}
		}).start();
        }
	}
	
}

class nioWorker extends Thread{
	String host="127.0.0.1";
	String port = "80";
	/**
	 * Sync 용
	 */
	private Object sync = new Object();
	/*
	 * 접속용
	 * */
	private SocketChannel client;
	
	private ByteBuffer send_buf = ByteBuffer.allocate( 4096 );
	private ByteBuffer recv_buf = ByteBuffer.allocate( 4096 );
	{
		// 기본 초기화
		send_buf.order(ByteOrder.BIG_ENDIAN);
		recv_buf.order(ByteOrder.BIG_ENDIAN);
	}
	
	private String lastError = "";	
	/**
	 * 마지막에 발생한 애러 리턴
	 * @return
	 */
	public String getLastError(){
		return lastError;
	}
	
	public boolean connect()throws UnknownHostException, IOException{
		boolean ret = true;
		synchronized (sync) {
			client = SocketChannel.open();
			client.configureBlocking(false);
			if( !client.connect( new InetSocketAddress(host, Integer.parseInt(port) )  ) ){
				lastError = "Connect Fail " + host + ":" + port;
				return false;
			}
		}
		return ret;
	}
	
	public boolean close(){
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}


