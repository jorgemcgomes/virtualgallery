package core.surface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.vecmath.Point3f;

import org.web3d.x3d.sai.X3DGroupingNode;
import org.web3d.x3d.sai.X3DNode;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.rendering.Coordinate;
import org.web3d.x3d.sai.rendering.TriangleSet;
import org.web3d.x3d.sai.shape.Shape;

import core.Gallery;
import core.StatusEvent;
import core.surface.filters.FilterSet;


/**
 * versao de shapes unificadas - 6ms
 * 
 * complex.x3d (22200 triangulos) -> 36s @ core I5 2.66Ghz
 * 
 * @author Jorge
 *
 */
class SurfaceDetector {

	private X3DNode[] nodes;
	private SurfaceGroup surfaces;
	private FilterSet filters;
	private LinkedHashSet<Triangle> triangles;
	private boolean indShapes;

	/**
	 * Cria um SurfaceDetector para a cena X3D dada
	 * @param scene A cena onde a deteccao vai ser feita
	 */
	SurfaceDetector(X3DNode[] nodes, boolean independentShapes) {
		this.nodes = nodes;
		this.indShapes = independentShapes;
	}

	SurfaceDetector(Collection<Triangle> tris) {
		this.triangles = new LinkedHashSet<Triangle>(tris.size());
		this.triangles.addAll(tris);
	}

	/**
	 * Encontra todas as surfaces na X3DScene em questao.<p>
	 * Uma surface eh um conjunto de triangulos adjacentes e com a mesma normal
	 * @param filters Os filtros a aplicar aquando da criacao das surfaces
	 * @return As surfaces encontradas
	 */
	SurfaceGroup findSurfaces(FilterSet filters) {		
		this.filters = filters;
		this.surfaces = new SurfaceGroup();

		long time = System.currentTimeMillis();
		if(triangles == null) {
			triangles = new LinkedHashSet<Triangle>(500);
			searchShapes(nodes);
			if(!indShapes)
				calculateSurfaces();
		} else {
			calculateSurfaces();
		}

		System.out.println("Tempo total: " + (System.currentTimeMillis() - time));

		createDoubleFaces();

		return surfaces;
	}

	/**
	 * Percorre os nos a procura de Shapes (nos filhos inclusive)
	 * @param nodes Os nos a procurar
	 */
	private void searchShapes(X3DNode[] nodes) {
		for(X3DNode node : nodes) {
			if(node instanceof X3DGroupingNode) {
				X3DGroupingNode g = (X3DGroupingNode) node;;
				X3DNode[] childs = null;
				g.getChildren(childs);
				searchShapes(childs);
			} else if(node instanceof Shape) {
				Shape s = (Shape) node;
				processShape(s);
			}
		}
	}

	/**
	 * Processa a geometria da shape, obtendo a lista de todos os triangulos
	 * @param shape A shape a processar
	 */
	private void processShape(Shape shape) {
		X3DNode geometry = shape.getGeometry();
		if(geometry instanceof TriangleSet) {
			TriangleSet set = (TriangleSet) geometry;
			Coordinate coords = (Coordinate) set.getCoord();
			float[] flatPoints = new float[coords.getNumPoint() * 3];
			coords.getPoint(flatPoints);
			generateTriangles(flatPoints);
		} else {
			String name = geometry == null ? "Undefined geometry" : geometry.getNodeName();
			Gallery.getInstance().notifyStatus(this, StatusEvent.WARNING_MSG, 
					"Unsupported geometry: " + name);
		}
	}

	/**
	 * Obtem os triangulos a partir da lista de pontos
	 * @param flatPoints Os pontos tridimensionais (flat array)
	 * @return Os triangulos encontrados
	 */
	private void generateTriangles(float[] flatPoints) {
		// obtem os pontos no espaco tridimensional
		int nPoints = flatPoints.length / 3;
		Point3f[] points = new Point3f[nPoints];
		for(int i = 0 ; i < nPoints ; i++) {
			Point3f newPoint = new Point3f();
			newPoint.x = flatPoints[i * 3];
			newPoint.y = flatPoints[i * 3 + 1];
			newPoint.z = flatPoints[i * 3 + 2];
			points[i] = newPoint;
		}

		// obtem os triangulos
		int nTriangles = nPoints / 3;
		if(indShapes)
			triangles = new LinkedHashSet<Triangle>((int) (nTriangles * 1.3), 0.8f);
		for(int i = 0 ; i < nTriangles ; i++) {
			Point3f[] triPoints = new Point3f[3];
			triPoints[0] = points[i * 3];
			triPoints[1] = points[i * 3 + 1];
			triPoints[2] = points[i * 3 + 2];
			Triangle newTriangle = new Triangle(triPoints);
			triangles.add(newTriangle);
		}

		if(indShapes)
			calculateSurfaces();	
	}

	/**
	 * A partir dos triangulos, detecta e adiciona as surfaces
	 * @param triangles Os triangulos
	 */
	private void calculateSurfaces() {
		System.out.println("Num triangles: " + triangles.size());
		while(!triangles.isEmpty()) {
			Surface s = new Surface();		
			boolean lastAdded;

			do {
				lastAdded = false;
				Iterator<Triangle> iter = triangles.iterator();
				while(iter.hasNext()) {
					Triangle t = iter.next();
					if(s.addTriangle(t)) {
						iter.remove();
						lastAdded = true;
					}
				}
			} while(lastAdded);

			if(filters.passFilters(s))
				surfaces.add(s);
		}
	}

	/**
	 * Duplica as surfaces existentes, criando para cada uma uma nova com a
	 * normal invertida
	 */
	private void createDoubleFaces() {
		ArrayList<Surface> temp = new ArrayList<Surface>(surfaces.size());
		for(Surface s : surfaces) {
			Surface newSurf = s.clone();
			newSurf.getNormal().negate();
			temp.add(newSurf);
		}
		surfaces.addAll(temp);
	}
}

/* CODIGO A FUNCIONAR (em principio) mas nao utilizado. o xj3d tem um problema
 * que nao permite ler o campo index de um IndexedTriangleSet fazendo com que
 * o processamento da shape seja impossivel

		} else if(geometry instanceof IndexedTriangleSet) {
			IndexedTriangleSet its = (IndexedTriangleSet) geometry;
			int[] index = new int[its.getNumIndex()];
			its.getIndex(index);
			System.out.println(Arrays.toString(index));

			Coordinate coords = (Coordinate) its.getCoord();
			float[] flatPoints = new float[coords.getNumPoint() * 3];
			coords.getPoint(flatPoints);
			System.out.println(Arrays.toString(flatPoints));

			LinkedList<Triangle> triangles = getTriangles(index, flatPoints);
			calculateSurfaces(triangles);
		}

	private LinkedList<Triangle> getTriangles(int[] index, float[] flatPoints) {
		// obtem os pontos no espaco tridimensional
		int nPoints = flatPoints.length / 3;
		Point3f[] points = new Point3f[nPoints];
		for(int i = 0 ; i < nPoints ; i++) {
			Point3f newPoint = new Point3f();
			newPoint.x = flatPoints[i * 3];
			newPoint.y = flatPoints[i * 3 + 1];
			newPoint.z = flatPoints[i * 3 + 2];
			points[i] = newPoint;
		}

		// obtem os triangulos
		int nTriangles = index.length / 3;
		LinkedList<Triangle> triangles = new LinkedList<Triangle>();
		for(int i = 0 ; i < nTriangles ; i++) {
			Point3f[] triPoints = new Point3f[3];
			triPoints[0] = points[index[i * 3]];
			triPoints[1] = points[index[i * 3 + 1]];
			triPoints[2] = points[index[i * 3 + 2]];
			Triangle newTriangle = new Triangle(triPoints);
			triangles.add(newTriangle);
		}

		return triangles;
	}

 */
