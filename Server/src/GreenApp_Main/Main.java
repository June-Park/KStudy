package GreenApp_Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	private ServerSocket server;
	private DataInputStream data_In;
	private DataOutputStream data_Out;
	private Socket sock;

	public static void main(String[] str) {	
		new Main().run();
	}

	public void run() {
		try {
			
			System.err.println("thread start");
			
			server = new ServerSocket(10001);
			
			while(true)
			{
				sock = server.accept();
				
				System.err.println("client connect");
				
				data_In = new DataInputStream(sock.getInputStream());
				data_Out = new DataOutputStream(sock.getOutputStream());
	
				Thread st = new Thread(new Server_Thread(data_In, data_Out));
				st.start();
			}
			
		} catch (Exception e) {
		}

	}
}