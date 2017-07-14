package core.module;

import java.io.File;
import java.util.Arrays;

import javax.media.j3d.Transform3D;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.w3c.dom.*;
import org.web3d.x3d.sai.SFBool;
import org.web3d.x3d.sai.SFTime;
import org.web3d.x3d.sai.X3DFieldEvent;
import org.web3d.x3d.sai.X3DFieldEventListener;
import org.web3d.x3d.sai.X3DGroupingNode;
import org.web3d.x3d.sai.X3DNode;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.geometry3d.Box;
import org.web3d.x3d.sai.geometry3d.IndexedFaceSet;
import org.web3d.x3d.sai.grouping.Group;
import org.web3d.x3d.sai.grouping.Transform;
import org.web3d.x3d.sai.pointingdevicesensor.TouchSensor;
import org.web3d.x3d.sai.rendering.Color;
import org.web3d.x3d.sai.rendering.Coordinate;
import org.web3d.x3d.sai.rendering.IndexedLineSet;
import org.web3d.x3d.sai.rendering.LineSet;
import org.web3d.x3d.sai.shape.Appearance;
import org.web3d.x3d.sai.shape.LineProperties;
import org.web3d.x3d.sai.shape.Material;
import org.web3d.x3d.sai.shape.Shape;

import core.Gallery;
import core.surface.Edge;
import core.surface.Surface;


public abstract class AbstractSceneObject implements SceneObject {

	private static final long serialVersionUID = -378174721838312550L;

	transient protected TouchSensor sensor;
	transient protected X3DNode node;
	transient protected X3DGroupingNode parent;
	transient protected ObjectBoundingBox boundingBox;
	transient protected Group group;
	transient protected Transform placeTransform;
	transient protected Transform placeRotateTransform;
	transient protected Transform moveTransform;
	transient protected Shape[] distanceLines;

	transient private OverListener overListener;
	transient private boolean isSelected;

	// localizacao espacial
	private Surface over;
	Point3f surfacePoint;

	// transformacoes aplicadas
	private float rotation; // em rads
	private Point3f translation;

	// caracteristicas da geometria
	private Point3f upper;
	private Point3f lower;
	private Point3f centerPoint;

	public AbstractSceneObject() {
		this.rotation = 0;
		this.translation = new Point3f(0,0,0);
	}

	private Point3f centerPoint(Point3f upper, Point3f lower) {
		Point3f center = new Point3f();
		center.add(upper, lower);
		center.scale(0.5f);

		return center;
	}

	@Override
	public void activateSensor(X3DFieldEventListener modificationListener) {
		sensor.setEnabled(true);
		SFTime ttime = (SFTime) sensor.getField("touchTime");
		ttime.addX3DEventListener(modificationListener);
		ttime.setUserData(this);
		boundingBox.marked();
		SFBool isOver = (SFBool) sensor.getField("isOver");
		overListener = new OverListener();
		isOver.addX3DEventListener(overListener);
	}

	@Override
	public void removeSensor(X3DFieldEventListener listener) {
		SFTime ttime = (SFTime) sensor.getField("touchTime");
		ttime.removeX3DEventListener(listener);
		sensor.setEnabled(false);
		boundingBox.invisible();
		isSelected = false;
		SFBool isOver = (SFBool) sensor.getField("isOver");
		isOver.removeX3DEventListener(overListener);
	}

	@Override
	public void enterScene(X3DGroupingNode parent) {	
		this.parent = parent;
		X3DScene scene = Gallery.getInstance().getScene();

		ObjectNode obj = getNode();
		upper = obj.bboxUpper;
		lower = obj.bboxLower;
		node = obj.node;
		centerPoint = centerPoint(upper, lower);

		placeTransform = (Transform) scene.createNode("Transform");
		parent.addChildren(new X3DNode[]{placeTransform});

		placeRotateTransform = (Transform) scene.createNode("Transform");
		placeTransform.addChildren(new X3DNode[]{placeRotateTransform});

		moveTransform = (Transform) scene.createNode("Transform");
		placeRotateTransform.addChildren(new X3DNode[]{moveTransform});

		group = (Group) scene.createNode("Group");
		moveTransform.addChildren(new X3DNode[]{group});

		sensor = (TouchSensor) scene.createNode("TouchSensor");
		sensor.setEnabled(false);

		boundingBox = new ObjectBoundingBox(upper, lower);
		X3DNode bbox = boundingBox.getBox();

		Shape selectionBox = getInvisibleBox();

		group.addChildren(new X3DNode[]{sensor, selectionBox, node, bbox});


		// Restores object position (if not new)
		if(over != null && surfacePoint != null) {
			placeTransforms(over, surfacePoint);
			moveTransform.setCenter(new float[]{centerPoint.x, centerPoint.y, centerPoint.z});
			moveTransform.setRotation(new float[]{0, 1, 0, rotation});
			moveTransform.setTranslation(new float[]{translation.x, translation.y, translation.z});
		}
	}

	private void placeTransforms(Surface surf, Point3f point) {
		Vector3f objectNormal = new Vector3f(0, 1, 0);
		Vector3f surfaceNormal = surf.getNormal();

		/*
		 * Poe o objecto paralelo a superficie
		 */
		float angle = objectNormal.angle(surfaceNormal);
		Vector3f rotationAxis = new Vector3f();
		rotationAxis.cross(objectNormal, surfaceNormal);
		if(!isZero(rotationAxis.length()))
			rotationAxis.normalize();

		Point3f trans = new Point3f();
		Point3f touchPoint = new Point3f(centerPoint.x, lower.y, centerPoint.z);
		trans.sub(point, touchPoint);

		placeTransform.setCenter(new float[]{centerPoint.x, lower.y, centerPoint.z});
		placeTransform.setRotation(new float[]{rotationAxis.x, rotationAxis.y,
				rotationAxis.z, angle});
		placeTransform.setTranslation(new float[]{trans.x, trans.y,
				trans.z});

		/*
		 * Alinha o topo do objecto
		 */
		Vector3f objectTop = new Vector3f(1,0,0);
		AxisAngle4f rot = new AxisAngle4f(rotationAxis, angle);
		Transform3D transf = new Transform3D();
		transf.setRotation(rot);
		transf.transform(objectTop);

		Vector3f surfaceTop;
		// verticais
		if(isZero(surfaceNormal.x) && !isZero(surfaceNormal.y) && isZero(surfaceNormal.z)) {
			surfaceTop = new Vector3f(1,0,0);

			// horizontais
		} else if(isZero(surfaceNormal.y)) {
			surfaceTop = new Vector3f(0,1,0);

			// obliquas
		} else {
			/*Vector3f up = new Vector3f(0,1,0);
			float a = surfaceNormal.angle(up);
			a = (float) (Math.PI / 2 - a);
			System.out.println("oblique angle: " + a);

			Vector3f axis = new Vector3f();
			axis.cross(objectTop, up);
			AxisAngle4f r = new AxisAngle4f(axis, a);
			Transform3D t = new Transform3D();
			t.setRotation(r);
			t.transform(up);
			surfaceTop = up;*/

			surfaceTop = (Vector3f) objectTop.clone();
			int trys = 720;
			AxisAngle4f r = new AxisAngle4f(surfaceNormal, (float) (Math.PI * 2) / trys);
			Transform3D t = new Transform3D();
			t.setRotation(r);
			Vector3f maxY = new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
			for(int i = 0 ; i < trys ; i++) {
				t.transform(surfaceTop);
				if(surfaceTop.y > maxY.y)
					maxY = (Vector3f) surfaceTop.clone();
			}
			surfaceTop = maxY;
		}

		float ang = objectTop.angle(surfaceTop);

		transf = new Transform3D();
		Vector3f positiveTest = (Vector3f) objectTop.clone();
		transf.setRotation(new AxisAngle4f(surfaceNormal, ang));
		transf.transform(positiveTest);

		Vector3f negativeTest = (Vector3f) objectTop.clone();
		transf.setRotation(new AxisAngle4f(surfaceNormal, -ang));
		transf.transform(negativeTest);

		if(negativeTest.angle(surfaceTop) < positiveTest.angle(surfaceTop))
			ang = -ang;

		placeRotateTransform.setCenter(new float[]{centerPoint.x, centerPoint.y, centerPoint.z});
		placeRotateTransform.setRotation(new float[]{0,1,0, ang});
	}

	private static final int[] FACESET_INDEXES = new int[]{
		0, 1, 2, 3, -1, 
		4, 5, 6, 7, -1, 
		0, 4, 5, 1, -1,
		0, 4, 7, 3, -1,
		3, 7, 6, 2, -1,
		1, 5, 6, 2, -1};

	private Shape getInvisibleBox() {
		X3DScene scene = Gallery.getInstance().getScene();
		Shape s = (Shape) scene.createNode("Shape");
		Appearance app = (Appearance) scene.createNode("Appearance");
		Material mat = (Material) scene.createNode("Material");
		mat.setTransparency(1);
		app.setMaterial(mat);
		s.setAppearance(app);

		Coordinate coord = (Coordinate) scene.createNode("Coordinate");
		coord.setPoint(boundingBox.getScaledPoints());
		IndexedFaceSet ifs = (IndexedFaceSet) scene.createNode("IndexedFaceSet");
		ifs.setCoordIndex(FACESET_INDEXES);
		ifs.setCoord(coord);
		s.setGeometry(ifs);

		return s;
	}

	private boolean isZero(float val) {
		return val > -0.001 && val < 0.001;
	}

	@Override
	public void exitScene() {
		parent.removeChildren(new X3DNode[]{placeTransform});
		placeTransform.dispose();
		over.removeObject(this);
	}

	public void placeOverSurface(Surface surf, Point3f point) {
		if(over != null)
			over.removeObject(this);

		this.over = surf;
		this.surfacePoint = point;

		// RESET MOVE TRANSFORMATIONS
		rotation = 0;
		translation.set(0, 0, 0);
		moveTransform.setCenter(new float[]{centerPoint.x, centerPoint.y, centerPoint.z});
		moveTransform.setRotation(new float[]{0, 1, 0, 0});
		moveTransform.setTranslation(new float[]{0, 0, 0});

		// PLACE OBJET IN THE NEW SURFACE
		placeTransforms(surf, point);

		over.addObject(this);
	}

	public void select() {
		boundingBox.highlight();
		isSelected = true;
	}

	public void deselect() {
		boundingBox.marked();
		isSelected = false;
	}

	public void rotate(float angle) {
		float rads = (float) (angle * (Math.PI / 180.0));
		float[] rot = new float[4];
		moveTransform.getRotation(rot);
		rot[3] += rads;
		moveTransform.setRotation(rot);

		rotation += rads;
	}

	public void translate(int direction, float distance) {
		Vector3f directionVector;
		switch(direction) {
		case UP:
			directionVector = new Vector3f(1, 0, 0);
			break;
		case LEFT:
			directionVector = new Vector3f(0, 0, -1);
			break;
		case DOWN:
			directionVector = new Vector3f(-1, 0, 0);
			break;
		case RIGHT:
			directionVector = new Vector3f(0, 0, 1);
			break;
		default:
			return;
		}

		directionVector.scale(distance);
		float[] t = new float[3];
		moveTransform.getTranslation(t);
		t[0] += directionVector.x;
		t[1] += directionVector.y;
		t[2] += directionVector.z;
		moveTransform.setTranslation(t);

		translation.add(directionVector);

		// TODO test
		Edge[] ds = over.getDistances(this);
		if(ds[0] != null)
			System.out.println("UP: " + ds[0].getDistance() + " ; " + ds[0].getP1() + " -> " + ds[0].getP2());
		if(ds[1] != null)
			System.out.println("RIGHT: " + ds[1].getDistance() + " ; " + ds[1].getP1() + " -> " + ds[1].getP2());
		if(ds[2] != null)
			System.out.println("DOWN: " + ds[2].getDistance() + " ; " + ds[2].getP1() + " -> " + ds[2].getP2());
		if(ds[3] != null)
			System.out.println("LEFT: " + ds[3].getDistance() + " ; " + ds[3].getP1() + " -> " + ds[3].getP2());

		updateDistanceLines(ds);
	}
	
	static final float[][] LINE_COLOR = new float[][]{{1,0,0}, {0,1,0}, {0,0,1}, {1,0,1}};
	
	void updateDistanceLines(Edge[] ds) {
		X3DScene scene = Gallery.getInstance().getScene();
		Shape s = (Shape) scene.createNode("Shape");

		/*Appearance app = (Appearance) scene.createNode("Appearance");
		Material mat = (Material) scene.createNode("Material");
		mat.setEmissiveColor(new float[]{1,0,0});
		
		app.setMaterial(mat);*/
		
		Coordinate coord = (Coordinate) scene.createNode("Coordinate");
		coord.setPoint(new float[]{0,0,0, 10,10,10});
		LineSet ls = (LineSet) scene.createNode("LineSet");
		Color c = (Color) scene.createNode("Color");
		c.setColor(new float[]{0,0,1, 0,0,1});
		ls.setColor(c);
		//ls.setCoordIndex(new int[]{1,2,-1});
		ls.setCoord(coord);
		ls.realize();
		
		/*LineProperties lp = (LineProperties) scene.createNode("LineProperties");
		lp.setApplied(true);
		lp.setLinewidthScaleFactor(3);
		lp.setLinetype(1);
		app.setLineProperties(lp);*/
		
		s.setGeometry(ls);
		//s.setAppearance(app);
		s.realize();
		
		System.out.println("SHIIIIIIIIIIIIIT");
		Transform t = (Transform) scene.createNode("Transform");
		t.realize();
		t.addChildren(new X3DNode[]{s});
		parent.addChildren(new X3DNode[]{t});
		
		
		/*
		if(distanceLines == null)
			distanceLines = new Shape[4];
		X3DScene scene = Gallery.getInstance().getScene();
		for(int i = 0 ; i < 4 ; i++) {
			IndexedLineSet ls = null;
			//if(distanceLines[i] == null) {
				distanceLines[i] = (Shape) scene.createNode("Shape");
				ls = (IndexedLineSet) scene.createNode("IndexedLineSet");
				Appearance app = (Appearance) scene.createNode("Appearance");
				Material mat = (Material) scene.createNode("Material");
				mat.setEmissiveColor(LINE_COLOR[i]);
				app.setMaterial(mat);
				
				LineProperties lp = (LineProperties) scene.createNode("LineProperties");
				lp.setApplied(true);
				lp.setLinewidthScaleFactor(2);
				lp.setLinetype(1);
				app.setLineProperties(lp);
				app.realize();
				
				distanceLines[i].setGeometry(ls);
				distanceLines[i].setAppearance(app);
				distanceLines[i].realize();
				
				//parent.addChildren(new X3DNode[]{distanceLines[i]});
			//}
			
			//if(ls == null)
			//	ls = (IndexedLineSet) distanceLines[i].getGeometry();
			Coordinate coord = (Coordinate) scene.createNode("Coordinate");
			coord.setPoint(new float[]{ds[i].getP1().x, ds[i].getP1().y, ds[i].getP1().z,
					ds[i].getP2().x, ds[i].getP2().y, ds[i].getP2().z});
			ls.setCoord(coord);
			ls.setCoordIndex(new int[]{1,2,-1});
			ls.realize();
			distanceLines[i].realize();
			
			parent.addChildren(new X3DNode[]{distanceLines[i]});
			
		}*/
	}

	public void reset() {
		float[] t = new float[3];
		moveTransform.getTranslation(t);
		Arrays.fill(t, 0);
		moveTransform.setTranslation(t);
		translation.x = 0; translation.y = 0; translation.z = 0;

		float[] rot = new float[4];
		moveTransform.getRotation(rot);
		rot[3] = 0;
		moveTransform.setRotation(rot);
		rotation = 0;
	}

	public Point3f getUpper() {
		return upper;
	}

	public Point3f getLower() {
		return lower;
	}

	public Point3f getCenterPoint() {
		return centerPoint;
	}

	public Point3f[] getCurrentBounds() {
		Transform3D transform = getTransform();
		Point3f[] bounds = boundingBox.getBounds();
		for(Point3f p : bounds)
			transform.transform(p);
		return bounds;
	}

	private Transform3D getTransform() {
		float[] q = new float[4];
		float[] t = new float[3];

		Transform3D placeR = new Transform3D();
		placeTransform.getRotation(q);
		placeR.setRotation(new AxisAngle4f(q[0], q[1], q[2], q[3]));

		Transform3D placeC = new Transform3D();
		placeTransform.getCenter(t);
		Vector3f center = new Vector3f(t[0], t[1], t[2]);
		placeC.setTranslation(center);

		Transform3D placeIC = new Transform3D();
		center.negate();
		placeIC.setTranslation(center);

		Transform3D placeT = new Transform3D();
		placeTransform.getTranslation(t);
		placeT.setTranslation(new Vector3f(t[0], t[1], t[2]));

		Transform3D placeRotateR = new Transform3D();
		placeRotateTransform.getRotation(q);
		placeRotateR.setRotation(new AxisAngle4f(q[0], q[1], q[2], q[3]));

		Transform3D placeRotateC = new Transform3D();
		placeRotateTransform.getCenter(t);
		center = new Vector3f(t[0], t[1], t[2]);
		placeRotateC.setTranslation(center);

		Transform3D placeRotateIC = new Transform3D();
		center.negate();
		placeRotateIC.setTranslation(center);

		Transform3D moveR = new Transform3D();
		moveTransform.getRotation(q);

		moveR.setRotation(new AxisAngle4f(q[0], q[1] , q[2], q[3]));

		Transform3D moveC = new Transform3D();
		moveTransform.getCenter(t); 
		center = new Vector3f(t[0], t[1], t[2]);
		moveC.setTranslation(center);

		Transform3D moveIC = new Transform3D();
		center.negate();
		moveIC.setTranslation(center);

		Transform3D moveT = new Transform3D();
		moveTransform.getTranslation(t);
		moveT.setTranslation(new Vector3f(t[0], t[1], t[2]));


		Transform3D place = new Transform3D();
		place.setIdentity();
		place.mul(placeT);
		place.mul(placeC);
		place.mul(placeR);
		place.mul(placeIC);

		Transform3D placeRotate = new Transform3D();
		placeRotate.setIdentity();
		placeRotate.mul(placeRotateC);
		placeRotate.mul(placeRotateR);
		placeRotate.mul(placeRotateIC);

		Transform3D move = new Transform3D();
		move.setIdentity();
		move.mul(moveT);
		move.mul(moveC);
		move.mul(moveR);
		move.mul(moveIC);

		Transform3D res = new Transform3D();
		res.setIdentity();
		res.mul(place);
		res.mul(placeRotate);
		res.mul(move);

		return res;
	}

	public float getRotation() {
		return rotation;
	}

	public Point3f getTranslation() {
		return translation;
	}

	/**
	 * The object represented by the X3DNode should be placed as follows:
	 * - The visibl e side of the object (the one that will be totally visible
	 * once the object is placed over a surface) should be pointing at the same
	 * direction as positive Y axis.
	 * - To achieve a better default rotation, the "TOP" side of the object
	 * 	should point at the same direction as positive X axis.
	 * @return
	 */
	protected abstract ObjectNode getNode();

	public class ObjectNode {

		private X3DNode node;
		private Point3f bboxUpper;
		private Point3f bboxLower;

		public ObjectNode(X3DNode node, Point3f bboxUpper, Point3f bboxLower) {
			this.node = node;
			this.bboxLower = bboxLower;
			this.bboxUpper = bboxUpper;
		}

	}

	private class OverListener implements X3DFieldEventListener {

		@Override
		public void readableFieldChanged(X3DFieldEvent evt) {	
			SFBool isOver = (SFBool) evt.getSource();
			if(isOver.getValue()) { // is over
				boundingBox.over();
			} else { // not over
				if(isSelected)
					boundingBox.highlight();
				else
					boundingBox.marked();
			}
		}

	}

	@Override
	public Node toX3D(Document doc, File output) {
		float[] q = new float[4];
		float[] t = new float[3];

		Element place = doc.createElement("Transform");
		placeTransform.getTranslation(t);
		place.setAttribute("translation", t[0] + " " + t[1] + " " + t[2]);
		placeTransform.getRotation(q);
		place.setAttribute("rotation", q[0] + " " + q[1] + " " + q[2] + " " + q[3]);
		placeTransform.getCenter(t);
		place.setAttribute("center", t[0] + " " + t[1] + " " + t[2]);

		Element placeR = doc.createElement("Transform");
		placeRotateTransform.getRotation(q);
		placeR.setAttribute("rotation", q[0] + " " + q[1] + " " + q[2] + " " + q[3]);
		placeRotateTransform.getCenter(t);
		placeR.setAttribute("center", t[0] + " " + t[1] + " " + t[2]);

		Element move = doc.createElement("Transform");
		moveTransform.getTranslation(t);
		move.setAttribute("translation", t[0] + " " + t[1] + " " + t[2]);
		moveTransform.getRotation(q);
		move.setAttribute("rotation", q[0] + " " + q[1] + " " + q[2] + " " + q[3]);
		moveTransform.getCenter(t);
		move.setAttribute("center", t[0] + " " + t[1] + " " + t[2]);

		place.appendChild(placeR);
		placeR.appendChild(move);
		move.appendChild(getExportNode(doc, output));

		return place;
	}

	protected abstract Node getExportNode(Document doc, File output);
}
