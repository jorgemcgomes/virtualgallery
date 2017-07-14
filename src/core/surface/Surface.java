package core.surface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.Transform3D;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.web3d.x3d.sai.SFBool;
import org.web3d.x3d.sai.SFTime;
import org.web3d.x3d.sai.X3DField;
import org.web3d.x3d.sai.X3DFieldEvent;
import org.web3d.x3d.sai.X3DFieldEventListener;
import org.web3d.x3d.sai.X3DGroupingNode;
import org.web3d.x3d.sai.X3DNode;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.grouping.Group;
import org.web3d.x3d.sai.pointingdevicesensor.TouchSensor;
import org.web3d.x3d.sai.rendering.Coordinate;
import org.web3d.x3d.sai.rendering.TriangleSet;
import org.web3d.x3d.sai.shape.Appearance;
import org.web3d.x3d.sai.shape.Shape;

import util.Utils;

import core.Gallery;
import core.module.SceneObject;


public class Surface extends FlatShape implements Serializable, Cloneable {
	
	private static final long serialVersionUID = -9103177365472228318L;

	/**
	 * Distancia da superficie em si ate a shape gerada.<p>
	 * Valor em metros
	 */
	public static final float SURFACE_SHIFT = 0.001f;
	
	private transient static Appearance invisibleAppearance;
	private transient static Appearance highlightAppearance;
	private transient static Appearance overAppearance;
	private transient static Appearance overHighlightAppearance;
	
	private boolean selectable;
	private boolean invisible;
	
	private transient Group group;
	private transient TouchSensor sensor;
	private transient Shape shape;
	private transient OverListener overListener;
	private transient Coordinate coordNode;
	
	private List<SceneObject> objects;
	private transient DistanceMeter distanceMeter;
	
	Surface() {
		super();
		objects = new LinkedList<SceneObject>();
	}
	
	static void setInvisibleAppearance(Appearance ap) {
		invisibleAppearance = ap;
	}
	
	static void setHighlightAppearance(Appearance ap) {
		highlightAppearance = ap;
	}
	
	static void setOverAppearance(Appearance ap) {
		overAppearance = ap;
	}
	
	static void setOverHighlightAppearance(Appearance ap) {
		overHighlightAppearance = ap;
	}
	
	public void invisible() {
		invisible = true;
		shape.setAppearance(invisibleAppearance);
	}
	
	public void highlight() {
		invisible = false;
		shape.setAppearance(highlightAppearance);
	}
	
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
	
	public boolean isSelectable() {
		return selectable;
	}
	
	public void addSensor(X3DFieldEventListener listener) {
		sensor.setEnabled(true);
		enableOver();
		SFTime touchTime = (SFTime) sensor.getField("touchTime");
		touchTime.addX3DEventListener(listener);
	}
	
	public void removeSensor(X3DFieldEventListener listener) {
		SFTime touchTime = (SFTime) sensor.getField("touchTime");
		touchTime.removeX3DEventListener(listener);
		disableOver();
		sensor.setEnabled(false);
	}
	
	private void enableOver() {
		X3DField over = sensor.getField("isOver");
		overListener = new OverListener();
		over.addX3DEventListener(overListener);
	}
	
	private void disableOver() {
		if(overListener != null) {
			X3DField over = sensor.getField("isOver");
			over.removeX3DEventListener(overListener);
			overListener = null;
		}
	}
	
	private void setOver() {
		if(shape.getAppearance().equals(highlightAppearance)) {
			shape.setAppearance(overHighlightAppearance);
		} else
			shape.setAppearance(overAppearance);
	}

	public float[] getLastHitPosition() {
		float[] hitPoint = new float[3];
		sensor.getHitPoint(hitPoint);
		return hitPoint;
	}
	
	public void enterScene(X3DGroupingNode parent) {
		X3DScene scene = Gallery.getInstance().getScene();
		
		group = (Group) scene.createNode("Group");
		parent.addChildren(new X3DNode[]{group});
		
		shape = getShape();
		group.addChildren(new X3DNode[]{shape});
		invisible();
		
		sensor = (TouchSensor) scene.createNode("TouchSensor");
		group.addChildren(new X3DNode[]{sensor});
		sensor.setEnabled(false);
		SFTime touchTime = (SFTime) sensor.getField("touchTime");
		touchTime.setUserData(this);
	}
	
	void transform(Transform3D transf) {
		for(Triangle t : triangles)
			t.transform(transf);
		coordNode.setPoint(getFlatPoints());
		transf.transform(normal);
	}
	
	private Shape getShape() {
		X3DScene scene = Gallery.getInstance().getScene();
		
		Shape s = (Shape) scene.createNode("Shape");
		TriangleSet ts = (TriangleSet) scene.createNode("TriangleSet");
		coordNode = (Coordinate) scene.createNode("Coordinate");
		coordNode.setPoint(getFlatPoints());
		ts.setCoord(coordNode);
		ts.setSolid(false);
		s.setGeometry(ts);
		
		return s;
	}
	
	private float[] getFlatPoints() {
		Point3f[] points = super.translatedPoints(SURFACE_SHIFT);
		float[] flatPoints = new float[points.length * 3];
		for(int i = 0 ; i < points.length ; i++) {
			flatPoints[i * 3] = points[i].x;
			flatPoints[i * 3 + 1] = points[i].y;
			flatPoints[i * 3 + 2] = points[i].z;
		}
		return flatPoints;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		Surface other = (Surface) obj;
		return super.equalsFlatShape(other) && this.selectable == other.selectable;
	}
	
	@Override
	public int hashCode() {
		Boolean b = new Boolean(selectable);
		return super.hashCode() + b.hashCode();
	}
	
	@Override
	public Surface clone() {
		Surface s = (Surface) super.clone();
		s.selectable = selectable;
		s.group = null;
		s.sensor = null;
		s.shape = null;
		s.overListener = null;
		return s;
	}
	
	private class OverListener implements X3DFieldEventListener {
		
		@Override
		public void readableFieldChanged(X3DFieldEvent evt) {	
			SFBool isOver = (SFBool) evt.getSource();
			if(isOver.getValue()) { // is over
				setOver();
			} else { // not over
				if(invisible)
					invisible();
				else
					highlight();
			}
		}
		
	}
	
	public void addObject(SceneObject obj) {
		objects.add(obj);
	}
	
	public void removeObject(SceneObject obj) {
		objects.remove(obj);
	}
	
	public Edge[] getDistances(SceneObject obj) {
		if(distanceMeter == null)
			distanceMeter = new DistanceMeter(perimeter, holes, objects);
		
		return distanceMeter.getDistances(obj);
	}
}
