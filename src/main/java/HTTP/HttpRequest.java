package HTTP;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.LinkedHashMap;

public class HttpRequest {
	public enum HttpReqestType{GET, POST, TRACE, HEAD, PUT, DELETE, OPTIONS, CONNECT}
	LinkedHashMap<String, String> headers;
	HttpReqestType type;
	String requestedURL;
	String httpVersion;
	byte[] data;

	public HttpRequest(){
		headers = new LinkedHashMap<String, String>();
	}

	private void parseRequestLine(String statusLine) throws ParseException {
		String[] triplet = statusLine.split(" ");
		if(triplet.length!=3)
			throw new ParseException("Could not parse status line", 0);
		try {
			type = HttpReqestType.valueOf(triplet[0]);
			requestedURL = triplet[1];
			httpVersion = triplet[2];
		}
		catch (Exception e){
			throw new ParseException("Could not parse status line", 0);
		}
	}

	private void parseHeaderLine(String headerLine){
		String[] pair = headerLine.split(": ");
		if(pair.length==2){
			headers.put(pair[0], pair[1]);
		}
	}

	private String readLine(BufferedInputStream in) throws IOException {
		StringBuilder builder = new StringBuilder();
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int b = 0;
		while (true){
			b = in.read();
			if((char)b == '\r') {
				in.read(); //remove \n
				break;
			}
			builder.append((char)b);
		}

		return builder.toString();
	}

	public void readHttpHeader(BufferedInputStream in) throws IOException, ParseException {
		System.out.println("Waiting for request...");

		//OBS!!! långsam som sata antagligen
		String requestLine = readLine(in);
		System.out.println(requestLine);

		//handle status line
		parseRequestLine(requestLine);

		String line = ".";
		while (!line.equals("")){
			line = readLine(in);
			parseHeaderLine(line);
			System.out.println(line);
		}
		readHttpData(in);
	}

	public void readHttpData(BufferedInputStream in) throws IOException {
		// Read data of line
		int len = 0;
		long limit = 0;
		byte[] buff = new byte[1024];
		ByteArrayOutputStream byteBuf = new ByteArrayOutputStream();
		String len_str = getHeaderValue("Content-Length");
		try{
			limit = Long.parseLong(len_str);
		}
		catch (Exception e){
			limit = 0;
		}
		System.out.println("Available: " + in.available()+"B");
		while(byteBuf.size() < limit && (len = in.read(buff))>=0){
			byteBuf.write(buff, 0, len);
		}
		data = byteBuf.toByteArray();
	}

	public String getHeaderValue(String key){
		String ret = headers.get(key);
		return ret!=null?ret:"";
	}

	public String getUrl(){
		return requestedURL;
	}

	public HttpReqestType getType(){
		return type;
	}
	public byte[] getData() {
		return data;
	}

}
