package HTTP;

import java.util.Random;

/*
* 	Håller koll på auth av en användare.
*		Hanterar även kopplingen till databasen
* */
public class Authentication {

	String authId = null;

	public Authentication(HttpRequest request){
		Cookies cookies = new Cookies(request);
		authId = cookies.getCookie("auth");
	}

	void createNewAuthId(HttpResponse response){
		System.out.println("Creating random auth id...");

		// Create new auth id for the user (in DB)
		authId = createRandomId(32);
		response.addHeader("Set-Cookie", "auth=" + authId);
	}

	public boolean isAuthenticated(){
		return authId!=null;
	}

	private String createRandomId(int len){
		StringBuilder id = new StringBuilder();
		Random rand = new Random();
		for (;len>0;len--){
			int r = rand.nextInt(64, 125);
			id.append((char)r);
		}
		return id.toString();
	}
}
