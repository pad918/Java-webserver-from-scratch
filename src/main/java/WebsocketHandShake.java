import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class WebsocketHandShake {
	public enum Http_type{GET, POST };
	public Http_type type;
	//public HashMap<String, String> handShakeParams;
	public LinkedHashMap<String, String> handShakeParams;
	public WebsocketHandShake(){
		handShakeParams = new LinkedHashMap<String, String>();
	}

	public WebsocketHandShake(ArrayList<String> handShakeStrings){
		this();
		if(handShakeStrings.size()<1){
			throw new IllegalArgumentException("Invalid handshake request");
		}
		//String responce = "";
		String first_line = handShakeStrings.get(0).replaceAll("\n", "").replaceAll("\r", "");
		String[] args = first_line.split(" ");
		try{
			type = Http_type.valueOf(args[0]);
		}catch (Exception e){
			e.printStackTrace();
		}


		// Read the rest of the parameters
		handShakeStrings.remove(0); // Remove the first processed line
		for (String line: handShakeStrings) {
			String lineWithoutCtrlChars = line.replaceAll("\r\n", "").trim();
			String[] keyValuePair = lineWithoutCtrlChars.split(": ");
			if(keyValuePair.length==2){
				handShakeParams.put(keyValuePair[0].trim(), keyValuePair[1].trim());
			}
		}
		System.out.print("TEST123\n");
	}

	private String calculateKeyHash(){
		String key = handShakeParams.get("Sec-WebSocket-Key");
		key = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		// hash the key:
		String hashKey = Hash.sha1HashString(key);
		return hashKey;
	}

	public WebsocketHandShake createResponce(){
		WebsocketHandShake serverHandShake = new WebsocketHandShake();
		serverHandShake.handShakeParams.put("Upgrade", handShakeParams.get("Upgrade"));
		serverHandShake.handShakeParams.put("Connection", handShakeParams.get("Connection"));
		String keyHash = calculateKeyHash();
		serverHandShake.handShakeParams.put("Sec-WebSocket-Accept", keyHash);
		//serverHandShake.handShakeParams.put("Sec-WebSocket-Protocol", "chat");
		return  serverHandShake;
	}

	public String getParamsAsStrings(){
		StringBuilder builder = new StringBuilder("");
		Set<String> keySet = handShakeParams.keySet();
		for(var key : keySet){
			builder.append(key);
			builder.append(": ");
			builder.append(handShakeParams.get(key));
			builder.append("\r\n");
		}
		return builder.toString();
	}
}
