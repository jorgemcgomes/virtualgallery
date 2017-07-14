package core.surface;

import gui.GUIController;

import java.io.Serializable;
import java.util.*;

import javax.vecmath.*;

import core.StatusEvent;

class FlatShape implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	protected Set<Triangle> triangles; // == no clone

	protected Polygon perimeter; // no clone
	protected Polygon[] holes; // no clone

	/**
	 * Normal - Vector normalizado
	 */
	protected Vector3f normal;

	FlatShape() {
		triangles = new LinkedHashSet<Triangle>(10);
	}

	boolean addTriangle(Triangle t) {
		// first triangle to be added
		if(triangles.size() == 0) {
			triangles.add(t);

			// metodo deterministico para a seleccao do sentido da normal
			Vector3f n = (Vector3f) t.getNormal().clone();
			int val = n.hashCode();
			Vector3f n2 = (Vector3f) n.clone();
			n2.negate();
			int valNegate = n2.hashCode();
			normal = val > valNegate ? n : n2;

			return true;
			// if the triangle can be added to the shape
		} else if(isAdjacent(t)) {
			triangles.add(t);
			return true;
			// if it was not added
		} else
			return false;
	}

	boolean isAdjacent(Triangle tri) {
		if(paralelVectors(tri.getNormal(), normal)) {
			for(Triangle t : triangles)
				if(tri.isAdjacent(t))
					return true;
		}
		return false;
	}

	private boolean paralelVectors(Vector3f v1, Vector3f v2) {
		float angle = v1.angle(v2);
		return angle < 0.01 || Math.abs(angle - Math.PI) < 0.01;
	}

	public float area() {
		float sum = 0;
		for(Triangle t : triangles)
			sum += t.area();
		return sum;
	}

	public int getNumTriangles() {
		return triangles.size();
	}

	public Vector3f getNormal() {
		return normal;
	}

	void invertNormal() {
		normal.negate();
	}

	/**
	 * @return
	 */
	Point3f[] translatedPoints(float distance) {
		Point3f[] result = new Point3f[triangles.size() * 3];
		int i = 0;
		for(Triangle t : triangles) {
			result[i++] = translatePoint(t.getPoint(0), distance);
			result[i++] = translatePoint(t.getPoint(1), distance);
			result[i++] = translatePoint(t.getPoint(2), distance);
		}
		return result;
	}

	private Point3f translatePoint(Point3f p, float distance) {
		Point3f trans = new Point3f();
		trans.x = p.x + normal.x * distance;
		trans.y = p.y + normal.y * distance;
		trans.z = p.z + normal.z * distance;
		return trans;
	}

	/**
	 * Sao iguais se tiverem exactamente os mesmos triangulos e a mesma normal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		FlatShape other = (FlatShape) obj;
		return equalsFlatShape(other);
	}

	boolean equalsFlatShape(FlatShape other) {
		if (triangles == null) {
			if (other.triangles != null)
				return false;
		} else if (triangles.equals(other.triangles) &&
				normal.epsilonEquals(other.normal, 0.0001f)) {
			return true;
		}
		return false;
	}

	/**
	 * Normal nao pode entrar nas contas (pelo menos nao de forma directa) devido
	 * a imprecisao dos valores float. ia causar que shapes equals nao tivessem
	 * o mesmo hashcode
	 */
	@Override
	public int hashCode() {
		int result = 0;
		for(Triangle t : triangles) {
			result += t.hashCode();
		}

		int x = floatSignal(normal.x);
		int y = floatSignal(normal.y);
		int z = floatSignal(normal.z);
		//for every different combination of x,y,z , normalHash will never
		//have the same value
		int normalHash =  31 * (x * 15 + y * 3 + z * 19);

		return result + normalHash;
	}

	private int floatSignal(float f) {
		int res;
		if(Math.abs(f) < 0.0001)
			res = 0;
		else if(f > 0)
			res = 1;
		else
			res = -1;
		return res;
	}

	void recalculateBoundaries() {
		/*
		 * Boundaries descovery
		 */
		HashSet<Edge> bounds = new HashSet<Edge>();
		for(Triangle t : triangles) {
			Edge e = new Edge(t.getPoint(0), t.getPoint(1));
			if(bounds.contains(e))
				bounds.remove(e);
			else
				bounds.add(e);

			e = new Edge(t.getPoint(1), t.getPoint(2));
			if(bounds.contains(e))
				bounds.remove(e);
			else
				bounds.add(e);

			e = new Edge(t.getPoint(2), t.getPoint(0));
			if(bounds.contains(e))
				bounds.remove(e);
			else
				bounds.add(e);
		}

		/*
		 * Boundaries association - continuity
		 */
		LinkedList<Polygon> polygons = new LinkedList<Polygon>();
		LinkedList<Edge> edges = new LinkedList<Edge>();
		edges.addAll(bounds);

		// Uma volta para cada poligono
		while(!edges.isEmpty()) {
			Polygon polygon = new Polygon(normal);
			polygon.add(edges.getFirst().getP1());		
			polygon.add(edges.getFirst().getP2());
			edges.removeFirst();

			// Uma volta para cada scan a lista de edges
			boolean end = false;
			while(!end) {
				end = true;
				Iterator<Edge> iter = edges.iterator();
				while(iter.hasNext()) {
					boolean added = false;
					Edge e = iter.next();
					if(polygon.getFirst().equals(e.getP1())) {
						polygon.addFirst(e.getP2());
						added = true;
					}else if(polygon.getFirst().equals(e.getP2())){
						polygon.addFirst(e.getP1());
						added = true;
					}else if(polygon.getLast().equals(e.getP1())){
						polygon.addLast(e.getP2());
						added = true;
					}else if(polygon.getFirst().equals(e.getP2())){
						polygon.addLast(e.getP1());
						added = true;
					}

					if(added) {
						iter.remove();
						end = false;
					}
				}
			}

			if(polygon.size() > 2 && polygon.getFirst().equals(polygon.getLast())) {
				polygon.removeLast();
				polygons.add(polygon);
			} else {
				GUIController.getInstance().notifyStatus(this, StatusEvent.ERROR_MSG, "Invalid polygon.");
			}
		}

		/*
		 * Hole discovery
		 */
		// Ordena lista por ordem descendente de comprimentos dos poligonos
		Collections.sort(polygons, new Comparator<Polygon>() {
			@Override
			public int compare(Polygon p1, Polygon p2) {
				float l1 = p1.length();
				float l2 = p2.length();
				if(l1 > l2)
					return -1;
				else if(l2 > l1)
					return 1;
				else
					return 0;
			}		
		});

		for(Polygon candidate : polygons) {
			boolean notCandidate = false;
			for(Polygon test : polygons) {
				if(test != candidate && !candidate.contains(test)) {
					notCandidate = true;
					break;
				}
			}
			if(!notCandidate) {
				perimeter = candidate;
				break;
			}
		}
		
		polygons.remove(perimeter);
		holes = new Polygon[polygons.size()];
		polygons.toArray(holes);

		/*
		 * Debug
		 */
		System.out.println("PERIMETER: " + perimeter.toString());
		for(LinkedList<Point3f> pol : holes) {
			System.out.print("HOLE: ");
			for(Point3f p : pol) {
				System.out.print("[" + p.toString() + "] ");
			}
			System.out.println();
		}
	}

	/**
	 * referencia para o conjunto de triangulos permance igual
	 */
	@Override
	protected FlatShape clone() {
		FlatShape shape = null;
		try {
			shape = (FlatShape) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		shape.normal = (Vector3f) normal.clone();
		shape.triangles = triangles;
		return shape;
	}
}