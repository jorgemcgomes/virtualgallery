package core.surface;

import javax.vecmath.Point3f;

public class Edge {
	
	private Point3f p1, p2;
	
	Edge(Point3f p1, Point3f p2) {	
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Point3f getP1() {
		return p1;
	}
	
	public Point3f getP2() {
		return p2;
	}
	
	public float getDistance() {
		return p1.distance(p2);
	}

	@Override
	public int hashCode() {
		int result = 0;
		result += ((p1 == null) ? 0 : p1.hashCode());
		result += ((p2 == null) ? 0 : p2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		Edge other = (Edge) obj;
		if((p1.equals(other.p1) && p2.equals(other.p2)) ||
			p1.equals(other.p2) && p2.equals(other.p1))
			return true;
			
		return false;
	}

}
