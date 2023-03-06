package HTTP;

import java.io.*;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Set;

public class HttpRequest {
	public enum HttpReqestType{GET, POST, TRACE, HEAD, PUT, DELETE, OPTIONS, CONNECT}
	LinkedHashMap<String, String> headers;
	LinkedHashMap<String, String> parameters;
	HttpReqestType type;
	String requestedURL;
	String requestedURI;
	String httpVersion;
	byte[] data;

	public HttpRequest(BufferedInputStream in) throws IOException, ParseException {
		headers = new LinkedHashMap<String, String>();
		parameters = new LinkedHashMap<String, String>();
		readHttpRequest(in);
	}

	private void parseRequestLine(String statusLine) throws ParseException {
		String[] triplet = statusLine.split(" ");
		if(triplet.length!=3)
			throw new ParseException("Could not parse status line", 0);
		try {
			type = HttpReqestType.valueOf(triplet[0]);
			requestedURL = triplet[1];

			String[] parts = requestedURL.split("\\?");
			if(parts.length==2)
				requestedURI = parts[0];
			else
				requestedURI = requestedURL;

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

	private void parseQueryParameters(String url){
		String[] url_parts = url.split("\\?");
		if(url_parts.length!=2)
			return;

		String query= url_parts[1];
		String[] parameterPairs = query.split("&");
		for (String pair : parameterPairs){
			String[] parts = pair.split("=");
			if(parts.length==2)
				parameters.put(parts[0], parts[1]);
		}
	}

	public void readHttpRequest(BufferedInputStream in) throws IOException, ParseException {
		System.out.println("Waiting for request...");

		//OBS!!! långsam som sata antagligen
		String requestLine = readLine(in);
		System.out.println(requestLine);

		//handle status line
		parseRequestLine(requestLine);

		parseQueryParameters(requestedURL);

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

	public String getParameterValue(String key){
		String ret = parameters.get(key);
		return ret!=null?ret:"";
	}

	public Set<String> getParameterKeySet(){
		return parameters.keySet();
	}

	public String getURI(){
		return requestedURI;
	}

	public HttpReqestType getType(){
		return type;
	}
	public byte[] getData() {
		return data;
	}

}
