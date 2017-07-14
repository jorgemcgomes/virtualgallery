package modules.art3d;

import java.util.EventObject;

class SelectArtEvent extends EventObject {
	
	private Artwork3DInfo selectedArt;

	SelectArtEvent(Object src, Artwork3DInfo selectedArt) {
		super(src);
		this.selectedArt = selectedArt;
	}
	
	Artwork3DInfo getSelectedArt() {
		return selectedArt;
	}

}
