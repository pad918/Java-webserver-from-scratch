package HTTP;
import java.net.*;
import java.io.*;
import java.text.ParseException;


public class HttpListener extends Thread {
	private final HttpServer server;
	private Socket socket;
	private DataOutputStream out;
	private BufferedReader ascii_in;
	private BufferedInputStream binary_in;
	private boolean keepAlive = false;
	private HttpRequest request;

	HttpListener(Socket accepted_socket, HttpServer server){
		System.out.println("Creating new thread");
		socket = accepted_socket;
		this.server = server;
	}

	@Override
	public void run() {
		//HttpResponce.init();
		try {
			binary_in = new BufferedInputStream(socket.getInputStream());
			ascii_in = new BufferedReader(new InputStreamReader(binary_in));
			out = new DataOutputStream(socket.getOutputStream());
		}
		catch (Exception e){
			e.printStackTrace();
			return;
		}
		while (true){
			try {
				waitForHttpRequest();
				HttpResponse response = server.getResponse(request);
				response.send(out);
			}
			catch (Exception e){
				e.printStackTrace();
				return; //Nödvändig?
			}
			keepAlive = false; // TEMPORÄR!!!
			if(!keepAlive)
				break;
		}
		try {
			socket.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		System.out.println("Closing thread");
	}

	private void waitForHttpRequest() throws IOException, ParseException {
		request = new HttpRequest(binary_in);
		String conType = request.getHeaderValue("Connection");
		keepAlive = conType.equals("keep-alive"); // Borde göras bättre
	}
}
