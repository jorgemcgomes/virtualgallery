package modules.art3d;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.vecmath.Point3f;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.geometry3d.Box;
import org.web3d.x3d.sai.shape.Appearance;
import org.web3d.x3d.sai.shape.Material;
import org.web3d.x3d.sai.shape.Shape;
import org.web3d.x3d.sai.texturing.ImageTexture;

import util.Utils;
import core.Gallery;

class ArtworkClumsy extends Artwork3D {
	
	private float height, width, length;

	ArtworkClumsy(Artwork3DInfo info) {
		super(info);
	}
	
	@Override
	protected Node getExportNode(Document doc, File output) {
		Element shape = doc.createElement("Shape");
		Element appearance = doc.createElement("Appearance");
		shape.appendChild(appearance);
		
		Element box = doc.createElement("Box");
		box.setAttribute("size", width + " " + height + " " + length);
		
		shape.appendChild(box);
		
		Element imgTex = doc.createElement("ImageTexture");
		File img;
		try {
			img = copyImage(output);
		} catch (IOException e) {
			e.printStackTrace();
			return doc.createComment("Error exporting: " + getArtworkInfo().getName());
		}
		File parent = output.getParentFile();
		URI relative = parent.toURI().relativize(img.toURI());
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
		
		Artwork3DInfo info = getArtworkInfo();
		int i = 0;
		File dest;
		String title = info.getName().replaceAll("[^a-zA-Z0-9]", "");
		do {
			dest = new File(resources, title + (i++) + ".png");
		} while(dest.exists());
		dest.createNewFile();
		Utils.writeScaledImage(dest, info.getThumbnail(), IMAGE_DIMENSION);
		return dest;
	}

	private static final int IMAGE_DIMENSION = 512;

	@Override
	protected ObjectNode getNode() {
		X3DScene scene = Gallery.getInstance().getScene();

		ImageTexture imgTex = (ImageTexture) scene.createNode("ImageTexture");
		imgTex.setRepeatS(false);
		imgTex.setRepeatS(false);
		File resize;
		try {
			resize = Utils.writeTempScaledImage(getArtworkInfo().getThumbnail(), IMAGE_DIMENSION);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		imgTex.setUrl(new String[]{resize.toURI().toString()});
		
		height = getArtworkInfo().getHeight();
		width = getArtworkInfo().getWidth();
		length = getArtworkInfo().getLength();
		if(height == 0 || width == 0 ||length == 0) {
			Point3f bboxLow = getArtworkInfo().getLowerBound();
			Point3f bboxUp = getArtworkInfo().getUpperBound();
			if(bboxLow != null && bboxUp != null) {
				width = bboxUp.x - bboxLow.x;
				height = bboxUp.y - bboxLow.y;
				length = bboxUp.z - bboxLow.z;
			}
		}
		
		Point3f upper = new Point3f(width / 2, height / 2, length / 2);
		Point3f lower = (Point3f) upper.clone();
		lower.negate();
		
		Box box = (Box) scene.createNode("Box");
		box.setSize(new float[]{width, height, length});
		
		Material mat = (Material) scene.createNode("Material");
		mat.setDiffuseColor(new float[]{0,1,0});
		
		Appearance app = (Appearance) scene.createNode("Appearance");
		app.setTexture(imgTex);	
		app.setMaterial(mat);

		Shape shape = (Shape) scene.createNode("Shape");
		shape.setGeometry(box);
		shape.setAppearance(app);

		return new ObjectNode(shape, upper, lower);
	}
}
