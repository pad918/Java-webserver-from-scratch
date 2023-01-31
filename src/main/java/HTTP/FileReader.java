package HTTP;
import java.io.*;

public class FileReader {
	private long fileSize=-1;
	private BufferedInputStream inputStream;

	public FileReader(String path) throws FileNotFoundException {
		java.io.File f = new java.io.File(path);
		inputStream = new BufferedInputStream(new FileInputStream(path));
		fileSize = f.length();
	}

	public BufferedInputStream getInputStream(){
		return inputStream;
	}

	public long getSize(){
		return fileSize;
	}
	public void close() throws IOException {
		inputStream.close();
	}
}
