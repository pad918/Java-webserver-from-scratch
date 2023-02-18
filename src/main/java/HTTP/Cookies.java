package HTTP;

import java.util.LinkedHashMap;

public class Cookies {
	LinkedHashMap<String, String> cookies;
	public Cookies(HttpRequest request){
		cookies = new LinkedHashMap<>();
		String serialized_cookies = request.getHeaderValue("Cookie");
		deserializeCookies(serialized_cookies);
	}

	public String getCookie(String name){
		return cookies.get(name);
	}

	private void deserializeCookies(String serialized){
		String[] cookie_pairs = serialized.split(";");
		for(String c : cookie_pairs) {

			String[] values = c.split("=");
			if(values.length!=2)
				continue;
			cookies.put(values[0], values[1]);

		}
	}
}
