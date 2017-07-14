package modules.art2d;

import gui.ModuleGUI;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import core.Gallery;
import core.module.ObjectPlacingMode;
import core.module.OperationModule.VGModule;
import database.AuthorInfo;
 
@VGModule
public class Art2DModule extends ObjectPlacingMode<Artwork2D, SelectArtEvent > {

	private Artwork2DInfo selectedArt;
	private Fetcher fetcher;
	private ModuleGUI gui;
	
	public Art2DModule() {
		super();
		try {
			fetcher = new Fetcher(Gallery.getInstance().getDBConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected Artwork2D createObject() {
		if(selectedArt != null)
			return new Artwork2D(selectedArt);
		else
			return null;
	}
	
	void selectArtwork(Artwork2DInfo art) {
		selectedArt = art;
		if(selected != null) {
			selected.deselect();
			objectDeselected(selected);
			selected = null;
		}
	}

	@Override
	protected void objectDeselected(Artwork2D object) {
		SelectArtEvent evt = new SelectArtEvent(object, null);
		notifyEvent(evt);
	}

	@Override
	protected void objectSelected(Artwork2D object) {
		SelectArtEvent evt = new SelectArtEvent(object, object.getArtworkInfo());
		notifyEvent(evt);
	}
	
	Artwork2DInfo[] getArtworks(AuthorInfo author, String type) {
		try {
			return fetcher.getArtwork2D(author, type);
		} catch (SQLException e) {
			e.printStackTrace();
			return new Artwork2DInfo[0];
		}
	}
	
	AuthorInfo[] getAllAuthors(String type) {
		try {
			return fetcher.getAuthors(type);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new AuthorInfo[0];
	}
	
	String[] getArtTypes() {
		try {
			return fetcher.getArtTypes();
		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0];
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		((PlacementPanel) gui.getLeftPanel()).refreshArtList();
	}

	@Override
	public Serializable getStatus() {
		ArrayList<Artwork2D> l = new ArrayList<Artwork2D>(super.objects);
		return l;
	}

	@Override
	public void restoreStatus(Serializable status) {
		ArrayList<Artwork2D> l = (ArrayList<Artwork2D>) status;
		objects.addAll(l);
		for(Artwork2D a : objects) {
			int id = a.getArtID();
			try {
				Artwork2DInfo info = fetcher.getArtById(id);
				a.setInfo(info);
				a.enterScene(modeGroup);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ModuleGUI getGUI() {
		if(gui == null) {
			gui = new ModuleGUI("2D Artwork");
			gui.setLeftPanel(new PlacementPanel(this));
			gui.setLeftTitle("Place artwork");
			gui.setRightPanel(new ObjectPanel(this));
			gui.setRightTitle("Move artwork");
		}
		return gui;
	}
}
