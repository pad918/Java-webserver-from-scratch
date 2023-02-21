package HTTP;
import database.Database;
import HTTP.HttpRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

public class CallableMethods {

	private static HashMap<String, Method> callableMethods;

	static{
		callableMethods = new HashMap<String, Method>();
		Class thisClass = CallableMethods.class;
		Method[] callable = thisClass.getMethods();
		System.out.println("Callable methods: ");
		for(Method m : callable) {

			if(m.getParameterCount()==2)
				callableMethods.put(m.getName(), m);
			System.out.println(m.getParameterCount() + " " + m.getName());
		}
		try {
			run("testP");
		}
		catch (Exception e){e.printStackTrace();}
	}

	// TO BE REMOVED !!!
	public static void run(String fun) throws InvocationTargetException, IllegalAccessException {
		Method method = callableMethods.get(fun);
		if(method!=null)
			method.invoke(null, "To be printed", 2);
	}

	public static void try_run(String fun, HttpRequest request, HttpResponse response){
		String[] parts = fun.split("/");
		if(parts.length>0)
			fun = parts[parts.length-1];
		else
			return;
		
		Method meth = callableMethods.get(fun);
		if(meth!=null){
			try {
				meth.invoke(null, request, response);
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	public static void init(){}

	public static boolean auth(HttpRequest request) {
		byte[] data = request.getData();
		System.out.print("Http Request Data: ");
		for(byte b : data)
			System.out.print((char)b);
		return true;
	}

	public static void testP(String test, int bla) {
		System.out.println(test + " " + bla);
	}

	//Private methods
	private static String toAscii(byte[] data) {
		return new String(data, StandardCharsets.UTF_8);
	}

	private static Hashtable<String, String> parsePostString(String post){
		Hashtable<String, String> table = new Hashtable<String, String>();
		String[] pairs = post.split("&");
		for(String s : pairs) {
			String[] key_val = s.split("=");
			if(key_val.length==2)
				table.put(key_val[0], key_val[1]);
		}
		return table;
	}

	// Public methods
	public static void auth(HttpRequest request, HttpResponse response){
		String data = toAscii(request.getData());
		Hashtable<String, String> pairs = parsePostString(data);
		String username = pairs.get("uname");
		String password = pairs.get("psw");

		//Lookup in database (OBS !!! INGEN HASH ÄN !!!)
		boolean success = Database.getInstance().try_login(username, password);
		if(success){
			// Bind cookie to username
			System.out.println("Login SUCESS( User: " + username + ", Pwd: " + password);
			Cookies cookies = new Cookies(request);
			String auth_cookie = cookies.getCookie("auth");
			boolean updateSuccess = Database.getInstance().add_cookie_authentication(username, auth_cookie);
		}
		else{
			System.out.println("Login FAILED( User: " + username + ", Pwd: " + password);
		}
	}


}