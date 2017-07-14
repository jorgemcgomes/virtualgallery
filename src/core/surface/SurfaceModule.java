package core.surface;

import gui.ModuleGUI;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.web3d.vrml.sav.ImportFileFormatException;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.grouping.Group;
import org.web3d.x3d.sai.shape.Appearance;
import org.web3d.x3d.sai.shape.Material;

import util.EventListener;
import util.EventNotifier;
import core.Converter;
import core.Gallery;
import core.StatusEvent;
import core.module.OperationModule;
import core.surface.filters.FilterSet;

public class SurfaceModule extends EventNotifier<SurfaceEvent> 
	implements OperationModule, SurfaceSelectionInterface {

	private LinkedList<SurfaceGroup> surfaces;
	private ManualPicker mPicker;
	private AutoPicker aPicker;
	private int selectedCount;
	
	private ModuleGUI gui;

	public SurfaceModule() {
		surfaces = new LinkedList<SurfaceGroup>();
		mPicker = new ManualPicker(this, surfaces);
		aPicker = new AutoPicker(surfaces);
	}

	public void reset() {
		surfaces.clear();
		selectedCount = 0;
		
		Surface.setInvisibleAppearance(solidColor(1, 1, 1, 1));
		Surface.setHighlightAppearance(solidColor(1, 0, 0, 0.5f));
		Surface.setOverAppearance(solidColor(1, 1, 1, 0.5f));
		Surface.setOverHighlightAppearance(solidColor(1, 0, 0, 0.3f));
	}
	
	public void detectBaseSurfaces(File f, X3DScene scene, boolean fastMode, FilterSet filters) throws Exception {
		SurfaceGroup base = detectSurfaces(f, scene, fastMode, filters);
		if(base == null)
			throw new Exception("Error in surface detection");
		surfaces.addFirst(base);
		Group g = (Group) scene.createNode("Group");
		scene.addRootNode(g);
		base.enterScene(g);
	}

	/**
	 * 
	 * @param f Original scene file
	 * @param scene Original X3DScene
	 * @param fastMode Detection through local mode
	 * @return Detected surfaces
	 */
	public SurfaceGroup detectSurfaces(File f, X3DScene scene, boolean fastMode, FilterSet filters) {
		File normalized = null;
		try {
			normalized = Converter.getNormalizedFile(f, scene);
		} catch (ImportFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(normalized == null)
			return null;
		Gallery.getInstance().notifyStatus(this, StatusEvent.OK_MSG, "Input file converted");

		X3DScene normalizedScene = Gallery.getInstance().getScene(normalized);
		if(normalizedScene == null)
			return null;
		Gallery.getInstance().notifyStatus(this, StatusEvent.OK_MSG, "Converted scene loaded");

		SurfaceDetector sd = new SurfaceDetector(normalizedScene.getRootNodes(), fastMode);
		SurfaceGroup newSurfaces = sd.findSurfaces(filters); // TODO

		Gallery.getInstance().notifyStatus(this, StatusEvent.OK_MSG, newSurfaces.size() + " surfaces found");

		return newSurfaces;
	}
	
	public SurfaceGroup detectSurfaces(Collection<Triangle> triangles) {
		SurfaceDetector sd = new SurfaceDetector(triangles);
		SurfaceGroup sg = sd.findSurfaces(new FilterSet());
		return sg;
	}

	private Appearance solidColor(float r, float g, float b, float alpha) {
		X3DScene scene = Gallery.getInstance().getScene();
		Appearance app = (Appearance) scene.createNode("Appearance");
		Material mat = (Material) scene.createNode("Material");
		mat.setDiffuseColor(new float[]{r, g, b});
		mat.setTransparency(alpha);
		app.setMaterial(mat);
		
		return app;
	}

	public void activate() {
		mPicker.spreadSensors();
		for(SurfaceGroup sg : surfaces)
			sg.highlightSelectable();
	}

	public void terminate() {
		mPicker.removeSensors();
		for(SurfaceGroup sg : surfaces)
			sg.invisible();
	}

	public Serializable getStatus() {
		return surfaces;
	}

	@SuppressWarnings("unchecked")
	public void restoreStatus(Serializable status) {
		surfaces.addAll((LinkedList<SurfaceGroup>) status);
		selectedCount = 0;
		
		Group g = (Group) Gallery.getInstance().getScene().createNode("Group");
		Gallery.getInstance().getScene().addRootNode(g);
		surfaces.getFirst().enterScene(g);
		selectedCount += surfaces.getFirst().getNSelected();
	}

	public int autoPick(FilterSet filters) {
		int n = aPicker.autoSelect(filters);
		selectedCount += n;
		SurfaceEvent evt = new SurfaceEvent(this, getNSurfaces(), selectedCount);
		notifyEvent(evt);
		return n;
	}

	public int autoReject(FilterSet filters) {
		int n = aPicker.autoDeselect(filters);
		selectedCount -= n;
		SurfaceEvent evt = new SurfaceEvent(this, getNSurfaces(), selectedCount);
		notifyEvent(evt);
		return n;
	}

	public void pickAll() {
		aPicker.selectAll();
		selectedCount = getNSurfaces();
		SurfaceEvent evt = new SurfaceEvent(this, getNSurfaces(), selectedCount);
		notifyEvent(evt);
	}

	public void clearPicks() {
		aPicker.clearSelection();
		selectedCount = 0;
		SurfaceEvent evt = new SurfaceEvent(this, getNSurfaces(), selectedCount);
		notifyEvent(evt);
	}

	void surfaceSelectedEvent(Surface s) {
		selectedCount++;
		SurfaceEvent evt = new SurfaceEvent(s, getNSurfaces(), selectedCount);
		notifyEvent(evt);
	}

	void surfaceDeselectedEvent(Surface s) {
		selectedCount--;
		SurfaceEvent evt = new SurfaceEvent(s, getNSurfaces(), selectedCount);
		notifyEvent(evt);
	}

	private int getNSurfaces() {
		int count = 0;
		for(SurfaceGroup sg : surfaces)
			count += sg.size();
		return count;
	}
	
	public Collection<Surface> getSelectableSurfaces() {
		ArrayList<Surface> allSurfs = new ArrayList<Surface>(selectedCount);
		for(SurfaceGroup sg : surfaces)
			for(Surface s : sg)
				if(s.isSelectable())
					allSurfs.add(s);
		return allSurfs;
	}

	@Override
	public void addSurfaceListener(EventListener<SurfaceEvent> listener) {
		super.addEventListener(listener);
	}
	
	public void addSurfaceGroup(SurfaceGroup sg) {
		surfaces.add(sg);
	}
	
	public void removeSurfaceGroup(SurfaceGroup sg) {
		surfaces.remove(sg);
	}

	@Override
	public void saveX3D(Document doc, File output) {
		// Nothing to do here
	}

	@Override
	public ModuleGUI getGUI() {
		if(gui == null) {
			gui = new ModuleGUI("Surface Selection");
			gui.setLeftPanel(new SurfaceModuleGUI(this));
			gui.setLeftTitle("Surface filtering");
		}
		return gui;
	}
}
