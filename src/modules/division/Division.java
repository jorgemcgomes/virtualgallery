package modules.division;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Point3f;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.web3d.x3d.sai.*;
import org.web3d.x3d.sai.grouping.Group;
import org.web3d.x3d.sai.rendering.Coordinate;
import org.web3d.x3d.sai.rendering.TriangleSet;
import org.web3d.x3d.sai.shape.Appearance;
import org.web3d.x3d.sai.shape.Material;
import org.web3d.x3d.sai.shape.Shape;

import core.Gallery;
import core.module.AbstractSceneObject;
import core.surface.Surface;
import core.surface.SurfaceGroup;
import core.surface.SurfaceModule;
import core.surface.Triangle;

class Division extends AbstractSceneObject {

	private float h, w, l;
	private Color color;
	private SurfaceGroup surfaces;
	private transient boolean dirty;
	private transient Group surfaceGroup;
	private float[] points;

	private static final int[] TRIANGLE_INDEXES = new int[] { 
		4,6,7, 4,5,6, 0,3,2, 0,1,2,
		0,1,4, 1,4,5, 1,5,2, 2,5,6, 7,3,6, 2,6,3, 7,3,0, 7,4,0,
	};

	Division(float h, float w, float l, Color c) {
		this.h = h;
		this.w = w;
		this.l = l;
		this.color = c;
		this.dirty = false;
	}

	@Override
	protected ObjectNode getNode() {
		Point3f p[] = new Point3f[8];
		p[0] = new Point3f(0,0,0);
		p[1] = new Point3f(l,0,0);
		p[2] = new Point3f(l,0,w);
		p[3] = new Point3f(0,0,w);
		p[4] = new Point3f(0,h,0);
		p[5] = new Point3f(l,h,0);
		p[6] = new Point3f(l,h,w);
		p[7] = new Point3f(0,h,w);
		List<Triangle> triangles = getTriangles(p);

		points = new float[triangles.size() * 3 * 3];
		int i = 0;
		for(Triangle t : triangles) {
			for(int j = 0 ; j < 3 ; j++) {
				Point3f point = t.getPoint(j);
				points[i++] = point.x;
				points[i++] = point.y;
				points[i++] = point.z;
			}
		}
		

		X3DScene scene = Gallery.getInstance().getScene();

		Coordinate coord = (Coordinate) scene.createNode("Coordinate");
		coord.setPoint(points);
		TriangleSet ts = (TriangleSet) scene.createNode("TriangleSet");
		ts.setCoord(coord);
		ts.setSolid(false);

		Shape s = (Shape) scene.createNode("Shape");
		s.setGeometry(ts);

		Material mat = (Material) scene.createNode("Material");
		mat.setDiffuseColor(new float[]{color.getRed()/255.0f, color.getGreen()/255.0f,
				color.getBlue()/255.0f});
		// TODO mais properiedades
		Appearance app = (Appearance) scene.createNode("Appearance");
		app.setMaterial(mat);
		s.setAppearance(app);

		return new ObjectNode(s, new Point3f(l, h, w), new Point3f(0,0,0));
	} 

	private List<Triangle> getTriangles(Point3f[] p) {
		List<Triangle> triangles = new ArrayList<Triangle>(12);
		for(int i = 0 ; i < 12 ; i++) {
			Triangle t = new Triangle(new Point3f[]{
					p[TRIANGLE_INDEXES[i * 3]],
					p[TRIANGLE_INDEXES[i * 3 + 1]],
					p[TRIANGLE_INDEXES[i * 3 + 2]]
			});
			triangles.add(t);
		}
		return triangles;
	}

	void showSurfaces() {
		X3DScene scene = Gallery.getInstance().getScene();
		if(!dirty) {
			scene.addRootNode(surfaceGroup);
		} else {
			if(surfaceGroup != null)
				scene.removeRootNode(surfaceGroup);
			initSurfaces();
		}
	}
	
	void initSurfaces() {
		X3DScene scene = Gallery.getInstance().getScene();
		SurfaceModule sm = Gallery.getInstance().getSurfaceModule();
		
		if(surfaces != null)
			sm.removeSurfaceGroup(surfaces);
		
		Point3f[] bounds = getCurrentBounds();
		
		boolean[] selected = new boolean[12];
		Arrays.fill(selected, true);
		if(surfaces != null) {
			int i = 0;
			for(Surface s : surfaces)
				selected[i++] &= s.isSelectable();
		}
		
		surfaces = sm.detectSurfaces(getTriangles(bounds));
		
		surfaceGroup = (Group) scene.createNode("Group");
		scene.addRootNode(surfaceGroup);
		surfaces.enterScene(surfaceGroup);
		int i = 0;
		for(Surface s : surfaces)
			s.setSelectable(selected[i++]);
		sm.addSurfaceGroup(surfaces);
	}

	void hideSurfaces() {
		if(surfaceGroup != null)
			Gallery.getInstance().getScene().removeRootNode(surfaceGroup);
		dirty = false;
	}
	
	@Override
	public void enterScene(X3DGroupingNode parent) {
		if(surfaceGroup == null && surfaces != null) { // restore status
			X3DScene scene = Gallery.getInstance().getScene();
			surfaceGroup = (Group) scene.createNode("Group");
			scene.addRootNode(surfaceGroup);
			surfaces.enterScene(surfaceGroup);
		}
		super.enterScene(parent);
	}

	@Override
	public void exitScene() {
		super.exitScene();
		hideSurfaces();
		Gallery.getInstance().getSurfaceModule().removeSurfaceGroup(surfaces);
	}

	SurfaceGroup getSurfaces() {
		return surfaces;
	}

	@Override
	public void placeOverSurface(Surface surf, Point3f point) {
		super.placeOverSurface(surf, point);
		dirty = true;
	}

	@Override
	public void reset() {
		super.reset();
		dirty = true;
	}

	@Override
	public void rotate(float angle) {
		super.rotate(angle);
		dirty = true;
	}

	@Override
	public void translate(int direction, float distance) {
		super.translate(direction, distance);
		dirty = true;
	}

	@Override
	protected Node getExportNode(Document doc, File output) {
		Element coord = doc.createElement("Coordinate");
		StringBuilder sb = new StringBuilder();
		for(float f : points)
			sb.append(f + " ");
		coord.setAttribute("point", sb.toString());
		
		Element ts = doc.createElement("TriangleSet");
		ts.setAttribute("solid", "false");
		ts.appendChild(coord);
		
		Element mat = doc.createElement("Material");
		mat.setAttribute("diffuseColor", color.getRed()/255.0f + " " + 
				color.getGreen()/255.0f + " " +	color.getBlue()/255.0f);
		
		Element app = doc.createElement("Appearance");
		app.appendChild(mat);
		
		Element shape = doc.createElement("Shape");
		shape.appendChild(ts);
		shape.appendChild(app);

		return shape;
	}

}
