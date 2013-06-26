package nio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Webserver {


	
	public static void main(String ar[]) throws Exception{
		ServerSocket server = new ServerSocket(80);
		Selector selector = Selector.open();
		ServerSocketChannel channel = ServerSocketChannel.open();
		SocketAddress addr = new InetSocketAddress("127.0.0.1", 80);
		while(true){
			System.out.println("요청대기");
			//Socket sock = server.accept();
			System.out.println("접속");
			Worker work = new Worker(channel, addr, selector);
			work.start();
		}
	}
}

	 class Worker extends Thread {
		 ServerSocketChannel channel = null;
		 Selector selector = null;
		 private final ByteBuffer read_buffer = ByteBuffer.allocate(2048);
		 private final ByteBuffer write_buffer = ByteBuffer.allocate(2048);
		 String filename = "index.html";
		
		
		private boolean isRunning = true;

		public Worker(ServerSocketChannel channel, SocketAddress addr, Selector selector) throws IOException{
			this.channel = channel;
			this.selector = selector;
			channel.socket().bind(addr);
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_ACCEPT);
			
			 read_buffer.order(ByteOrder.BIG_ENDIAN);
		}
		
			
					
		

		@Override
		public void run() {
				
			if(isRunning){
				try{
					selector.selectNow();
					 Iterator<SelectionKey> i = selector.selectedKeys().iterator();
		                while (i.hasNext()) {
		                    SelectionKey key = i.next();
		                    i.remove();
		                    if (!key.isValid()) {
		                        continue;
		                    }
		                    try{
		                    	if(key.isAcceptable()){
		                    		SocketChannel client = channel.accept();
		                    		client.configureBlocking(false);
		                    		client.register(selector, SelectionKey.OP_READ);
		                    	}else if(key.isReadable()){
		                    		 SocketChannel client = (SocketChannel) key.channel();
		                    		 int read = client.read(read_buffer);
		                    		 read_buffer.flip();
		                    	}                    		 
		                    }catch(IOException ioe){
		                    			
		                    }
		            				
		                }
			}catch(Exception e){
				}
			}
		}
	 
}
