package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class NioClient {
	Object sync = new Object();
	private SocketChannel client;
	ByteBuffer send_buff=ByteBuffer.allocate(20); //버퍼생성
	
	{
		//buffer 초기화
		send_buff.order(ByteOrder.BIG_ENDIAN);
	}
	
	public boolean connect()throws UnknownHostException, IOException
	{
		boolean ret = true;
		synchronized (sync) {
			client = SocketChannel.open();
			client.configureBlocking(false);
			if( !client.connect( new InetSocketAddress("127.0.0.1", Integer.parseInt("8080") )  ) ){
			ret = false;
			}
		}
		return ret;
	}
	
	
	public static void main(String[] args) throws Exception {
		// SocketChannel생성해서 접속한다.
		NioClient client = new NioClient();
		client.connect();
		System.out.println("접속 성공");
		
	
	}
}
