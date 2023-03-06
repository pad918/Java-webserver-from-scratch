package HTTP;

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

	public CloudHttpServer(){
		super();
		//Add URI redirects/pointers, eg domain.com/ --> domain.com/index.html
		Resource.addRedirect("/", "/index.html");
	}

	@Override
	public HttpResponse getResponse(HttpRequest request) {

		//If user is not signed in --> go to loginpage
		Authentication auth = new Authentication(request);
		HttpResponse response = new HttpResponse();
		if(!auth.isAuthId()) {
			auth.createNewAuthId(response);
		}
		//If not on allowed page, redirect
		boolean noAuthRequiered = request.getURI().equals("/auth") || request.getURI().equals("/login.html");
		if(!noAuthRequiered && !auth.isAuthenticated()){
			try {
				return response.makeRedirect("/login.html");
			}
			catch (Exception e){
				response.setHttpCode(500);
				return response;
			}
		}

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
		HttpResponse response = new HttpResponse();//getGetResponse(request);

		// Run script if needed...
		// 		Scripts can modify the database and the responce
		//		Scripts can access the request (which containes the post data)

		System.out.println("URI: " + request.getURI());
		CallableMethods.try_run(request.getURI(), request, response);

		return response;
	}

	private HttpResponse getGetResponse(HttpRequest request){
		HttpResponse response = new HttpResponse();
		response.addHeader("Connection", "close");
		Resource resource;
		try {
			String URI = request.getURI();

			resource = new Resource(URI);
			response.setResource(resource);

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
