package core.surface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

class Polygon extends LinkedList<Point3f> {
	
	private Vector3f normal;
	
	Polygon(Vector3f normal) {
		super();
		this.normal = normal;
	}

	float length() {
		float len = 0;
		Point3f last = null;
		for(Point3f p : this) {
			if(last != null)
				len += p.distance(last);
			last = p;
		}
		len += getFirst().distance(getLast());

		return len;
	}

	boolean contains(Polygon other) {
		List<Point2f> this2d = this.convertTo2D();
		List<Point2f> other2d = other.convertTo2D();

		// bounding box do poligono
		float xMin = Float.MAX_VALUE, xMax = Float.MIN_VALUE, yMin = Float.MAX_VALUE, yMax = Float.MIN_VALUE;
		for(Point2f p : this2d) {
			if(p.x < xMin)
				xMin = p.x;
			if(p.x > xMax)
				xMax = p.x;
			if(p.y < yMin)
				yMin = p.y;
			if(p.y > yMax)
				yMax = p.y;
		}

		// ver se o outro poligono esta contido na bounding box deste
		for(Point2f p : other2d)
			if(p.x < xMin || p.x > xMax || p.y < yMin || p.y > yMax)
				return false;

		// ray testing
		for(Point2f p : other2d)
			if(!pointInPolygon(p, this2d))
				return false;

		return true;
	}

	boolean pointInPolygon(Point2f test, List<Point2f> poly)  {
		int i, j;
		boolean c = false;
		for (i = 0, j = poly.size() - 1 ; i < poly.size() ; j = i++) {
			if(	((poly.get(i).y > test.y) != (poly.get(j).y > test.y)) &&
					(test.x < (poly.get(j).x - poly.get(i).x) * (test.y - poly.get(i).y) / (poly.get(j).y - poly.get(i).y) + poly.get(i).x) )
				c = !c;
		}
		return c;
	}

	private List<Point2f> convertTo2D() {
		ArrayList<Point2f> pol2d = new ArrayList<Point2f>(this.size());
		float max = Math.max(Math.abs(normal.x), Math.max(Math.abs(normal.y), Math.abs(normal.z)));
		if(normal.x == max)
			for(Point3f point : this)
				pol2d.add(new Point2f(point.y, point.z));
		else if(normal.y == max)
			for(Point3f point : this)
				pol2d.add(new Point2f(point.x, point.z));
		else
			for(Point3f point : this)
				pol2d.add(new Point2f(point.x, point.y));

		return pol2d;
	}
	
	List<Edge> getEdges() {
		List<Edge> edges = new ArrayList<Edge>(super.size());
		Point3f last = null;
		for(Point3f p : this) {
			if(last != null)
				edges.add(new Edge(last, p));
			last = p;
		}
		edges.add(new Edge(getFirst(), getLast()));
		return edges;
	}
	
	List<Edge> getTransformedEdges(Transform3D t) {
		ArrayList<Edge> edges = new ArrayList<Edge>(super.size());
		Point3f last = null;
		for(Point3f p : this) {
			Point3f pT = (Point3f) p.clone();
			t.transform(pT);
			if(last != null)
				edges.add(new Edge(last, pT));
			last = pT;
		}
		edges.add(new Edge(edges.get(0).getP1(), last));
		return edges;
	}
	
	Vector3f getNormal() {
		return normal;
	}

}
