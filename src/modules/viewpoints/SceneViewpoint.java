package modules.viewpoints;

import java.io.Serializable;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3f;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.web3d.x3d.sai.SFRotation;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.navigation.Viewpoint;

import core.Gallery;

class SceneViewpoint implements Serializable {

	private float[] position;
	private float[] orientation;
	private String descr;
	
	private transient Viewpoint node;
	
	SceneViewpoint(float[] position, float[] orientation, String descr) {
		this.position = position;
		this.orientation = orientation;
		this.descr = descr;
	}
	
	SceneViewpoint(Viewpoint viewpoint) {
		position = new float[3];
		orientation = new float[4];
		SFRotation orient = (SFRotation) viewpoint.getField("orientation");
		viewpoint.getPosition(position);
		orient.getValue(orientation);
		descr = viewpoint.getDescription();
		node = viewpoint;
	}
	
	void enterScene() {
		X3DScene scene = Gallery.getInstance().getScene();
		node = (Viewpoint) scene.createNode("Viewpoint");
		node.setDescription(descr);
		node.setPosition(position);
		node.setOrientation(orientation);
		scene.addRootNode(node);
	}
	
	void exitScene() {
		X3DScene scene = Gallery.getInstance().getScene();
		scene.removeRootNode(node);
	}
	
	String getDescription() {
		return descr;
	}
	
	void setDescription(String d) {
		descr = d;
		node.setDescription(d);
	}
	
	void setLocation(float[] position, float[] orientation) {
		this.position = position;
		this.orientation = orientation;
		node.setPosition(position);
		node.setOrientation(orientation);
	}
	
	void goTo() {
		node.setBind(true);
	}

	Node toX3D(Document doc) {
		Element vp = doc.createElement("Viewpoint");
		vp.setAttribute("position", position[0] + " " + position[1] + " " + position[2]);
		vp.setAttribute("orientation", orientation[0] + " " + orientation[1] + " " +
				orientation[2] + " " + orientation[3]);
		vp.setAttribute("description", descr);
		return vp;
	}
} 