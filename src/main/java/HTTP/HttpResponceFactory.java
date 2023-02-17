package HTTP;

import java.io.FileNotFoundException;

/*
* Replaced by HttpServer.getResponse.
*	Remove in the future
*
* */

public class HttpResponceFactory {
	public static HttpResponse getGetResponce(HttpRequest request){
		HttpResponse responce = new HttpResponse();
		// Always force close, fix this later
		responce.addHeader("Connection", "close");

		//Lookup requested resource

		Resource resource;
		try {
			String URL = request.getUrl();
			resource = new Resource(URL);
			responce.setResource(resource);

			//Add resource specific headers (flytta till metod i responce?)
			responce.addHeader("Content-Length", Long.toString(resource.getFile().getSize()));
			String type = resource.getHTTPResourceTypeDescription();
			if(!type.equals(""))
				responce.addHeader("Content-Type", type);
		}
		catch (FileNotFoundException e){
			//e.printStackTrace(); // Att ta bort
			responce.setHttpCode(404);
		}

		return responce;
	}
}
