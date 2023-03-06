package HTTP;

/*
* Class for handling resources and
* authentication level needed to read/write to
* the resource
* */


import java.io.FileNotFoundException;
import java.util.HashMap;

public class Resource {

	// VARNING! Detta är inte ett säkert sätt att hantera det på.
	// Alla filer som är tillgängliga för alla borde istället finnas i en
	// hashtabell, som man kan söka i. Då kan ingen någonsin
	// få tillågn till filer som inte finns i hashtabellen.
	private static final String folderRootPath =
			"C:/Users/Måns/Documents/programmering/java/WebSocketServerTest/src/main/java/resources/";

	//This should be loaded from a file or a database!
	private static HashMap<String, String> fileExtensionContentType;

	private String URL;
	private FileReader file;


	// Wow, en ny upptäckt!
	static {
		fileExtensionContentType = new HashMap<String, String>();
		// Som sagt, detta bör läsas från DB/fil.
		fileExtensionContentType.put("png",  "image/png");
		fileExtensionContentType.put("jpg",  "image/jpeg");
		fileExtensionContentType.put("jpeg", "image/jpeg");
		fileExtensionContentType.put("bmp",  "image/bmp");
		fileExtensionContentType.put("html", "text/html; charset=utf-8");
		fileExtensionContentType.put("pdf",  "document/pdf");
	}

	public Resource(String URL) throws FileNotFoundException {
		this.URL = URL;

		//Main website case (Gör en hashlist för detta?)
		if(URL.equals("/"))
			URL = "/index.html";

		String path = folderRootPath + URL;
		loadFile(path);
	}

	public FileReader getFile(){
		return file;
	}

	private String getFileExtension(){
		String[] sep = URL.split("\\.");
		if(sep.length==0)
			return null;
		return sep[sep.length-1];
	}

	public String getHTTPResourceTypeDescription(){
		String description = null;
		try {
			description= fileExtensionContentType.get(getFileExtension());
		}
		catch (NullPointerException e){}
		return description!=null?description:"";

	}

	private void loadFile(String path) throws FileNotFoundException {
		file = new FileReader(path);
	}

}
