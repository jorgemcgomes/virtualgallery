package app.test;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

public class AngleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector3f v1 = new Vector3f(0, 1, 0);
		Vector3f v2 = new Vector3f(0, 0, 1);
		System.out.println(getAngles(v1,v2).toString());
	}

	public static Tuple3f getAngles(Vector3f v1, Vector3f v2) {
		v1.normalize();
		v2.normalize();

		float angle = (float) Math.acos(v1.dot(v2));
		float mAngle = -angle;

		Vector3f rotAxis = getRotationAxis(v1, v2);

		Quat4f qAngle = new Quat4f();
		Quat4f qMAngle = new Quat4f();

		qAngle.set(new AxisAngle4f(rotAxis, angle));
		qMAngle.set(new AxisAngle4f(rotAxis, mAngle));

		return getEulerAngles(qAngle);
	}

	public static Tuple3f getEulerAngles(Quat4f quat) {
		float xx, yy, zz;
		double test = quat.x * quat.y + quat.z * quat.w;
		if (test > 0.499) { // singularity at north pole
			yy = (float) (2 * Math.atan2(quat.x,quat.w));
			zz = (float) (Math.PI/2);
			xx = 0; 
		} else if (test < -0.499) { // singularity at south pole
			yy = (float) (-2 * Math.atan2(quat.x,quat.w));
			zz = (float) (- Math.PI/2);
			xx = 0;
		} else {
			double sqx = quat.x * quat.x;
			double sqy = quat.y * quat.y;
			double sqz = quat.z * quat.z;
			yy = (float) Math.atan2(2*quat.y*quat.w-2*quat.x*quat.z , 1 - 2*sqy - 2*sqz);
			zz = (float) Math.asin(2*test);
			xx = (float) Math.atan2(2*quat.x*quat.w-2*quat.y*quat.z , 1 - 2*sqx - 2*sqz);
		}
		return new Point3f(xx, yy, zz);
	}

	public static Vector3f getRotationAxis(Vector3f v1, Vector3f v2) {
		Vector3f cross = new Vector3f();
		cross.cross(v1, v2);
		return cross;
	}

}
