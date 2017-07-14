package modules.art3d;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.vecmath.Point3f;

import org.web3d.x3d.sai.X3DComposedGeometryNode;
import org.web3d.x3d.sai.X3DGeometryNode;
import org.web3d.x3d.sai.X3DGroupingNode;
import org.web3d.x3d.sai.X3DNode;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.rendering.Coordinate;
import org.web3d.x3d.sai.shape.Shape;

import core.Converter;
import core.Gallery;
import core.StatusEvent;

class ModelParser {
	
	static Point3f[] calculateBounds(File model) {
		Gallery.getInstance().notifyStatus(ModelParser.class, StatusEvent.WARNING_MSG, 
				"Processing model, please wait. " + model.getAbsolutePath());
		
		try {
			File normalized = Converter.getCoordFile(model);
			X3DScene objectScene = Gallery.getInstance().getScene(normalized);
			Point3f lower = new Point3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
			Point3f upper = new Point3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
			X3DNode[] nodes = objectScene.getRootNodes();

			findBounds(nodes, upper, lower);
			System.out.println("upper: " + upper.toString());
			System.out.println("lower: " + lower.toString());

			if(upper.x != Float.MIN_VALUE && upper.y != Float.MIN_VALUE && upper.z != Float.MIN_VALUE &&
					lower.x != Float.MAX_VALUE && lower.y != Float.MAX_VALUE && lower.z != Float.MAX_VALUE)
				return new Point3f[]{upper, lower};
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void findBounds(X3DNode[] nodes, Point3f upper, Point3f lower) {
		Stack<X3DNode> st = new Stack<X3DNode>();
		pushArray(st, nodes);
		
		while(!st.empty()) {
			X3DNode n = st.pop();
			if(n instanceof X3DGroupingNode) {
				X3DGroupingNode g = (X3DGroupingNode) n;
				X3DNode[] children = new X3DNode[g.getNumChildren()];
				g.getChildren(children);
				pushArray(st, children);
			} else if(n instanceof Shape) {
				Shape s = (Shape) n;
				processShape(s, upper, lower);
			}
		}
	}

	private static void pushArray(Stack<X3DNode> st, X3DNode[] nodes) {
		for(X3DNode n : nodes)
			st.push(n);
	}
	
	private static void processShape(Shape s, Point3f upper, Point3f lower) {
		X3DGeometryNode geom = (X3DGeometryNode) s.getGeometry();
		if(geom instanceof X3DComposedGeometryNode) {
			X3DComposedGeometryNode comp = (X3DComposedGeometryNode) geom;
			Coordinate coord = (Coordinate) comp.getCoord();
			float[] points = new float[coord.getNumPoint() * 3];
			coord.getPoint(points);
			for(int i = 0 ; i < coord.getNumPoint() ; i++) {
				Point3f p = new Point3f(points[i * 3], points[i*3+1], points[i*3+2]);
				lower.x = p.x < lower.x ? p.x : lower.x;
				lower.y = p.y < lower.y ? p.y : lower.y;
				lower.z = p.z < lower.z ? p.z : lower.z;
				upper.x = p.x > upper.x ? p.x : upper.x;
				upper.y = p.y > upper.y ? p.y : upper.y;
				upper.z = p.z > upper.z ? p.z : upper.z;
			}
		}
	}

}
