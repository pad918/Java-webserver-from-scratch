import java.math.BigInteger;

/* Skriven kod:
2023-01-29: 274 lines

 */

public class Main {
	static Server s;
	public static void main(String[] args) {
		HTTP.HttpServer server = new HTTP.HttpServer();
		server.startServer(80);
		/*
		//System.out.println("Byte = "+Helper.byteToBinary((byte)-127));
		System.out.println("Working");
		System.out.println("Server started");
		// SRBrKYWs2Cw/9HrlY+Rh2g== GER FEL
		String key = "SRBrKYWs2Cw/9HrlY+Rh2g=="; // "iY3kOHDhLMQxlx1KEfJ/vg==" -> "O2nS8akA9NoLZlXsvKGNEuJ6rzw="
		key += "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		String enc = Hash.sha1HashString(key);
		System.out.println(key + " --> " + enc);
		//System.out.print("MAX RADIX = "+ Character.MAX_RADIX);

		s = new Server();
		try{s.start();}
		catch (Exception e){
			e.printStackTrace();
		}

		try{Thread.sleep(100000);}
		catch (Exception e){
			e.printStackTrace();
		}
		*/

		System.out.println("Closing server...");
	}
}
