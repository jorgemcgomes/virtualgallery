package core.module;

import java.io.File;
import java.io.Serializable;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point3f;
import org.w3c.dom.*;

import org.web3d.x3d.sai.X3DFieldEventListener;
import org.web3d.x3d.sai.X3DGroupingNode;

import core.surface.Surface;


public interface SceneObject extends Serializable, DirectionConstants {	
	
	Node toX3D(Document doc, File output);
	void enterScene(X3DGroupingNode group);
	void exitScene();
	void activateSensor(X3DFieldEventListener modificationListener);
	void removeSensor(X3DFieldEventListener modificationListener);
	void rotate(float angle);
	void translate(int direction, float distance);
	void reset();
	void select();
	void deselect();
	public void placeOverSurface(Surface surf, Point3f surfacePoint);
	public Point3f[] getCurrentBounds();
	public float getRotation();
	public Point3f getTranslation();
	
}
