package HTTP;
import java.net.*;
import java.io.*;

public class HttpServer {
	ServerSocket serverSocket;
	Socket clientSocket;
	public void startServer(int port) {
		System.out.println("Starting server on port: " + port);
		try {
			serverSocket = new ServerSocket(port);
		}
		catch (Exception e){
			System.out.println("Could not start server");
			e.printStackTrace();
		}
		while (true){
			try{
				clientSocket = serverSocket.accept();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			InetAddress connectedIp = clientSocket.getInetAddress();
			System.out.println("New http request from " + connectedIp);
			new HttpListener(clientSocket).start();
		}

	}
}
