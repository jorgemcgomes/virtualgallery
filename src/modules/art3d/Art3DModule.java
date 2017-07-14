package modules.art3d;

import gui.ModuleGUI;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.vecmath.Point3f;

import org.w3c.dom.Document;

import core.Gallery;
import core.StatusEvent;
import core.module.ObjectPlacingMode;
import core.module.OperationModule.VGModule;
import database.AuthorInfo;


@VGModule
public class Art3DModule extends ObjectPlacingMode<Artwork3D, SelectArtEvent> {

	private Artwork3DInfo selectedArt;
	private Fetcher fetcher;
	private ModuleGUI gui;
	private boolean selectedDetail;
	
	public Art3DModule() {
		try {
			fetcher = new Fetcher(Gallery.getInstance().getDBConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected Artwork3D createObject() {
		if(selectedArt != null) {
			if(selectedDetail)
				return new ArtworkModel(selectedArt);
			else
				return new ArtworkClumsy(selectedArt);
		} else
			return null;
	}
	
	void selectArtwork(Artwork3DInfo art) {
		selectedArt = art;
		
		if(selected != null) {
			selected.deselect();
			objectDeselected(selected);
			selected = null;
		}
	}
	
	void selectDetail(boolean highDetail) {
		selectedDetail = highDetail;
	}

	@Override
	protected void objectDeselected(Artwork3D object) {
		SelectArtEvent evt = new SelectArtEvent(object, null);
		notifyEvent(evt);
	}

	@Override
	protected void objectSelected(Artwork3D object) {
		SelectArtEvent evt = new SelectArtEvent(object, object.getArtworkInfo());
		notifyEvent(evt);
	}
	
	Artwork3DInfo[] getArtworks(AuthorInfo author) {
		try {
			return fetcher.getArtwork3D(author);
		} catch (SQLException e) {
			e.printStackTrace();
			return new Artwork3DInfo[0];
		}
	}
	
	AuthorInfo[] getAllAuthors() {
		try {
			return fetcher.getAuthors();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new AuthorInfo[0];
	}

	@Override
	public Serializable getStatus() {
		ArrayList<Artwork3D> l = new ArrayList<Artwork3D>(objects);
		return l;
	}

	@Override
	public void restoreStatus(Serializable status) {
		ArrayList<Artwork3D> l = (ArrayList<Artwork3D>) status;
		objects.addAll(l);
		for(Artwork3D a : objects) {
			int id = a.getArtID();
			try {
				Artwork3DInfo info = fetcher.getArtById(id);
				a.setInfo(info);
				a.enterScene(modeGroup);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		((PlacementPanel) gui.getLeftPanel()).refreshArtList();
	}

	@Override
	public ModuleGUI getGUI() {
		if(gui == null) {
			gui = new ModuleGUI("3D Artwork");
			gui.setLeftPanel(new PlacementPanel(this));
			gui.setLeftTitle("Place artwork");
			gui.setRightPanel(new ObjectPanel(this));
			gui.setRightTitle("Move artwork");
		}
		return gui;
	}
}
