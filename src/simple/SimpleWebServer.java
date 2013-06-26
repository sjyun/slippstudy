package simple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleWebServer {

	public static void main(String ar[]){
		Socket sock = null;
		BufferedReader br = null;
		
		try {
			ServerSocket server = new ServerSocket(80);
			sock = server.accept();
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String line = null;
			while((line = br.readLine()) != null){
				System.out.println(line);
			}
		} catch (Exception e) {
			System.out.println(e);
		}finally{
			try{
				if(br != null) br.close();
				if(sock != null) sock.close();
			}catch(Exception ex){
				System.out.println(ex);
			}
		}
	}
}
