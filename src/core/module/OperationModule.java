package core.module;

import gui.ModuleGUI;

import java.io.File;
import java.io.Serializable;

import java.lang.annotation.*;

import org.w3c.dom.Document;

public interface OperationModule {
	
	@Retention(RetentionPolicy.RUNTIME) 
	@Target(ElementType.TYPE)
	public @interface VGModule {}
	
	public void reset();
	public Serializable getStatus();
	public void restoreStatus(Serializable statuas);
	public void activate();
	public void terminate();
	public ModuleGUI getGUI();
	public void saveX3D(Document doc, File output);
	
}
