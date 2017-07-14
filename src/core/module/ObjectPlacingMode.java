package core.module;

import java.io.File;
import java.util.Collection;
import java.util.EventObject;
import java.util.LinkedList;

import javax.vecmath.Point3f;

import org.w3c.dom.*;
import org.web3d.x3d.sai.SFTime;
import org.web3d.x3d.sai.X3DFieldEvent;
import org.web3d.x3d.sai.X3DFieldEventListener;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.grouping.Group;

import core.Gallery;
import core.StatusEvent;
import core.surface.Surface;
import core.surface.SurfaceModule;

import util.EventNotifier;


public abstract class ObjectPlacingMode<T extends SceneObject, E extends EventObject> 
		extends EventNotifier<E> 
		implements OperationModule, ObjectPlacingModeInterface {
	
	protected Collection<T> objects;
	protected T selected;
	protected Group modeGroup;
	private Collection<Surface> surfs;
	
	protected PlacementListener pListener;
	protected ModificationListener mListener;
	
	public static final float COARSE_TRANSLATE = 0.3f;
	public static final float COARSE_ROTATE = 30;
	public static final float FINE_TRANSLATE = 0.05f;
	public static final float FINE_ROTATE = 6;
	
	private float rotateDistance;
	private float translateDistance;
	
	
	public ObjectPlacingMode() {
		objects = new LinkedList<T>();
		translateDistance = COARSE_TRANSLATE;
		rotateDistance = COARSE_ROTATE;
	}
	
	@Override
	public void activate() {
		SurfaceModule sm = (SurfaceModule) Gallery.getInstance().getSurfaceModule();
		surfs = sm.getSelectableSurfaces();
		
		pListener = new PlacementListener();
		for(Surface s : surfs) {
			s.addSensor(pListener);
		}
		
		mListener = new ModificationListener();
		for(T o : objects) {
			o.activateSensor(mListener);
		}
	}

	@Override
	public void terminate() {
		for(Surface s : surfs) {
			s.removeSensor(pListener);
		}
		
		for(T obj : objects) {
			obj.removeSensor(mListener);
		}
	}

	@Override
	public void reset() {
		objects.clear();
		selected = null;
		
		X3DScene scene = Gallery.getInstance().getScene();
		modeGroup = (Group) scene.createNode("Group");
		scene.addRootNode(modeGroup);
	}
	
	public void translateSelected(int direction) {
		selected.translate(direction, translateDistance);
	}
	
	public void rotateSelected(int direction) {
		switch(direction) {
		case CLOCKWISE:
			selected.rotate(-rotateDistance);
			break;
		case COUNTERCLOCKWISE:
			selected.rotate(rotateDistance);
			break;
		}
	}
	
	public void resetSelected() {
		selected.reset();
	}
	
	public void deleteSelected() {
		selected.exitScene();
		objectDeselected(selected);
		objects.remove(selected);
		selected = null;
	}
	
	protected abstract T createObject();
	
	private class PlacementListener implements X3DFieldEventListener {

		@Override
		public void readableFieldChanged(X3DFieldEvent evt) {
			SFTime ttime = (SFTime) evt.getSource();
			Surface over = (Surface) ttime.getUserData();
			float[] lastHit = over.getLastHitPosition();
			Point3f point = new Point3f(lastHit[0], lastHit[1], lastHit[2]);
			System.out.println(point.toString());
			
			if(selected == null) {
				T newObj = createObject();
				if(newObj != null) {
					objects.add(newObj);
					newObj.enterScene(modeGroup);
					newObj.placeOverSurface(over, point);
					newObj.activateSensor(mListener);
					selected = newObj;
					newObj.select();
					objectSelected(newObj);
				} else {
					Gallery.getInstance().notifyStatus(this, StatusEvent.WARNING_MSG, 
							"Unnable to place object. Insufficient data.");
				}
			} else {
				selected.placeOverSurface(over, point);
			}
		}
		
	}
	
	private class ModificationListener implements X3DFieldEventListener {

		@SuppressWarnings("unchecked")
		@Override
		public void readableFieldChanged(X3DFieldEvent evt) {
			SFTime ttime = (SFTime) evt.getSource();
			T obj = (T) ttime.getUserData();
			if(selected == obj) {
				selected.deselect();
				objectDeselected(obj);
				selected = null;
			} else {
				if(selected != null)
					selected.deselect();
				selected = obj;
				objectSelected(obj);
				obj.select();
			}
		}
	}
	
	public void setCoarse() {
		rotateDistance = COARSE_ROTATE;
		translateDistance = COARSE_TRANSLATE;
	}
	
	public void setFine() {
		rotateDistance = FINE_ROTATE;
		translateDistance = FINE_TRANSLATE;
	}
	
	protected abstract void objectSelected(T object);
	
	protected abstract void objectDeselected(T object);
	
	public void saveX3D(Document doc, File output) {
		NodeList l = doc.getElementsByTagName("Scene");
		Node scene = l.item(0);

		Element group = doc.createElement("Group");
		scene.appendChild(group);
		
		Comment c = doc.createComment(this.getClass().getSimpleName());
		group.appendChild(c);

		for(T object : objects) {
			group.appendChild(object.toX3D(doc, output));
		}
	}
}
