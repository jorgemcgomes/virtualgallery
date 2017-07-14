package app.test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class FileTest {

	/**
	 * @param args
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws URISyntaxException {

		    File absolute = new File("/public/html/javafaq/index.html");
		    File relative = new File("html/javafaq/index.html");

		    System.out.println("absolute: ");
		    System.out.println(absolute.getName());
		    System.out.println(absolute.getPath());

		    System.out.println("relative: ");
		    System.out.println(relative.getName());
		    System.out.println(relative.getAbsolutePath());
	}

}
