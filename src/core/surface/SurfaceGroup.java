package core.surface;

import java.io.Serializable;
import java.util.LinkedHashSet;

import javax.media.j3d.Transform3D;

import org.web3d.x3d.sai.X3DFieldEventListener;
import org.web3d.x3d.sai.X3DGroupingNode;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.grouping.Transform;

import core.Gallery;


public class SurfaceGroup extends LinkedHashSet<Surface> implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient X3DGroupingNode parent;
	
	public void enterScene(X3DGroupingNode parent) {
		this.parent = parent;
		System.out.println("enter chamado");
		for(Surface s : this)
			s.enterScene(parent);
	}
	
	void activateAllSensors(X3DFieldEventListener listener) {
		for(Surface s : this)
			s.addSensor(listener);
	}
	
	public void activateSelectedSensors(X3DFieldEventListener listener) {
		for(Surface s : this)
			if(s.isSelectable())
				s.addSensor(listener);
	}
	
	public void deactivateSensors(X3DFieldEventListener listener) {
		for(Surface s : this)
			s.removeSensor(listener);
	}
	
	public void transform(Transform3D transf) {
		for(Surface s : this)
			s.transform(transf);
	}
	
	void highlightSelectable() {
		for(Surface s : this) {
			if(s.isSelectable())
				s.highlight();
		}
	}
	
	void invisible() {
		for(Surface s : this)
			s.invisible();
	}
	
	int getNSelected() {
		int count = 0;
		for(Surface s : this)
			if(s.isSelectable())
				count++;
		return count;
	}

}
