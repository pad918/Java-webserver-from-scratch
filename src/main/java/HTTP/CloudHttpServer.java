package HTTP;

import HTTP.CallableMethods;

/*
* 	Senaste planen:
* 		En användare kan söka efter bild eller tag eller liknande.
* 		Detta ger en csv fil med alla URI:er till bilderna.
* 		Klient js hämtar bilderna från servern. (Ingen XMLHTTP... behöver användas)
*
*
* */

import java.io.FileNotFoundException;

public class CloudHttpServer extends HttpServer{
	@Override
	public HttpResponse getResponse(HttpRequest request) {
		return switch (request.getType()) {
			case GET -> getGetResponse(request);
			case POST -> getPostResponse(request);
			default -> null; // CHANGE!
		};
	}

	//Should be able to access the database
	private HttpResponse getPostResponse(HttpRequest request){
		CallableMethods.auth(request);
		//request.requestedURL = "/index.html";
		HttpResponse response = getGetResponse(request);
		// Run script if needed...
		// 		Scripts can modify the database and the responce
		//		Scripts can access the request (which containes the post data)
		System.out.println("URL: " + request.requestedURL);
		CallableMethods.try_run(request.requestedURL, request, response);

		return response;
	}

	private HttpResponse getGetResponse(HttpRequest request){
		HttpResponse response = new HttpResponse();
		response.addHeader("Connection", "close");
		Resource resource;
		try {
			String URI = request.getURI();
			//Direct to auth (insert state mache here?)
			if(URI.equals("/"))
				URI = "/login.html";
			resource = new Resource(URI);
			response.setResource(resource);

			// Kolla på kakan eller skapa kaka om den inte finns
			Authentication auth = new Authentication(request);
			if(!auth.isAuthenticated())
				auth.createNewAuthId(response);

			//Add resource specific headers (flytta till metod i responce?)
			response.addHeader("Content-Length", Long.toString(resource.getFile().getSize()));
			String type = resource.getHTTPResourceTypeDescription();
			if(!type.equals(""))
				response.addHeader("Content-Type", type);
		}
		catch (FileNotFoundException e){
			response.setHttpCode(404);
		}
		return response;
	}
}
