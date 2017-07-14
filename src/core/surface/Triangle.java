package core.surface;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import javax.media.j3d.Transform3D;
import javax.vecmath.*;

public class Triangle implements Serializable {

	private static final long serialVersionUID = 1L;
	private Point3f[] points;
	/**
	 * Normal - vector normalizado
	 */
	private Vector3f normal;
	
	/**
	 * Cria um triangulo
	 * Os 3 pontos tem que ser diferentes
	 * @param points Array de comprimento 3 com os pontos do triangulo
	 */
	public Triangle(Point3f[] ps) {
		//points = ps;
		points = new Point3f[3];
		points[0] = ps[0];
		points[1] = ps[1];
		points[2] = ps[2];
		
        Arrays.sort(points, new Comparator<Point3f>() {
            public int compare(Point3f a, Point3f b) {
            	int hashA = a.hashCode();
            	int hashB = b.hashCode();
                if(hashA == hashB)
                	return 0;
                else if(hashA > hashB)
                	return 1;
                else
                 	return -1;
            }
        });
        
		calculateNormal();
	}
	
	private void calculateNormal() {
		Point3f sub1 = new Point3f();
		sub1.sub(points[1], points[0]);
		Point3f sub2 = new Point3f();
		sub2.sub(points[2], points[0]);
		Vector3f edge1 = new Vector3f(sub1);
		Vector3f edge2 = new Vector3f(sub2);
		normal = new Vector3f();
		normal.cross(edge1, edge2);
		normal.normalize();
	}
	
	void transform(Transform3D transf) {
		for(Point3f p : points)
			transf.transform(p);
		transf.transform(normal);
	}
	
	public Vector3f getNormal() {
		return normal;
	}
	
	public Point3f getPoint(int i) {
		return points[i];
	}
	
	boolean isAdjacent(Triangle other) {
		int equalCount = 0;
		for(int i = 0 ; i < 3 ; i++) {
			Point3f p = other.getPoint(i);
			if(p.equals(points[0]) || p.equals(points[1]) || p.equals(points[2]))
				equalCount++;
			if(equalCount == 2) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * http://en.wikipedia.org/wiki/Triangle#Computing_the_area_of_a_triangle
	 * In three dimensions, the area of a general triangle {A = (xA, yA, zA), 
	 * B = (xB, yB, zB) and C = (xC, yC, zC)} is the Pythagorean sum of the 
	 * areas of the respective projections on the three principal planes 
	 * (i.e. x = 0, y = 0 and z = 0)
	 * @return
	 */
	float area() {
		Matrix3f m1 = new Matrix3f(points[0].x, points[1].x, points[2].x,
									points[0].y, points[1].y, points[2].y,
									1, 1, 1);
		Matrix3f m2 = new Matrix3f(points[0].y, points[1].y, points[2].y,
									points[0].z, points[1].z, points[2].z,
									1, 1, 1);
		Matrix3f m3 = new Matrix3f(points[0].z, points[1].z, points[2].z,
									points[0].x, points[1].x, points[2].x,
									1, 1, 1);
		
		double area = 0.5 * Math.sqrt(Math.pow(m1.determinant(), 2) + 
								Math.pow(m2.determinant(), 2) + Math.pow(m3.determinant(), 2));
		
		return (float) area;
	}
	
	@Override
	/**
	 * Sao iguais triangulos que tenham os mesmos vertices, independentemente
	 * da ordem
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Triangle))
			return false;
		Triangle other = (Triangle) obj;
		if(Arrays.equals(points, other.points)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Fica em conformidade com o equals
	 */
	public int hashCode() {
		int result = 0;
		result += points[0].hashCode();
		result += points[1].hashCode();
		result += points[2].hashCode();
		return result;
	}	
}
