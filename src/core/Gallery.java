package core;

import gui.GUIController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.web3d.x3d.sai.BrowserFactory;
import org.web3d.x3d.sai.InvalidBrowserException;
import org.web3d.x3d.sai.InvalidURLException;
import org.web3d.x3d.sai.InvalidX3DException;
import org.web3d.x3d.sai.X3DComponent;
import org.web3d.x3d.sai.X3DScene;
import org.xj3d.sai.Xj3DBrowser;
import org.xml.sax.SAXException;

import util.EventNotifier;
import core.module.OperationModule;
import core.surface.SurfaceModule;
import core.surface.filters.FilterSet;
import database.DBConnection;

/**
 * FACADE CONTROLLER
 * @author ark
 * -Djava.library.path="lib/bin" -Xmx512m
 */
public class Gallery extends EventNotifier<StatusEvent> {

	public static final String RESOURCE_DIR = "resources";

	// a ser inicializados aquando de um novo projecto ou carregamento de um
	private static X3DScene scene;
	private ProjectConfiguration conf;

	// inicializado sempre juntamente com o objecto gallery
	private ProjectManager projManager;
	private Xj3DBrowser browser;
	private X3DComponent x3dComp;
	private Xj3DBrowser nullBrowser;
	private DBConnection dbConnection;

	private OperationModule[] modules;
	private SurfaceModule surfaceModule;

	private OperationModule activeModule;

	// singleton
	private static Gallery instance;

	public static Gallery getInstance() {
		if(instance == null) {
			instance = new Gallery();
			instance.init();
		}
		return instance;
	}

	private Gallery() {}

	private void init() {
		try {
			dbConnection = new DBConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		surfaceModule = new SurfaceModule();
		List<OperationModule> om = ModuleLoader.loadModules();
		om.add(0, surfaceModule);
		modules = new OperationModule[om.size()];
		om.toArray(modules);

		projManager = new ProjectManager(modules);

		initializeBrowser();
	}

	private void initializeBrowser() {
		HashMap<String, Object> requestedParameters = new HashMap<String, Object>();
		requestedParameters.put("Antialiased", true);
		requestedParameters.put("Xj3D_AntialiasingQuality", "high");
		requestedParameters.put("TextureQuality", "high");
		requestedParameters.put("PrimitiveQuality", "high");
		requestedParameters.put("Xj3D_LocationShown", false);
		requestedParameters.put("Xj3D_NavbarPosition", "Bottom");

		//System.setProperty("x3d.sai.factory.class", "org.xj3d.ui.awt.browser.ogl.X3DOGLBrowserFactoryImpl");
		x3dComp = BrowserFactory.createX3DComponent(requestedParameters);
		browser = (Xj3DBrowser) x3dComp.getBrowser();

		//System.setProperty("x3d.sai.factory.class", "org.xj3d.ui.awt.browser.ogl.X3DNRBrowserFactoryImpl");
		X3DComponent x3dComp2 = BrowserFactory.createX3DComponent(null);	
		nullBrowser = (Xj3DBrowser) x3dComp2.getBrowser();
	}

	public void newProject(String name, File x3dFile, boolean fastMode, FilterSet filters) throws Exception {
		initializeScene(x3dFile);
		notifyStatus(this, StatusEvent.OK_MSG, "Original scene loaded");

		conf = new ProjectConfiguration();
		conf.setTitle(name);
		conf.setOriginalFile(x3dFile);
		conf.setFastMode(fastMode);

		for(OperationModule m : modules)
			m.reset();

		surfaceModule.detectBaseSurfaces(x3dFile, scene, fastMode, filters);

		GUIController.getInstance().setWindowTitle(name + " *");
	}

	public void loadProject(File projectFile) throws Exception {
		conf = projManager.load(projectFile);
		if(conf != null) {
			initializeScene(conf.getOriginalFile());
			if(scene != null) {
				projManager.restoreLastStatus();
				GUIController.getInstance().setWindowTitle(conf.getTitle() + 
						" - " + projectFile.getAbsolutePath());
			}
		}
	}

	public void saveProject(File blankFile) throws IOException {
		try {
			File s = projManager.save(blankFile, conf);
			GUIController.getInstance().setWindowTitle(conf.getTitle() + " - " +
					s.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public boolean hasSaveLocation() {
		return projManager.hasSaveLocation();
	}

	public void switchMode(OperationModule om) {
		if(activeModule != null)
			activeModule.terminate();

		if(om != null) {
			om.activate();
			activeModule = om;
		} else {
			activeModule.terminate();
			activeModule = null;
		}
	}

	/*public void closeProject() {
		// se ha um projecto aberto
		if(scene != null) {
			surfMode.reset();
			for(X3DNode n : scene.getRootNodes()) {
				scene.removeRootNode(n);
				n.dispose();
			}
		}
		scene = null;
		conf = null;
	}*/

	private void initializeScene(File x3dFile) throws Exception {
		try {
			String url = x3dFile.getAbsolutePath().replace('\\', '/');
			url = "file:///" + url;
			scene = browser.createX3DFromURL(new String[]{url});
			System.out.println(scene.getWorldURL());
		} catch (Exception e) {
			throw e;
		} 
		if(scene == null)
			throw new Exception("Fatal error in scene creation");
		browser.replaceWorld(scene);
	}

	public X3DScene getScene(File f) {	
		X3DScene s = null;
		try {
			String url = f.getAbsolutePath().replace('\\', '/');
			url = "file:///" + url;
			System.out.println(url);
			s = nullBrowser.createX3DFromURL(new String[]{url});
		} catch (InvalidBrowserException e) {
			e.printStackTrace();			
		} catch (InvalidX3DException e) {
			e.printStackTrace();
		}
		nullBrowser.replaceWorld(s);
		return s;
	}

	public Xj3DBrowser getNullBrowser() {
		return nullBrowser;
	}

	public JPanel getBrowserPanel() {
		return (JPanel) x3dComp.getImplementation();
	}

	public void exportX3D(File output) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(conf.getOriginalFile());
			for(OperationModule m : modules) {
				m.saveX3D(doc, output);
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			//initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(output);
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			Converter.shortenDEF(output, output);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}

	public X3DScene getScene() {
		return scene;
	}

	public Xj3DBrowser getBrowser() {
		return browser;
	}

	public SurfaceModule getSurfaceModule() {
		return surfaceModule;
	}

	public OperationModule getModuleByName(String name) {
		for(OperationModule m : modules) {
			if(m.getClass().getName().equals(name))
				return m;
		}
		return null;
	}

	public OperationModule[] getAllModules() {
		return modules;
	}

	public ProjectConfiguration getConfiguration() {
		return conf;
	}

	public boolean hasOpenProject() {
		return scene != null;
	}

	public void notifyStatus(Object src, int type, String msg) {
		StatusEvent evt = new StatusEvent(src, type, msg);
		super.notifyEvent(evt);
	}

	public DBConnection getDBConnection() {
		return dbConnection;
	}

	public void runDBManager() {
		try {
			String[] command = { "cmd.exe", "/C", "Start", "SQLiteStudio\\sqlitestudio-1.1.3.exe"};
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			notifyStatus(this, StatusEvent.ERROR_MSG, "Unnable to start DB Manager");
			e.printStackTrace();
		}
	}
}
