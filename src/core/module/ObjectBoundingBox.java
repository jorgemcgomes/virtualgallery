package core.module;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.web3d.x3d.sai.*;
import org.web3d.x3d.sai.grouping.Group;
import org.web3d.x3d.sai.rendering.Coordinate;
import org.web3d.x3d.sai.rendering.IndexedLineSet;
import org.web3d.x3d.sai.shape.*;

import core.Gallery;
import core.surface.Surface;

public class ObjectBoundingBox {

	public static float BOUNDING_BOX_DISTANCE = Surface.SURFACE_SHIFT;

	private static final int[] LINESET_INDEXES = {
		0, 1, 2, 3, 0, 4, -1,
		7, 4, 5, 6, 7, 3, -1,
		5, 1, -1, 6, 2, -1};

	private Point3f lower;
	private Point3f upper;

	private Group group;
	private Shape boundingBox;

	public ObjectBoundingBox(Point3f low, Point3f up) {
		lower = low;
		upper = up;
	}
	
	private Point3f[] getBoundsFromCorners(Point3f up, Point3f low) {
		Point3f[] points = new Point3f[8];

		points[0] = new Point3f(low);
		points[1] = new Point3f(low.x, low.y, up.z);
		points[2] = new Point3f(up.x, low.y, up.z);
		points[3] = new Point3f(up.x, low.y, low.z);

		points[4] = new Point3f(low.x, up.y, low.z);
		points[5] = new Point3f(low.x, up.y, up.z);
		points[6] = new Point3f(up);
		points[7] = new Point3f(up.x, up.y, low.z);
		
		return points;
	}
	
	/**
	 *     5------6
	 *   / |    / |
	 * 4------7   |
	 * |   |  |   |
	 * |   1------2
	 * | /    | /  
	 * 0------3
	 */
	Point3f[] getBounds() {
		return getBoundsFromCorners(upper, lower);
	}
	
	float[] getScaledPoints() {
		Point3f scaledUp = (Point3f) upper.clone();
		Point3f scaledLow = (Point3f) lower.clone();
		scaleUpCorners(scaledUp, scaledLow, BOUNDING_BOX_DISTANCE);
		Point3f[] points = getBoundsFromCorners(scaledUp, scaledLow);
		
		float[] coords = new float[8 * 3];
		for(int i = 0 ; i < 8 ; i++) {
			coords[i * 3 + 0] = points[i].x;
			coords[i * 3 + 1] = points[i].y;
			coords[i * 3 + 2] = points[i].z;
		}
		
		return coords;
	}

	private void scaleUpCorners(Point3f up, Point3f low, float distance) {
		Vector3f vec = new Vector3f(1, 1, 1);
		vec.normalize();
		vec.scale(BOUNDING_BOX_DISTANCE);
		
		low.add(vec);
		up.sub(vec);
	}

	public void setCorners(Point3f upper, Point3f lower) {
		this.upper = upper;
		this.lower = lower;
	}

	private Appearance solidLine(float r, float g, float b, float thickness, int type) {
		X3DScene scene = Gallery.getInstance().getScene();
		Appearance app = (Appearance) scene.createNode("Appearance");
		Material mat = (Material) scene.createNode("Material");
		mat.setEmissiveColor(new float[]{r, g, b});
		app.setMaterial(mat);

		LineProperties lp = (LineProperties) scene.createNode("LineProperties");
		lp.setApplied(true);
		lp.setLinewidthScaleFactor(thickness);
		lp.setLinetype(type);
		app.setLineProperties(lp);

		app.realize();
		return app;
	}

	public void highlight() {
		boundingBox.setAppearance(solidLine(0,0,1,2,1));;
	}

	public void marked() {
		boundingBox.setAppearance(solidLine(0,0,1,0,3));
	}

	public void over() {
		boundingBox.setAppearance(solidLine(0,0,1,0,1));
	}
	
	public void invisible() {
		Appearance nil = (Appearance) Gallery.getInstance().getScene().createNode("Appearance");
		boundingBox.setAppearance(nil);
	}

	/**
	 *     5------6
	 *   / |    / |
	 * 4------7   |
	 * |   |  |   |
	 * |   1------2    
	 * | /    | /  
	 * 0------3
	 */
	public X3DNode getBox() {
		if(group == null) {
			X3DScene scene = Gallery.getInstance().getScene();
	
			float[] coords = getScaledPoints();
	
			Coordinate coord = (Coordinate) scene.createNode("Coordinate");
			coord.setPoint(coords);
	
			boundingBox = (Shape) scene.createNode("Shape");
			IndexedLineSet ils = (IndexedLineSet) scene.createNode("IndexedLineSet");
			ils.setCoord(coord);
			ils.setCoordIndex(LINESET_INDEXES);
			boundingBox.setGeometry(ils);
			
			group = (Group) scene.createNode("Group");
			group.realize();
			group.addChildren(new X3DNode[]{boundingBox});
		}
		return group;
	}
}
