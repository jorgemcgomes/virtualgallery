package modules.viewpoints;

import gui.ModuleGUI;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.web3d.x3d.sai.X3DScene;
import org.web3d.x3d.sai.environmentalsensor.ProximitySensor;

import core.Gallery;
import core.module.OperationModule;
import core.module.OperationModule.VGModule;

@VGModule
public class ViewpointModule implements OperationModule {
	
	private ProximitySensor sensor;
	private LinkedList<SceneViewpoint> viewpoints;
	private ModuleGUI gui;
	private ViewpointGUI panels;
	
	private SceneViewpoint selected;

	@Override
	public void reset() {
		X3DScene scene = Gallery.getInstance().getScene();
		sensor = (ProximitySensor) scene.createNode("ProximitySensor");
		sensor.setSize(new float[]{1000, 1000, 1000});
		sensor.setEnabled(false);
		scene.addRootNode(sensor);
		
		viewpoints = new LinkedList<SceneViewpoint>();
		/*List<Viewpoint> vps = getInitialViewpoints();
		for(Viewpoint v : vps) {
			viewpoints.add(new SceneViewpoint(v));
		}*/
		
		panels.updateList();
	}
	
	@Override
	public void activate() {
		sensor.setEnabled(true);
	}
	
	@Override
	public void terminate() {
		sensor.setEnabled(false);
	}

	@Override
	public Serializable getStatus() {
		return viewpoints;
	}

	@Override
	public void restoreStatus(Serializable status) {
		LinkedList<SceneViewpoint> st = (LinkedList<SceneViewpoint>) status;
		viewpoints.addAll(st);
		for(SceneViewpoint sv : viewpoints)
			sv.enterScene();
		panels.updateList();
	}

	@Override
	public void saveX3D(Document doc, File output) {
		NodeList l = doc.getElementsByTagName("Scene");
		Node scene = l.item(0);

		for(SceneViewpoint v : viewpoints) {
			scene.appendChild(v.toX3D(doc));
		}
	}
	
	void defineViewpoint(String descr) {
        float[] pos_value = new float[3];
        float[] orient_value = new float[4];

        sensor.getPosition(pos_value);
        sensor.getOrientation(orient_value);
        
        SceneViewpoint vp = new SceneViewpoint(pos_value, orient_value, descr);
        vp.enterScene();
        viewpoints.add(vp);
	}
	
	/*private List<Viewpoint> getInitialViewpoints() {
		X3DScene scene = Gallery.getInstance().getScene();
		List<Viewpoint> vps = new LinkedList<Viewpoint>();
		
		Stack<X3DNode> st = new Stack<X3DNode>();
		X3DNode[] nodes = scene.getRootNodes();
		for(X3DNode n : nodes)
			st.push(n);
		
		while(!st.empty()) {
			X3DNode n = st.pop();
			if(n instanceof X3DGroupingNode) {
				X3DGroupingNode g = (X3DGroupingNode) n;
				X3DNode[] children = new X3DNode[g.getNumChildren()];
				g.getChildren(children);
				for(X3DNode child : children)
					st.push(child);
			} else if(n instanceof Viewpoint) {
				vps.add((Viewpoint) n);
			}
		}
		return vps;
	}*/
	
	String[] getViewpoints() {
		String[] array = new String[viewpoints.size()];
		int i = 0;
		for(SceneViewpoint vp : viewpoints)
			array[i++] = vp.getDescription();
		return array;
	}
	
	void setSelected(int i) {
		selected = viewpoints.get(i);
	}
	
	void deleteSelected() {
		viewpoints.remove(selected);
		selected.exitScene();
	}
	
	void changeSelectedLocation() {
        float[] pos_value = new float[3];
        float[] orient_value = new float[4];

        sensor.getPosition(pos_value);
        sensor.getOrientation(orient_value);
        
        selected.setLocation(pos_value, orient_value);
	}
	
	void changeSelectedDescription(String s) {
		selected.setDescription(s);
	}
	
	void goToSelected() {
		selected.goTo();
	}
	
	@Override
	public ModuleGUI getGUI() {
		if(gui == null) {
			panels = new ViewpointGUI(this);
			gui = new ModuleGUI("Viewpoints");
			gui.setLeftPanel(panels.getLeft());
			gui.setLeftTitle("Define Viewpoint");
			gui.setRightPanel(panels.getRight());
			gui.setRightTitle("Modify Viewpoint");
		}
		return gui;
	}
	
	

}
