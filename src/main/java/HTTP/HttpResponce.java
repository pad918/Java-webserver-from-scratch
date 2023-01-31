package HTTP;

import jdk.jshell.spi.ExecutionControl;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class HttpResponce {
	static final String folderRootPath =
			"C:/Users/Måns/Documents/programmering/java/WebSocketServerTest/src/main/java/resources/";
	private static HashMap<String, String> urlFilePair;
	private static HashMap<String, String> fileExtensionContentType; // load from file?

	public static void init(){
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

	public static void respond(DataOutputStream out, HttpRequest request) throws IOException {
		String url = request.getUrl();
		String fileName = urlFilePair.get(url);
		if(fileName==null)
			fileName = url.replaceAll("/", "");
		String filePath = folderRootPath + fileName;
		if(filePath==null){
			sendNotFoundResponce(out);
		}
		//Return file
		sendRequestedFile(out, filePath);
	}

	private static void sendNotFoundResponce(DataOutputStream out) throws IOException {
		out.writeBytes("HTTP/1.1 404 Not Found\r\n\r\n");
	}

	private static void sendRequestedFile(DataOutputStream out, String filePath)
			throws IOException {
		System.out.println("Sending requested file");
		try {
			StringBuilder responceBuilder = new StringBuilder("HTTP/1.1 200 OK\r\n");
			responceBuilder.append("Content-Type: " + getContentType(filePath) + "\r\n");
			responceBuilder.append("Connection: close\r\n");
			//Load file

			FileReader file = new FileReader(filePath);
			responceBuilder.append("Content-Length: " + file.getSize() + "\r\n");
			//End header
			responceBuilder.append("\r\n");
			out.writeBytes(responceBuilder.toString());

			//Write attached file
			BufferedInputStream fileStream = file.getInputStream();
			int readBytes = 0;
			byte[] fileBytes = new byte[1024];
			while (fileStream.available()>0) {
				int len = fileStream.read(fileBytes);
				if(len>0)
					readBytes+=len;
				if(len>0)
					out.write(fileBytes, 0, len);
			}
			System.out.println("Total read: " + readBytes + "B");
			file.close();
			out.flush();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
			sendNotFoundResponce(out);
		}
	}

	private static String getContentType(String fileName){
		String[] extensionSplit = fileName.split("\\.");
		if(extensionSplit.length<2)
			throw new IllegalArgumentException("File name invalid");
		String type = fileExtensionContentType.get(extensionSplit[extensionSplit.length-1]);
		System.out.println("File content type: " + type);
		return type!=null?type:"example";
	}

	void send(DataOutputStream out) throws IOException {
		//throw new ExecutionControl.NotImplementedException("DO NOT USE");
		/*StringBuilder responceBuilder = new StringBuilder("HTTP/1.1 200 OK\r\n");
		responceBuilder.append("Content-Type: text/html\r\n");
		responceBuilder.append("Connection: close\r\n");
		String html_test =
				"""
						<!DOCTYPE html>
						<html>
						  <head>
						    <title>HTML FIL FRAN JAVA SERVERN</title>
						    <meta charset="UTF-8">
						  </head>
						  <body>
						    <h1>DET HÄR ÄR EN EXEMPELSIDA</h1>
						    <p><a href="http://www.example.org/">www.example.org</a></p>
						  </body>
						</html>
				""";
		responceBuilder.append("Content-Length: " + html_test.length() + "\r\n");
		responceBuilder.append("\r\n");
		responceBuilder.append(html_test);
		out.writeBytes(responceBuilder.toString());
		out.flush();
		 */
	}

}
