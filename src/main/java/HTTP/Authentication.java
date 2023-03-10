package HTTP;

import database.Database;

import java.util.Random;

/*
* 	Håller koll på auth av en användare.
*		Hanterar även kopplingen till databasen
* */
public class Authentication {

	String authId = null;
	private String authenticatedUser;

	public Authentication(HttpRequest request){
		Cookies cookies = new Cookies(request);
		authId = cookies.getCookie("auth");
		authenticatedUser = Database.getInstance().getUsernameOfCookieAuthentication(authId);
	}

	void createNewAuthId(HttpResponse response){
		System.out.println("Creating random auth id...");

		// Create new auth id for the user (in DB)
		authId = createRandomId(32);
		response.addHeader("Set-Cookie", "auth=" + authId);
	}

	public boolean isAuthId(){
		return authId!=null;
	}

	public boolean isAuthenticated(){
		return authenticatedUser!=null;
	}

	public String getAuthenticatedUser(){
		return authenticatedUser;
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
