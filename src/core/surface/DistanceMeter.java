package core.surface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.Transform3D;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import util.Utils;

import core.module.SceneObject;

class DistanceMeter {
	
	static final float INF = 1E9f;

	private Polygon perimeter;
	private Polygon[] holes;
	private List<SceneObject> objects;

	private List<Edge> surfaceEdges;
	private Transform3D transform;
	private float zCoord;

	DistanceMeter(Polygon perimeter, Polygon[] holes, List<SceneObject> objects) {
		super();
		this.perimeter = perimeter;
		this.holes = holes;
		this.objects = objects;

		init();
	}

	private void init() {
		/*
		 * Determinar a rotacao para colocar a superficie sobre plano xy
		 */
		transform = new Transform3D();
		Vector3f xyNormal = new Vector3f(0,0,1);
		float angle = xyNormal.angle(perimeter.getNormal());

		// Horizontal
		if(Utils.isZero(perimeter.getNormal().x) && Utils.isZero(perimeter.getNormal().z)) {
			Vector3f normalDup = (Vector3f) perimeter.getNormal().clone();
			transform.setRotation(new AxisAngle4f(1,0,0, angle));
			transform.transform(normalDup);
			if(!Utils.isZero(normalDup.z))
				transform.setRotation(new AxisAngle4f(1,0,0, -angle));
		// Vertical
		} else if(Utils.isZero(perimeter.getNormal().y)) {
			Vector3f normalDup = (Vector3f) perimeter.getNormal().clone();
			transform.setRotation(new AxisAngle4f(0,1,0, angle));
			transform.transform(normalDup);
			if(!Utils.isZero(normalDup.z))
				transform.setRotation(new AxisAngle4f(1,0,0, -angle));
		} else {
			transform = null;
			return;
		}
		
		Point3f test = (Point3f) perimeter.get(0).clone();
		zCoord = test.z;

		/*
		 * Determinar os segmentos da superficie
		 */
		surfaceEdges = new LinkedList<Edge>();
		surfaceEdges.addAll(perimeter.getTransformedEdges(transform));

		for(Polygon h : holes)
			surfaceEdges.addAll(h.getTransformedEdges(transform));
	}

	public Edge[] getDistances(SceneObject obj) {
		if(!objects.contains(obj) || transform == null)
			return null;

		ArrayList<Edge> edgeSet = new ArrayList<Edge>(surfaceEdges.size() + 4 * objects.size() - 1);	
		edgeSet.addAll(surfaceEdges);
		/*
		 * Obter edges dos objectos
		 */
		for(SceneObject o : objects)
			if(o != obj) {
				Polygon p = new Polygon(perimeter.getNormal());
				Point3f[] bounds = o.getCurrentBounds();
				for(int i = 0 ; i < 4 ; i++)
					p.add(bounds[i]);
				edgeSet.addAll(p.getTransformedEdges(transform));
			}

		/*
		 * Calcular box do objecto e transformar para o plano XY
		 */
		Point3f[] bounds = obj.getCurrentBounds();
		Point3f[] box = new Point3f[4];
		for(int i = 0 ; i < 4 ; i++) {
			box[i] = (Point3f) bounds[i].clone();
			transform.transform(box[i]);
		}
		
		Point3f first = null;
		for(Point3f p : box)
			if(first == null || p.y > first.y || (p.y == first.y && p.x > first.x))
				first = p;
		for(int i = 0 ; i < 3 ; i++)
			if(box[0] == first)
				break;
			else
				wind(box);
		
		Edge[] d = new Edge[4]; // up, right, down, left
		if(isAligned(box)) {
			Point3f p = getDistancePoint(new Strip(Strip.UP, box), edgeSet);
			d[0] = p != null ? new Edge(new Point3f(p.x, box[0].y, 0), p) : null;
			
			p = getDistancePoint(new Strip(Strip.RIGHT, box), edgeSet);
			d[1] = p != null ? new Edge(new Point3f(box[1].x, p.y, 0), p) : null;
			
			p = getDistancePoint(new Strip(Strip.DOWN, box), edgeSet);
			d[2] = p != null ? new Edge(new Point3f(p.x, box[2].y, 0), p) : null;
			
			p = getDistancePoint(new Strip(Strip.LEFT, box), edgeSet);
			d[3] = p != null ? new Edge(new Point3f(box[3].x, p.y, 0), p) : null;
		} else {
			Edge e = new Edge(box[0], new Point3f(box[0].x, INF, 0));
			Point3f p = getDistancePoint(e, edgeSet);
			d[0] = p != null ? new Edge(box[0], p) : null;
			
			e = new Edge(box[1], new Point3f(INF, box[1].y, 0));
			p = getDistancePoint(e, edgeSet);
			d[1] = p != null ? new Edge(box[1], p) : null;
			
			e = new Edge(box[2], new Point3f(box[2].x, -INF, 0));
			p = getDistancePoint(e, edgeSet);
			d[2] = p != null ? new Edge(box[2], p) : null;
			
			e = new Edge(box[3], new Point3f(-INF, box[3].y, 0));
			p = getDistancePoint(e, edgeSet);
			d[3] = p != null ? new Edge(box[3], p) : null;
		}
		
		Transform3D revert = new Transform3D(transform);
		revert.invert();
		for(Edge e : d) {
			if(e != null) {
				e.getP1().z = zCoord;
				e.getP2().z = zCoord;
				revert.transform(e.getP1());
				revert.transform(e.getP2());
			}
		}
		
		return d;
	}
	
	private void wind(Point3f[] vec) {
		Point3f temp = vec[vec.length - 1];
		System.arraycopy(vec, 0, vec, 1, 3);
		vec[0] = temp;
	}
	
	private Point3f getDistancePoint(Edge search, List<Edge> edges) {
		Point3f closestIntrs = null;
		for(Edge e : edges) {
			Point3f i = edgeIntersection(search, e);
			if(i != null && (closestIntrs == null || 
					e.getP1().distance(i) < e.getP1().distance(closestIntrs)))
				closestIntrs = i;
		}
		return closestIntrs;
	}
	
	private Point3f getDistancePoint(Strip strip, List<Edge> edges) {
		Point3f closest = null;
		for(Edge e : edges) {
			Point3f i = strip.closestIntersection(e);
			if(i != null && (closest == null || strip.closestToBox(closest, i) == i))
				closest = i;
		}
		return closest;
	}
	
	private boolean isAligned(Point3f[] box) {
		return box[0].x == box[1].x && box[0].y == box[3].y && 
				box[1].y == box[2].y && box[2].x == box[3].x;
				
	}

	private Point3f edgeIntersection(Edge e1, Edge e2) {
		float den = (e2.getP2().y - e2.getP1().y) * (e1.getP2().x - e1.getP1().x) -
		(e2.getP2().x - e2.getP1().x) * (e1.getP2().y - e1.getP1().y);
		float numA = (e2.getP2().x - e2.getP1().x) * (e1.getP1().y - e2.getP1().y) -
		(e2.getP2().y - e2.getP1().y) * (e1.getP1().x - e2.getP1().x);
		float numB = (e1.getP2().x - e1.getP1().x) * (e1.getP1().y - e2.getP1().y) -
		(e1.getP2().y - e1.getP1().y) * (e1.getP1().x - e2.getP1().x);

		if(Utils.isZero(den) && Utils.isZero(numA) && Utils.isZero(numB)) {
			// TODO: sao coincidentes
			return null;
		} else if(Utils.isZero(den)) {
			// sao paralelas
			return null;
		}

		float Ua = numA / den, Ub = numB / den;
		if(Ua < 0 || Ua > 1 || Ub < 0 || Ub > 1)
			return null;

		Point3f intersection = new Point3f();
		intersection.x = e1.getP1().x + Ua * (e1.getP2().x - e1.getP1().x);
		intersection.y = e1.getP1().y + Ua * (e1.getP2().y - e1.getP1().y);

		return intersection;
	}

	private class Strip {

		static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;
		private float lower, betweenLow, betweenHigh;
		private Edge lowEdge, middleEdge, highEdge;
		private Point3f middlePoint;
		private int type;

		/*
		 * 3---0
		 * |   |
		 * 2---1
		 */
		Strip(int type, Point3f[] box) {
			this.type = type;

			switch(type) {
			case RIGHT:
				lower = box[0].x;
				betweenHigh = box[0].y;
				betweenLow = box[1].y;
				middlePoint = new Point3f(box[0].x, (box[0].y + box[1].y) / 2, 0);
				highEdge = new Edge(box[0], new Point3f(INF, box[0].y, 0));
				middleEdge = new Edge(middlePoint, new Point3f(INF, middlePoint.y, 0));
				lowEdge = new Edge(box[1], new Point3f(INF, box[1].y, 0));
				break;
			case DOWN:
				lower = box[1].y;
				betweenHigh = box[1].x;
				betweenLow = box[2].x;
				middlePoint = new Point3f((box[1].x + box[2].x) / 2, box[1].y, 0);
				highEdge = new Edge(box[1], new Point3f(box[1].x, -INF, 0));
				middleEdge = new Edge(middlePoint, new Point3f(middlePoint.x, -INF, 0));
				lowEdge = new Edge(box[2], new Point3f(box[2].x, -INF, 0));
				break;
			case LEFT:
				lower = box[3].x;
				betweenHigh = box[3].y;
				betweenLow = box[2].y;
				middlePoint = new Point3f(box[3].x, (box[3].y + box[2].y) / 2, 0);
				highEdge = new Edge(box[3], new Point3f(-INF, box[3].y, 0));
				middleEdge = new Edge(middlePoint, new Point3f(-INF, middlePoint.y, 0));
				lowEdge = new Edge(box[2], new Point3f(-INF, box[2].y, 0));
				break;
			case UP:
				lower = box[0].y;
				betweenHigh = box[0].x;
				betweenLow = box[3].x;
				middlePoint = new Point3f((box[0].x + box[3].x) / 2, box[0].y, 0);
				highEdge = new Edge(box[0], new Point3f(box[0].x, INF, 0));
				middleEdge = new Edge(middlePoint, new Point3f(middlePoint.x, INF, 0));
				lowEdge = new Edge(box[3], new Point3f(box[3].x, INF, 0));
				break;
			}
		}

		Point3f closestIntersection(Edge e) {
			boolean contains1 = containsPoint(e.getP1());
			boolean contains2 = containsPoint(e.getP2());

			// Edge paralela a a bounding box
			if(aligned(e)) {
				Point3f intrsM = edgeIntersection(e, middleEdge);
				if(intrsM != null)
					return intrsM;
				return closestToBox(e.getP1(), e.getP2());
			}

			// Edge totalmente contida na faixa
			if(contains1 && contains2)
				return closestToBox(e.getP1(), e.getP2());

			Point3f intrsL = edgeIntersection(e, lowEdge);
			Point3f intrsH = edgeIntersection(e, highEdge);

			// Edge parcialmente contida na faixa com um ponto la dentro
			if(contains1 || contains2) {
				Point3f intrs = intrsL == null ? intrsH : intrsL;

				if(contains1)
					return closestToBox(e.getP1(), intrs);
				if(contains2)
					return closestToBox(e.getP2(), intrs);
			}

			// Edge parcialmente contida na faixa mas com as pontas fora
			if(intrsL != null || intrsH != null)
				return closestToBox(intrsL, intrsH);

			// Edge totalmente fora da faixa
			return null;
		}

		/**
		 * Se um dos pontos for null, devolve o outro.
		 * Devolve null se ambos forem null
		 */
		Point3f closestToBox(Point3f p1, Point3f p2) {
			if(p1 == null)
				return p2;
			if(p2 == null)
				return p1;
			switch(type) {
			case RIGHT:
				if(p1.x == p2.x)
					return Math.abs(p1.y - middlePoint.y) < Math.abs(p2.y - middlePoint.y) ? p1 : p2;
				return p1.x < p2.x ? p1 : p2;
			case LEFT:
				if(p1.x == p2.x)
					return Math.abs(p1.y - middlePoint.y) < Math.abs(p2.y - middlePoint.y) ? p1 : p2;
				return p1.x > p2.x ? p1 : p2;
			case UP:
				if(p1.y == p2.y)
					return Math.abs(p1.x - middlePoint.x) < Math.abs(p2.x - middlePoint.x) ? p1 : p2;
				return p1.y < p2.y ? p1 : p2;
			case DOWN:
				if(p1.y == p2.y)
					return Math.abs(p1.x - middlePoint.x) < Math.abs(p2.x - middlePoint.x) ? p1 : p2;
				return p1.y > p2.y ? p1 : p2;
			default:
				return null;
			}
		}	

		private boolean aligned(Edge e) {
			if(type == RIGHT || type == LEFT)
				return e.getP1().x == e.getP2().x;
			else
				return e.getP1().y == e.getP2().y;
		}

		private boolean containsPoint(Point3f p) {
			switch(type) {
			case RIGHT:
				return p.x >= lower && p.y <= betweenHigh && p.y >= betweenLow;
			case UP:
				return p.y >= lower && p.x <= betweenHigh && p.x >= betweenLow;
			case LEFT:
				return p.x <= lower && p.y <= betweenHigh && p.y >= betweenLow;
			case DOWN:
				return p.y <= lower && p.x <= betweenHigh && p.x >= betweenLow;
			default:
				return false;
			}
		}
	}
}
