package HTTP;
import java.net.*;

/*
* Plan för mer modulär server:
* 	HttpListener tar HttpServer objekt och använder
* 	metoder i HttpServer för att skapa responces
* 	till Http requests. Därmed behöver varje hemsida
* 	endast skapa class som derivar HttpServer och
* 	implementera dessa metoder, antagligen en för varje
* 	typ av Http request typ, GET, POST o.s.v.
* */


public abstract class HttpServer {
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
			new HttpListener(clientSocket, this).start();
		}

	}
	public abstract HttpResponse getResponse(HttpRequest request);
}
