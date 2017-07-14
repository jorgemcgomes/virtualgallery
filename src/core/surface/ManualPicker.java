package core.surface;

import java.util.Collection;

import org.web3d.x3d.sai.SFTime;
import org.web3d.x3d.sai.X3DFieldEvent;
import org.web3d.x3d.sai.X3DFieldEventListener;

class ManualPicker implements X3DFieldEventListener {
	
	private Collection<SurfaceGroup> surfaces;
	private SurfaceModule surfMode;
	
	ManualPicker(SurfaceModule sm, Collection<SurfaceGroup> surfaces) {
		this.surfaces = surfaces;
		this.surfMode = sm;
	}
	
	void spreadSensors() {
		for(SurfaceGroup sg : surfaces)
			sg.activateAllSensors(this);			
	}
	
	void removeSensors() {
		for(SurfaceGroup sg : surfaces)
			sg.deactivateSensors(this);
	}

	public void readableFieldChanged(X3DFieldEvent evt) {
		SFTime ttime = (SFTime) evt.getSource();
		Surface surf = (Surface) ttime.getUserData();
		boolean selectable = surf.isSelectable();
		if(selectable) {
			surf.setSelectable(false);
			surf.invisible();	
			surfMode.surfaceDeselectedEvent(surf);
		} else {
			surf.setSelectable(true);
			surf.highlight();
			surfMode.surfaceSelectedEvent(surf);
			surf.recalculateBoundaries(); // TODO testing
		}
	}

}
