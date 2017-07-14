package core.surface;

import java.util.EventObject;

/**
 * Evento que e lancado cada vez que ha uma alteracao nas superficies 
 * seleccionadas ou detectadas
 * @author Jorge
 *
 */
class SurfaceEvent extends EventObject {

	private int nDetected, nSelected;
	
	SurfaceEvent(Object source, int nDetected, int nSelected) {
		super(source);
		this.nDetected = nDetected;
		this.nSelected = nSelected;
	}

	int getnDetected() {
		return nDetected;
	}

	int getnSelected() {
		return nSelected;
	}

}
