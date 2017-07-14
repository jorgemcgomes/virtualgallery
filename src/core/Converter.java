package core;

import java.io.File;
import java.io.IOException;

import org.web3d.vrml.sav.ImportFileFormatException;
import org.web3d.x3d.sai.X3DGeometryNode;
import org.web3d.x3d.sai.X3DGroupingNode;
import org.web3d.x3d.sai.X3DNode;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.geometry3d.*;
import org.web3d.x3d.sai.grouping.Transform;
import org.web3d.x3d.sai.rendering.*;
import org.web3d.x3d.sai.shape.*;

import util.ExitException;
import util.NoExitSecurityManager;
import xj3d.filter.*;

public class Converter {

	public static int TS_LEVEL = 0;
	public static int IFS_LEVEL = 1;
	public static int ITS_LEVEL = 2;
	public static int MAX_LEVEL = 3;
	public static int HAS_TRANSFORM = 100;

	private static void convert(String[] args, File origin, File dest) {
		String o = origin.getAbsolutePath();
		String d = dest.getAbsolutePath();
		NoExitSecurityManager.forbidExit(); 
		try {
			CDFFilter.main(new String[]{"Identity", o, d});
		} catch(ExitException e) {
			System.out.println("Exit: " + e.getExitStatus());
		}
		for(String a : args) {
			try {
				CDFFilter.main(new String[]{a, d, d});
			} catch(ExitException e) {
				System.out.println("Exit: " + e.getExitStatus());
			}
		}
		NoExitSecurityManager.enableExit();
	}

	public static File getNormalizedFile(File origin, X3DScene scene) 
		throws IOException, ImportFileFormatException {

		File normalized = null;
		normalized = File.createTempFile("VirtualGallery_normalized_", ".x3d");
		normalized.deleteOnExit();
		int level = geometryLevel(scene);
		triangleSetNormalize(origin, normalized, level);
		return normalized;
	}
	
	public static File getNormalizedFile(File origin) 
		throws IOException, ImportFileFormatException {
		
		X3DScene scene = Gallery.getInstance().getScene(origin);
		if(scene == null)
			throw new IOException("Unnable to create scene from file");
		return getNormalizedFile(origin, scene);
	}
	
	private static void triangleSetNormalize(File o, File d, int level) {	
		System.out.println("Attempting LEVEL " + level + " conversion.");

		if(level == TS_LEVEL) {
			convert(new String[]{}, o, d);
		} else if(level == TS_LEVEL + HAS_TRANSFORM) {
			convert(new String[]{"FlattenTransform"}, o, d);	
		} else if(level == IFS_LEVEL || level == IFS_LEVEL + HAS_TRANSFORM) {
			convert(new String[]{"FlattenTransform", "IFSToTS"}, o, d);
		} else if(level == ITS_LEVEL || level == ITS_LEVEL + HAS_TRANSFORM) {
			convert(new String[]{"IFSFilter", "FlattenTransform","IFSToTS"}, o, d);
		} else if(level == MAX_LEVEL) {
			convert(new String[]{"Triangulation", "IFSFilter", "IFSToTS"}, o, d);
		} else {
			convert(new String[]{"Triangulation", "IFSFilter", "FlattenTransform", "IFSToTS"}, o, d);
		}
	}
	
	public static File getCoordFile(File origin) 
		throws IOException, ImportFileFormatException {
		X3DScene scene = Gallery.getInstance().getScene(origin);
		if(scene == null)
			throw new IOException("Unnable to create scene from file");
		File converted = null;
		converted = File.createTempFile("VirtualGallery_normalized_", ".x3d");
		converted.deleteOnExit();
		int level = geometryLevel(scene);
		coordNormalize(origin, converted, level);
		return converted;
	}
	
	private static void coordNormalize(File o, File d, int level) {
		System.out.println("Attempting LEVEL " + level + " conversion.");

		if(level == MAX_LEVEL || level == MAX_LEVEL + HAS_TRANSFORM)
			convert(new String[]{"Triangulation"}, o, d);
		else
			convert(new String[]{}, o, d);
		
		if(level >= HAS_TRANSFORM)
			convert(new String[]{"FlattenTransform"}, d, d);
	}
	
	public static void shortenDEF(File origin, File dest) throws ImportFileFormatException {
		convert(new String[]{"ShortenDEF"}, origin, dest);
	}
	
	public static void convertToX3D(File origin, File dest) throws ImportFileFormatException {
		NoExitSecurityManager.forbidExit();
		try {
			CDFFilter.main(new String[]{"Identity", origin.getAbsolutePath(), dest.getAbsolutePath()});
		} catch (ExitException e) {
			System.out.println("Exit: " + e.getExitStatus());
		}
		NoExitSecurityManager.enableExit();
	}
	
	private static int geometryLevel(X3DScene scene) {		
		X3DNode[] nodes = scene.getRootNodes();
		int geometryLevel = geometryLevel(nodes);
		boolean hasTransform = hasTransform(nodes);
		if(hasTransform)
			geometryLevel += HAS_TRANSFORM;

		return geometryLevel;
	}

	private static int geometryLevel(X3DNode[] nodes) {
		int level = 0;
		for(X3DNode n : nodes) {
			int temp = 0;
			if(n instanceof X3DGroupingNode) {
				X3DGroupingNode g = (X3DGroupingNode) n;
				X3DNode[] children = new X3DNode[g.getNumChildren()];
				g.getChildren(children);
				temp = geometryLevel(children);
			} else if(n instanceof Shape) {
				temp = shapeLevel((Shape) n);
			}
			if(temp > level)
				level = temp;
		}
		return level;
	}

	private static int shapeLevel(Shape shape) {
		X3DGeometryNode geometry = (X3DGeometryNode) shape.getGeometry();
		if(geometry instanceof TriangleSet)
			return TS_LEVEL;
		else if(geometry instanceof IndexedFaceSet)
			return IFS_LEVEL;
		else if(geometry instanceof IndexedTriangleSet || 
				geometry instanceof IndexedTriangleFanSet ||
				geometry instanceof IndexedTriangleStripSet)
			return ITS_LEVEL;
		else
			return MAX_LEVEL;
	}

	private static boolean hasTransform(X3DNode[] nodes) {
		for(X3DNode n : nodes) {
			if(n instanceof X3DGroupingNode) {
				if(n instanceof Transform) {
					return true;
				} else {
					X3DGroupingNode g = (X3DGroupingNode) n;
					X3DNode[] children = new X3DNode[g.getNumChildren()];
					g.getChildren(children);
					if(hasTransform(children))
						return true;
				}
			}
		}
		return false;
	}
}
