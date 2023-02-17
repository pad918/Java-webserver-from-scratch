package resources;

import HTTP.HttpRequest;

public class CallableMetods {
	public static boolean auth(HttpRequest request) {
		byte[] data = request.getData();
		System.out.print("Http Request Data: ");
		for(byte b : data)
			System.out.print((char)b);
		return true;
	}
}
