package modules.division;

import gui.ModuleGUI;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JPanel;

import core.module.ObjectPlacingMode;
import core.module.OperationModule.VGModule;

@VGModule
public class DivisionModule extends ObjectPlacingMode<Division, DivisionSelected> {
	
	private float length, width, heigth;
	private Color chosenColor;
	private ModuleGUI gui;
	
	
	@Override
	protected Division createObject() {
		((PlacementPanel) gui.getLeftPanel()).updateSettings();
		return new Division(heigth, width, length, chosenColor);
	}

	@Override
	protected void objectDeselected(Division object) {
		this.notifyEvent(new DivisionSelected(object, null));
	}

	@Override
	protected void objectSelected(Division object) {
		this.notifyEvent(new DivisionSelected(object, object));
	}

	void setDimensions(float w, float l, float h) {
		length = l;
		width = w;
		heigth = h;
	}
	
	void setColor(Color c) {
		chosenColor = c;
	}
	
	@Override
	public void activate() {
		super.activate();
		for(Division d : super.objects) {
			d.getSurfaces().deactivateSensors(super.pListener);
			d.hideSurfaces();
		}
	}
	
	@Override
	public void terminate() {
		super.terminate();
		for(Division d : super.objects) {
			d.showSurfaces();
		}
	}

	@Override
	public Serializable getStatus() {
		ArrayList<Division> l = new ArrayList<Division>(objects);
		return l;
	}

	@Override
	public void restoreStatus(Serializable status) {
		ArrayList<Division> l = (ArrayList<Division>) status;
		objects.addAll(l);
		for(Division d : objects)
			d.enterScene(modeGroup);
	}
	
	@Override
	public ModuleGUI getGUI() {
		if(gui == null) {
			gui = new ModuleGUI("Blocks");
			gui.setLeftPanel(new PlacementPanel(this));
			gui.setLeftTitle("Place block");
			gui.setRightPanel(new ObjectPanel(this));
			gui.setRightTitle("Move block");
		}
		return gui;
	}
}
