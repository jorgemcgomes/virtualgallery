package modules.art3d;

import core.module.AbstractSceneObject;

abstract class Artwork3D extends AbstractSceneObject {

	private transient Artwork3DInfo info;
	private int artID;
	
	Artwork3D(Artwork3DInfo info) {
		this.info = info;
		this.artID = info.getID();
	}
	
	int getArtID() {
		return artID;
	}

	void setInfo(Artwork3DInfo info) {
		this.info = info;
	}
	
	Artwork3DInfo getArtworkInfo() {
		return info;
	}
}
