package HTTP;
import database.Database;
import HTTP.HttpRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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

	public static void run(String fun) throws InvocationTargetException, IllegalAccessException {
		Method method = callableMethods.get(fun);
		if(method!=null)
			method.invoke(null, "To be printed", 2);
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


}