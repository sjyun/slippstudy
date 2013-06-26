package nio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer2 implements Runnable {
	Selector selector;
	int port = 80;
	BufferedReader br = null;
	PrintWriter pw = null;
	BufferedReader fbr = null;

	// -> 생성자
	public NioServer2() throws IOException {
		selector = Selector.open(); // 셀렉터를 생성한다.
		ServerSocketChannel server = ServerSocketChannel.open();// binding 되지 않는
																// ServerSocketChannel
																// 생성
		System.out.println(server);
		ServerSocket socket = server.socket();// 내부소켓 생성
		SocketAddress addr = new InetSocketAddress(port);
		socket.bind(addr);// binding
		System.out.println(server);
		server.configureBlocking(false);// Non-Blocking I/O모드 설정
		// 만들어진 ServerSocketChannel이 어떤 동작이 가능한지 본다.

		int validOps = server.validOps();
		System.out.print("ServerSocketChannel.validOps() : " + validOps);
		System.out.println(", " + (validOps == SelectionKey.OP_ACCEPT));
		// binding된 ServerSocketChannel을 Selector에 등록한다.
		server.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("****************************************");
		System.out.println("클라이언트의 접속을 기다리고 있습니다");
		System.out.println("****************************************");
	}

	// --> run()
	public void run() {
		// SocketChannel 용 행동 변수를 미리 만들어 둔다.
		int socketOps = SelectionKey.OP_CONNECT | SelectionKey.OP_READ
				| SelectionKey.OP_WRITE;
		ByteBuffer buf = null;// 버퍼
		while (true) {
			try {
				// 현재 selector에 등록된 채널에 관심있는 동작이 하나라도 실행된 경우 그 채널들을
				// SelectionKey의 Set에 추가한다.
				selector.select();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			// 선택된 채널(위의 select()로 지정된)들의 리스트를 얻는다.
			Set selectedKeys = selector.selectedKeys();
			// Set에 대해 Iterator를 얻어서 하나씩 처리하게 된다.
			Iterator iter = selectedKeys.iterator();
			while (iter.hasNext()) {
				try {
					SelectionKey selected = (SelectionKey) iter.next();
					iter.remove();// 현재 처리하는 SelectionKey는 Set에서 제거한다.
					// channel()는 현재 동작에 관련된 채널들을 리턴하는데
					// 이를 가지고 지금 현재 하는 작업이 읽기냐 쓰기냐 접속이냐를 판단한다.
					SelectableChannel channel = selected.channel();
					// 현재 동작중인 채널이 ServerSocketChannel이라면 accept()를 호출해서
					// 접속요청을 해온 상대방 소켓과 연결될 수 있는 SocketChannel 을 얻는다.
					if (channel instanceof ServerSocketChannel) {
						ServerSocketChannel serverChannel = (ServerSocketChannel) channel;
						SocketChannel socketChannel = serverChannel.accept();
						if (socketChannel == null) {
							System.out.println(" # null server socket");
							continue;
						}
						System.out.println(" # socket accepted : "+ socketChannel);
						socketChannel.configureBlocking(false);
																
																
																


						// 읽기(OP_READ),쓰기(OP_WRITE),접속(OP_CONNECT) 

						int validOps = socketChannel.validOps();
						System.out.print("SocketChannel.validOps() : "
								+ validOps);
						System.out.println(", " + (validOps == socketOps));
						// 소켓채널을 셀렉터에 등록
						socketChannel.register(selector, socketOps);
					} else {
						SocketChannel socketChannel = (SocketChannel) channel;
						buf = ByteBuffer.allocate(20);
						// 소켓채널의 가능 행동을 검사해서 맞다면 그에 대응하는 작업을 한다.
						// 접속인지 아닌지
						if (selected.isConnectable()) {
							System.out.println(" # socket connected");
							if (socketChannel.isConnectionPending()) {
								System.out.println(" # Connection is pending");
								socketChannel.finishConnect();
							}
						}
						// 읽기 인지 아닌지
						if (selected.isReadable()) {
							System.out.println("write");
							String line = br.readLine();
							int start = line.indexOf(" ") + 2;
							int end = line.lastIndexOf("HTTP") - 1;
							String filename = line.substring(start, end);
							if (filename.equals(""))
								filename = "index.html";
							System.out.println(filename + "요청");
							fbr = new BufferedReader(new FileReader(filename));
						}
						// 쓰기인지 아닌지
						if (selected.isWritable()) {
							
							
							String fline = null;
							while ((fline = fbr.readLine()) != null) {
								buf.put(fline.getBytes());
								buf.flip();
								socketChannel.write(buf);
							}													
							System.out.println(" # socket write");
						}
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}finally{
					try{
						if( fbr !=  null)
							fbr.close();
					}catch(Exception e){
						System.out.println(e);
					}
					try{
						if( br !=  null)
							br.close();
					}catch(Exception e){
						System.out.println(e);
					}
				}
			}
		}
	}

	public static void main(String ar[]) throws Exception {

		NioServer2 server = new NioServer2();
		new Thread(server).start();
	}
}
