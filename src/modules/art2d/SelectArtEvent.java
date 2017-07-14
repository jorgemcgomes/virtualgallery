package modules.art2d;

import java.util.EventObject;



class SelectArtEvent extends EventObject {
	
	private Artwork2DInfo selectedArtwork;

	SelectArtEvent(Object src, Artwork2DInfo selectedArtwork) {
		super(src);
		this.selectedArtwork = selectedArtwork;
	}
	
	Artwork2DInfo getSelectedArtwork() {
		return selectedArtwork;
	}

}
