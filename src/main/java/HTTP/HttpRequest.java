package HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;

public class HttpRequest {
	public enum HttpReqestType{GET, POST, TRACE, HEAD, PUT, DELETE, OPTIONS, CONNECT}
	LinkedHashMap<String, String> headers;
	HttpReqestType type;
	String requestedURL;
	String httpVersion;

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

	public void waitForRequest(BufferedReader in) throws IOException, ParseException {
		System.out.println("Waiting for request...");

		String requestLine = in.readLine();
		System.out.println(requestLine);

		//handle status line
		parseRequestLine(requestLine);

		String line = ".";
		while (!line.equals("")){
			line = in.readLine();
			parseHeaderLine(line);
			System.out.println(line);
		}
	}

	public String getHeaderValue(String key){
		String ret = headers.get(key);
		return ret!=null?ret:"";
	}

	public String getUrl(){
		return requestedURL;
	}
}
