import java.net.*;
import java.io.*;
import java.nio.CharBuffer;
import java.util.ArrayList;

public class Server{
	enum Http_type{GET, POST };
	private ServerSocket serverSocket;
	private Socket clientSocket;
	//private PrintWriter out;
	private DataOutputStream out;
	private BufferedReader in;
	private BufferedInputStream inStream;

	private void respondToHandsake(ArrayList<String> clientMessage)
	throws IOException{
		WebsocketHandShake clientHandShake = new WebsocketHandShake(clientMessage);
		WebsocketHandShake responceHandShake = clientHandShake.createResponce();
		StringBuilder responce = new StringBuilder("HTTP/1.1 101 Switching Protocols\r\n");
		responce.append(responceHandShake.getParamsAsStrings());
		responce.append("\r\n"); // has to end with blank line
		String responceString = responce.toString();
		System.out.println("----- RESPONCE ------");
		System.out.print(responceString);
		//out.print(responceString);
		out.writeBytes(responceString);
		out.flush();
		System.out.println("Listening");

		// Printing in binary
		for(int i=0; i<100; i++) { // WHILE SOCKET IS ON
			WebSocketFrame frame = new WebSocketFrame(inStream);
			frame.readHeader();
			frame.printHeaderValues();
		}
		System.out.println("Done...");
	}

	void OnConnected () throws IOException{
		//Respond with correct handshake to init websocket
		ArrayList<String> strings = new ArrayList<String>();
		String c = ".";
		System.out.println("--- HANDSHAKE REQUEST ---");
		while(!c.equals("")) {
			c = in.readLine();
			strings.add(c);
			System.out.println(c);
		}
		respondToHandsake(strings);
	}

	void start() throws IOException{
		serverSocket = new ServerSocket(8080);
		clientSocket = serverSocket.accept(); // OBS BLOCKING!!!
		//out = new PrintWriter(clientSocket.getOutputStream(), true);
		out = new DataOutputStream(clientSocket.getOutputStream());

		inStream = new BufferedInputStream(clientSocket.getInputStream());
		in = new BufferedReader(new InputStreamReader(inStream));

		System.out.println("Client sent a message: ...");
		OnConnected();

		System.out.print("\n");
	}

}