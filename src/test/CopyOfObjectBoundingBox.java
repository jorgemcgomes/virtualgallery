package test;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.web3d.x3d.sai.*;
import org.web3d.x3d.sai.geometry3d.IndexedFaceSet;
import org.web3d.x3d.sai.grouping.Group;
import org.web3d.x3d.sai.rendering.Coordinate;
import org.web3d.x3d.sai.rendering.IndexedLineSet;
import org.web3d.x3d.sai.shape.*;

import core.Gallery;

public class CopyOfObjectBoundingBox {
	
	public static float BOUNDING_BOX_DISTANCE = 0.1f;
	
	private static final int[] FACESET_INDEXES = new int[]{
			0, 1, 2, 3, -1, 
			4, 5, 6, 7, -1, 
			0, 4, 5, 1, -1,
			0, 4, 7, 3, -1,
			3, 7, 6, 2, -1,
			1, 5, 6, 2, -1};
	
	private static final int[] LINESET_INDEXES = new int[]{
			0, 1, 2, 3, 0, 4, -1,
			7, 4, 5, 6, 7, 3, -1,
			5, 1, -1, 6, 2, -1};
	
	private Point3f lower;
	private Point3f upper;
	
	private Shape faceShape;
	private Shape lineShape;
	private Group boundingBox;
	private static Appearance highlightAppearance;
	private static Appearance invisibleAppearance;
	private static Appearance overAppearance;
	private static Appearance lineAppearance;
	
	public CopyOfObjectBoundingBox(Point3f low, Point3f up) {
		lower = low;
		upper = up;
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
	private Point3f[] getBoundsFromCorners(Point3f low, Point3f up) {
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
	
	private void scaleUpCorners(Point3f low, Point3f up) {
		Vector3f vec = new Vector3f();
		vec.sub(low, up);
		vec.normalize();
		vec.scale(BOUNDING_BOX_DISTANCE);
		low.add(vec);
		up.sub(vec);
	}
	
	
	public void setCorners(Point3f upper, Point3f lower) {
		this.upper = upper;
		this.lower = lower;
	}
	
	private Appearance solidColor(float r, float g, float b, float alpha) {
		X3DScene scene = Gallery.getInstance().getScene();
		Appearance app = (Appearance) scene.createNode("Appearance");
		Material mat = (Material) scene.createNode("Material");
		mat.setDiffuseColor(new float[]{r, g, b});
		mat.setTransparency(alpha);
		app.setMaterial(mat);
		app.realize();
		return app;
	}
	
	private void initAppearances() {
		highlightAppearance = solidColor(1.0f, 0, 0, 0.5f);
		overAppearance = solidColor(1.0f, 0, 0, 0.3f);
		invisibleAppearance = solidColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		X3DScene scene = Gallery.getInstance().getScene();
		lineAppearance = (Appearance) scene.createNode("Appearance");
		Material mat = (Material) scene.createNode("Material");
		mat.setEmissiveColor(new float[]{1.0f, 0.0f, 0.0f});
		lineAppearance.setMaterial(mat);
		LineProperties lp = (LineProperties) scene.createNode("LineProperties");
		lp.setApplied(true);
		lp.setLinewidthScaleFactor(2.0f);
		lineAppearance.setLineProperties(lp);
		lineAppearance.realize();
	}
	
	public void highlight() {
		if(invisibleAppearance == null)
			initAppearances();
		faceShape.setAppearance(highlightAppearance);
		lineShape.setAppearance(lineAppearance);
	}
	
	public void invisible() {
		if(invisibleAppearance == null)
			initAppearances();
		faceShape.setAppearance(invisibleAppearance);
		lineShape.setAppearance(invisibleAppearance);
	}
	
	public void over() {
		if(invisibleAppearance == null)
			initAppearances();
		faceShape.setAppearance(overAppearance);
		lineShape.setAppearance(lineAppearance);	
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
		if(boundingBox == null) {
			X3DScene scene = Gallery.getInstance().getScene();
			if(invisibleAppearance == null)
				initAppearances();
			
			Point3f low = (Point3f) lower.clone();
			Point3f up = (Point3f) upper.clone();
			scaleUpCorners(low, up);
			Point3f[] bounds = getBoundsFromCorners(low, up);

			float[] coords = new float[8 * 3];
			for(int i = 0 ; i < 8 ; i++) {
				coords[i * 3 + 0] = bounds[i].x;
				coords[i * 3 + 1] = bounds[i].y;
				coords[i * 3 + 2] = bounds[i].z;
			}
			
			Coordinate coord = (Coordinate) scene.createNode("Coordinate");
			coord.setPoint(coords);
			
			initFaces(coord);
			initLines(coord);
			boundingBox = (Group) scene.createNode("Group");
			boundingBox.realize();
			boundingBox.addChildren(new X3DNode[]{faceShape, lineShape});
		}
		return boundingBox;
	}
	
	private void initFaces(Coordinate coord) {
		X3DScene scene = Gallery.getInstance().getScene();
		faceShape = (Shape) scene.createNode("Shape");
		IndexedFaceSet ifs = (IndexedFaceSet) scene.createNode("IndexedFaceSet");
		ifs.setCoord(coord);
		ifs.setCoordIndex(FACESET_INDEXES);
		ifs.setSolid(false);
		faceShape.setGeometry(ifs);
		faceShape.setAppearance(invisibleAppearance);
	}
	
	private void initLines(Coordinate coord) {
		X3DScene scene = Gallery.getInstance().getScene();
		lineShape = (Shape) scene.createNode("Shape");
		IndexedLineSet ils = (IndexedLineSet) scene.createNode("IndexedLineSet");
		ils.setCoord(coord);
		ils.setCoordIndex(LINESET_INDEXES);
		lineShape.setGeometry(ils);
		lineShape.setAppearance(invisibleAppearance);
	}
}
