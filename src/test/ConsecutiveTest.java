package app.test;

import java.awt.BorderLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.swing.*;
import javax.vecmath.Point3f;

import org.web3d.x3d.sai.*;
import org.web3d.x3d.sai.pointingdevicesensor.TouchSensor;
import org.web3d.x3d.sai.rendering.Coordinate;
import org.web3d.x3d.sai.rendering.TriangleSet;
import org.web3d.x3d.sai.shape.Appearance;
import org.web3d.x3d.sai.shape.Material;
import org.web3d.x3d.sai.shape.Shape;



public class ConsecutiveTest extends JFrame {

	private String x3d = new String("blender.x3d");
	private X3DComponent x3dComp;
	private ExternalBrowser x3dBrowser;
	private X3DScene mainScene;
	private JComponent x3dPanel;

	public static void main(String[] args) throws InvalidBrowserException, InvalidX3DException, IOException {
		ConsecutiveTest t = new ConsecutiveTest();
		t.initBrowser();
		t.initScene();
		t.doStuff();
		
		Scanner sc = new Scanner(System.in);
		sc.next();
		
		t.initScene();
		t.cena();
		t.doStuff();
	}
	
	public void cena() {
		mainScene = (X3DScene) x3dBrowser.getExecutionContext();
	}

	// initializes browser
	public void initBrowser() throws InvalidBrowserException, InvalidX3DException, IOException {
		HashMap requestedParameters = new HashMap();
		requestedParameters.put("Antialiased", Boolean.TRUE);
		requestedParameters.put("TextureQuality", "high");
		requestedParameters.put("PrimitiveQuality", "high");
		x3dComp = BrowserFactory.createX3DComponent(requestedParameters);
		x3dPanel = (JComponent) x3dComp.getImplementation();
		JComponent contentPane = (JComponent)getContentPane();
		contentPane.add(x3dPanel, BorderLayout.CENTER);

		setSize(1024, 768);
		setVisible(true);

		x3dBrowser = x3dComp.getBrowser();
	}
	
	// initializes scene
	private void initScene() {
		try {
			mainScene = x3dBrowser.createX3DFromURL(new String[]{x3d});
			// Replace the current world with the new one
			x3dBrowser.replaceWorld(mainScene);
		}
		catch (org.web3d.vrml.lang.UnsupportedNodeException ex) {
			System.out.println(ex.getMessage());
		} catch(NullPointerException np){
			System.out.println(np.getMessage());
			np.printStackTrace();
		}
	}

	// adds a red triangle to the current scene
	private void doStuff() {
		Appearance app = (Appearance) mainScene.createNode("Appearance");
		Material mat = (Material) mainScene.createNode("Material");
		mat.setDiffuseColor(new float[]{1, 0, 0});
		app.setMaterial(mat);
		Shape s = (Shape) mainScene.createNode("Shape");
		TriangleSet ts = (TriangleSet) mainScene.createNode("TriangleSet");
		ts.setSolid(false);
		Coordinate coord = (Coordinate) mainScene.createNode("Coordinate");
		float[] flatPoints = new float[]{0,0,0, 3,3,3, 0,2,2};
		coord.setPoint(flatPoints);
		ts.setCoord(coord);
		s.setGeometry(ts);
		s.setAppearance(app);

		mainScene.addRootNode(s);
	}
}