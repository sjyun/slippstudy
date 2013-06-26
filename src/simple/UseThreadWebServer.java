package simple;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class UseThreadWebServer {

	public static void main(String ar[]) throws Exception{
		ServerSocket server = new ServerSocket(80);
		
		while(true){
			System.out.println("요청대기");
			Socket sock = server.accept();
			System.out.println("접속");
			Worker work = new Worker(sock);
			work.start();
		}
	}
}

	 class Worker extends Thread {
		Socket sock = null;
		BufferedReader br = null;
		PrintWriter pw = null;

		public Worker(Socket sock) {
			this.sock = sock;
			try {
				br = new BufferedReader(new InputStreamReader(
						sock.getInputStream()));
				pw = new PrintWriter(new OutputStreamWriter(
						sock.getOutputStream()));
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		@Override
		public void run() {
			BufferedReader fbr = null;
			try {
				String line = br.readLine();
				int start = line.indexOf(" ") + 2;
				int end = line.lastIndexOf("HTTP") - 1;
				String filename = line.substring(start, end);

				if (filename.equals(""))
					filename = "index.html";
				System.out.println(filename + "요청");
				fbr = new BufferedReader(new FileReader(filename));
				String fline = null;
				while ((fline = fbr.readLine()) != null) {
					pw.println(fline);
					pw.flush();
				}
			} catch (Exception ex) {
				System.out.println(ex);
			} finally {
				try {
					if (fbr != null)
						fbr.close();
				} catch (Exception e) {
				}
				try {
					if (pw != null)
						pw.close();
				} catch (Exception ex) {
				}
				try {
					if (sock != null)
						sock.close();
				} catch (Exception e) {
				}

			}
		}
	}

