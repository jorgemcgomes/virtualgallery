package test;

import javax.vecmath.Point3f;
import java.io.Serializable;
import javax.vecmath.Vector3f;

import org.web3d.x3d.sai.X3DGroupingNode;
import org.web3d.x3d.sai.grouping.Transform;

import core.surface.Surface;


public class MovableObject implements Serializable {

	public static final int UP = 1;
	public static final int LEFT = 2;
	public static final int DOWN = 3;
	public static final int RIGHT = 4;

	protected transient Transform transform;
	
	protected Point3f centerPoint;
	protected Surface over;
	/**
	 * rotacao total em rads
	 */
	private float rotation;
	/**
	 * translacao total em metros
	 */
	private Point3f translation;
	
	public MovableObject(Point3f centerPoint) {
		this.centerPoint = centerPoint;
		rotation = 0;
		translation = new Point3f();
	}
	
	public void enterScene(Transform transfNode) {
		transform = transfNode;
		
		transform.setCenter(new float[]{centerPoint.x, centerPoint.y, centerPoint.z});
		Vector3f normal =  over.getNormal();
		transform.setRotation( new float[]{normal.x, normal.y, normal.z, rotation});
		transform.setTranslation(new float[]{translation.x, translation.y, translation.z});
	}
	
	public void reset(Surface over) {
		this.over = over;
		rotation = 0;
		translation.set(0, 0, 0);
		
		transform.setCenter(new float[]{centerPoint.x, centerPoint.y, centerPoint.z});
		Vector3f normal = over.getNormal();
		transform.setRotation(new float[]{normal.x, normal.y, normal.z, 0});
		transform.setTranslation(new float[]{0, 0, 0});
	}

	public void rotate(float angle) {
		float rads = (float) (angle * (Math.PI / 180.0));
		float[] rot = new float[4];
		transform.getRotation(rot);
		rot[3] += rads;
		transform.setRotation(rot);
		
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
		transform.getTranslation(t);
		t[0] += directionVector.x;
		t[1] += directionVector.y;
		t[2] += directionVector.z;
		transform.setTranslation(t);
		
		translation.add(directionVector);
	}
}