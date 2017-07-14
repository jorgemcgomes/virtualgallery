package app.test;

import java.io.File;
import java.io.IOException;

import org.web3d.vrml.sav.ImportFileFormatException;

import util.ExitException;
import util.NoExitSecurityManager;

public class ConverterTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ImportFileFormatException 
	 */
	public static void main(String[] args) throws ImportFileFormatException, IOException {
		
		File o = new File("C:\\Users\\Jorge\\My Dropbox\\Labmag\\Galeria virtual\\blender.x3d");
		File d = new File("C:\\Users\\Jorge\\My Dropbox\\Labmag\\Galeria virtual\\test_app.x3d");
		
		Converter.triangleSetNormalize(o, d);
		
		System.out.println("DOOOOONE");
	}

}

