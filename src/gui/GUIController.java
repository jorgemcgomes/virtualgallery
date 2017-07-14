package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;

import javax.swing.*;

import core.Gallery;
import core.StatusEvent;
import core.surface.filters.FilterSet;


public class GUIController {
	
	/**
	 * Singleton
	 */
	private static GUIController instance;
		
	private GUIController() {}
	
	public static GUIController getInstance() {
		if(instance == null)
			instance = new GUIController();
		return instance;
	}
	
	private StatusWindow status;
	private ModeChooser chooser;
	private JPanel leftContext;
	private JPanel rightContext;
	private MainMenu menu;
	private JFrame frame;
	private JPanel browserHolder;
	private JPanel contentPane;
	
	void createAndShowGUI() {
		frame = new JFrame();
		frame.setSize(new Dimension(1200,750));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Virtual Gallery");
		
		BorderLayout layout = new BorderLayout();
		frame.setLayout(layout);

		// menu
		menu = new MainMenu();
		frame.setJMenuBar(menu);
		
		contentPane = (JPanel) frame.getContentPane();
		
		// opcoes dependentes do contexto
		leftContext = new JPanel();
		contentPane.add(leftContext, BorderLayout.WEST);
		
		rightContext = new JPanel();
		contentPane.add(rightContext, BorderLayout.EAST);

		// botoes dos modos
		chooser = new ModeChooser(leftContext, rightContext);
		contentPane.add(chooser, BorderLayout.NORTH);

		// barra de estado
		status = new StatusWindow();
		Gallery.getInstance().addEventListener(status);

		// browser
		JPanel browser = Gallery.getInstance().getBrowserPanel();
		contentPane.add(browser, BorderLayout.CENTER);
		
		frame.validate();
	}

	// TODO: verificacao de erros
	void initializeProject(String title, File f, boolean fastMode, FilterSet filters) {
		InitProject i = new InitProject(title, f, fastMode, filters);
		i.execute();
	}
	
	private class InitProject extends LongOperation {
		
		private File f;
		private boolean fastMode;
		private String title;
		private boolean success;
		private FilterSet filters;
		
		InitProject(String title, File f, boolean fastMode, FilterSet filters) {
			super(frame, "Surface detection in progress. This may take a while deppending on the scene's size");
			this.title = title;
			this.f = f;
			this.fastMode = fastMode;
			this.success = true;
			this.filters = filters;
		}
		
		@Override
		public void done() {
			super.done();
			if(success) {
				chooser.enableOptions();
				menu.setOpenedProject();
			}
		}

		@Override
		void callOperation() {
			Gallery gal = Gallery.getInstance();
			//if(gal.hasOpenProject())
			//	closeProject();
			try {
				gal.newProject(title, f, fastMode, filters);
			} catch (Exception e) {
				e.printStackTrace();
				notifyStatus(this, StatusEvent.ERROR_MSG, "Failed to create the project: " + e.getMessage());
				success = false;
			}
		}
	}
	
	void loadProject(File toLoad) {
		Gallery gal = Gallery.getInstance();
		//if(gal.hasOpenProject())
		//	closeProject();
		try {
			gal.loadProject(toLoad);
			chooser.enableOptions();
			menu.setOpenedProject();
		} catch (Exception e) {
			e.printStackTrace();
			notifyStatus(this, StatusEvent.ERROR_MSG, "Failed to load project: " + e.getMessage());
		}
	}
	
	void closeProject() {
		chooser.disableOptions();
	}
	
	public void setWindowTitle(String t) {
		frame.setTitle(t);
	}
	
	public void notifyStatus(Object src, int type, String message) {
		status.notifyStatus(src, type, message);
	}
	
	JFrame getFrame() {
		return frame;
	}

	public void exportX3D(File export) {
		Export e = new Export(export);
		e.execute();
	}
	
	private class Export extends LongOperation {
		
		private File f;
		
		Export(File f) {
			super(frame, "Exporting to: " + f.getAbsolutePath());
			this.f = f;
		}

		@Override
		void callOperation() {
			Gallery.getInstance().exportX3D(f);
		}		
	}

	void showLogWindow() {
		status.setVisible(true);
	}
}
