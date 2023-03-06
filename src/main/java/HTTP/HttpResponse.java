package HTTP;

import jdk.jshell.spi.ExecutionControl;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class HttpResponse {
	static final String folderRootPath =
			"C:/Users/Måns/Documents/programmering/java/WebSocketServerTest/src/main/java/resources/";
	private static HashMap<String, String> urlFilePair;
	private static HashMap<String, String> fileExtensionContentType; // load from file?

	private int responceCode = 200;
	private LinkedHashMap<String, String> headers;
	private Resource resource;
	private byte[] data;

	static{
		fileExtensionContentType = new HashMap<String, String>();
		urlFilePair = new HashMap<String, String>();
		fileExtensionContentType.put("png", "image/png");
		fileExtensionContentType.put("jpg", "image/jpeg");
		fileExtensionContentType.put("jpeg", "image/jpeg");
		fileExtensionContentType.put("bmp", "image/bmp");
		fileExtensionContentType.put("html", "text/html; charset=utf-8");
		fileExtensionContentType.put("pdf", "document/pdf");

		//Det här är rätt efterblivet... eller super geni?
		urlFilePair.put("/", "index.html");
		urlFilePair.put("/favicon.ico", "insek.png");
	}

	public HttpResponse(){
		headers = new LinkedHashMap<String, String>();
	}

	public HttpResponse makeRedirect(String redirectURL){
		setHttpCode(301);
		addHeader("Location", redirectURL);
		return this;
	}

	private void sendErrorCode(DataOutputStream out) throws IOException {
		//Should add code name
		String errorMessage = "HTTP/1.1 " + responceCode + "\r\n\r\n";
		out.writeBytes(errorMessage);
		out.flush();
	}

	public void addHeader(String key, String value){
		headers.put(key, value);
	}

	public void setHttpCode(int code) {
		responceCode = code;
	}

	public void setResource(Resource resource){
		this.resource = resource;
	}

	private void writeResourceToOut(DataOutputStream out) throws IOException {
		byte[] buff = new byte[1024];
		BufferedInputStream fileStream = resource.getFile().getInputStream();
		int readLen = 0;
		while (readLen>=0) {
			out.write(buff, 0, readLen);
			readLen = fileStream.read(buff);
		}
	}

	void send(DataOutputStream out) throws IOException {
		//Test if erronous code:
		if(responceCode>=400) {
			sendErrorCode(out);
			return;
		}

		StringBuilder responceBuilder = new StringBuilder("HTTP/1.1 ");
		responceBuilder.append(String.valueOf(responceCode) + "\r\n");
		var headerSet = headers.entrySet();
		for(var headerPair : headerSet){
			responceBuilder.append(headerPair.getKey() + ": " + headerPair.getValue() + "\r\n");
		}
		responceBuilder.append("\r\n");
		out.writeBytes(responceBuilder.toString());

		if(resource!=null) {
			writeResourceToOut(out);
		}
		out.flush();
	}

}
