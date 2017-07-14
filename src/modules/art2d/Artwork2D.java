package modules.art2d;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.vecmath.Point3f;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.web3d.x3d.sai.X3DNode;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.geometry3d.IndexedFaceSet;
import org.web3d.x3d.sai.rendering.Coordinate;
import org.web3d.x3d.sai.shape.Appearance;
import org.web3d.x3d.sai.shape.Material;
import org.web3d.x3d.sai.shape.Shape;
import org.web3d.x3d.sai.texturing.ImageTexture;
import org.web3d.x3d.sai.texturing.TextureCoordinate;

import util.Utils;
import core.Gallery;
import core.module.AbstractSceneObject;



class Artwork2D extends AbstractSceneObject {

	private static final int[] FACESET_INDEXES = {
		0, 1, 2, 3, -1, 
		4, 5, 6, 7, -1, 
		2, 3, 4, 7, -1,
		1, 2, 7, 6, -1,
		0, 1, 6, 5, -1,
		0, 3, 4, 5, -1};

	private static final float[] TEXTURE_INDEXES = {
		0,0, 0,0, 0,0, 0,0,
		1,0, 0,0, 0,1, 1,1
	};

	/**
	 * TODO: no processo inverso de serializacao, carregar esta paintinginfo
	 */
	private transient Artwork2DInfo info;
	private int artID;

	public static final float PAINTING_THICKNESS = 0.01f;
	public static final int IMAGE_DIMENSION = 1024;

	Artwork2D(Artwork2DInfo paint) {
		super();
		this.info = paint;
		this.artID = paint.getID();
	}
	
	void setInfo(Artwork2DInfo info) {
		this.info = info;
	}
	
	int getArtID() {
		return artID;
	}

	@Override
	protected ObjectNode getNode() {
		X3DScene scene = Gallery.getInstance().getScene();

		ImageTexture imgTex = (ImageTexture) scene.createNode("ImageTexture");
		imgTex.setRepeatS(false);
		imgTex.setRepeatT(false);
		File resize;
		try {
			resize = Utils.writeTempScaledImage(info.getImage(), IMAGE_DIMENSION);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		imgTex.setUrl(new String[]{resize.toURI().toString()});

		float h = info.getHeight();
		float w = info.getWidth();
		Point3f lower = new Point3f(0, 0, 0);
		Point3f upper = new Point3f(h, PAINTING_THICKNESS, w);
		
		IndexedFaceSet ifs = (IndexedFaceSet) scene.createNode("IndexedFaceSet");
		ifs.setSolid(false);
		ifs.setCoordIndex(FACESET_INDEXES);
		Coordinate coord = (Coordinate) scene.createNode("Coordinate");
		coord.setPoint(getCoordPoints(upper, lower));
		ifs.setCoord(coord);

		TextureCoordinate texCoord = (TextureCoordinate) scene.createNode("TextureCoordinate");
		texCoord.setPoint(TEXTURE_INDEXES);
		ifs.setTexCoord(texCoord);

		Material mat = (Material) scene.createNode("Material");
		mat.setDiffuseColor(new float[]{0,1,0});
		
		Appearance app = (Appearance) scene.createNode("Appearance");
		app.setTexture(imgTex);	
		app.setMaterial(mat);

		Shape shape = (Shape) scene.createNode("Shape");
		shape.setGeometry(ifs);
		shape.setAppearance(app);

		return new ObjectNode(shape, upper, lower);
	}

	/**
	 *     5------6      Y
	 *   / |    / |      |_ X
	 * 4------7   |     /    
	 * |   |  |   |    Z
	 * |   0------1    
	 * | /    | /  
	 * 3------2
	 */
	private float[] getCoordPoints(Point3f up, Point3f lo) {
		Point3f[] points = new Point3f[8];

		points[0] = new Point3f(lo);
		points[1] = new Point3f(up.x, lo.y, lo.z);
		points[2] = new Point3f(up.x, lo.y, up.z);
		points[3] = new Point3f(lo.x, lo.y, up.z);

		points[4] = new Point3f(lo.x, up.y, up.z);
		points[5] = new Point3f(lo.x, up.y, lo.z);
		points[6] = new Point3f(up.x, up.y, lo.z);
		points[7] = new Point3f(up);

		float[] coords = new float[8 * 3];
		for(int i = 0 ; i < 8 ; i++) {
			coords[i * 3 + 0] = points[i].x;
			coords[i * 3 + 1] = points[i].y;
			coords[i * 3 + 2] = points[i].z;
		}
		System.out.println(Arrays.toString(coords));

		return coords;
	}

	Artwork2DInfo getArtworkInfo() {
		return info;
	}

	@Override
	protected Node getExportNode(Document doc, File output) {
		Element shape = doc.createElement("Shape");
		Element appearance = doc.createElement("Appearance");
		shape.appendChild(appearance);
		
		Element ifs = doc.createElement("IndexedFaceSet");
		ifs.setAttribute("solid", "false");
		StringBuilder str = new StringBuilder();
		for(int i : FACESET_INDEXES)
			str.append(i + " ");
		ifs.setAttribute("coordIndex", str.toString());
		Element coord = doc.createElement("Coordinate");
		str = new StringBuilder();
		float[] points = getCoordPoints(getUpper(), getLower());
		for(float f : points)
			str.append(f + " ");
		coord.setAttribute("point", str.toString());
		ifs.appendChild(coord);
		
		Element texCoord = doc.createElement("TextureCoordinate");
		str = new StringBuilder();
		for(float f : TEXTURE_INDEXES)
			str.append(f + " ");
		texCoord.setAttribute("point", str.toString());
		ifs.appendChild(texCoord);
		
		shape.appendChild(ifs);
		
		Element imgTex = doc.createElement("ImageTexture");
		File img;
		try {
			img = copyImage(output);
		} catch (IOException e) {
			e.printStackTrace();
			return doc.createComment("Error exporting: " + info.getTitle());
		}
		File parent = output.getParentFile();
		URI relative = parent.toURI().relativize(img.toURI());
		System.out.println(relative.toString());
		imgTex.setAttribute("url", relative.toString());
		imgTex.setAttribute("repeatS", "false");
		imgTex.setAttribute("repeatT", "false");
		appearance.appendChild(imgTex);
		
		return shape;
	}
	
	public File copyImage(File output) throws IOException {
		File parent = output.getParentFile();
		File resources = new File(parent, Gallery.RESOURCE_DIR);
		if(!resources.exists() || !resources.isDirectory())
			resources.mkdir();
		
		int i = 0;
		File dest;
		String title = info.getTitle().replaceAll("[^a-zA-Z0-9]", "");
		do {
			dest = new File(resources, title + (i++) + ".png");
		} while(dest.exists());
		dest.createNewFile();
		Utils.writeScaledImage(dest, info.getImage(), IMAGE_DIMENSION);
		return dest;
	}
}
