package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;

import util.Utils;

import core.Gallery;
import core.StatusEvent;


class MainMenu extends JMenuBar {

	private JMenuItem newProj, save, saveAs, load, export, dbManager, exit, status;
	private JMenu proj;

	MainMenu() {
		super();
		init();
	}

	private void init() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		JMenu file = new JMenu("File");
		FileListener listener = new FileListener();

		newProj = new JMenuItem("New Project");
		newProj.addActionListener(listener);
		file.add(newProj);

		save = new JMenuItem("Save");
		save.addActionListener(listener);
		save.setEnabled(false);
		file.add(save);

		saveAs = new JMenuItem("Save as...");
		saveAs.addActionListener(listener);
		saveAs.setEnabled(false);
		file.add(saveAs);

		load = new JMenuItem("Open");
		load.addActionListener(listener);
		file.add(load);

		exit = new JMenuItem("Exit");
		exit.addActionListener(listener);
		file.add(exit);

		add(file);

		proj = new JMenu("Project");
		export = new JMenuItem("Export X3D");
		export.addActionListener(listener);
		proj.add(export);
		proj.setEnabled(false);

		add(proj);
		
		

		JMenu db = new JMenu("Database");

		dbManager = new JMenuItem("Open Manager");
		dbManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Gallery.getInstance().runDBManager();
			}
		});
		db.add(dbManager);

		add(db);
		
		JMenu help = new JMenu("Help");
		status = new JMenuItem("Log window");
		status.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUIController.getInstance().showLogWindow();
			}
		});
		help.add(status);
		
		add(help);
	}

	void setOpenedProject() {
		proj.setEnabled(true);
		save.setEnabled(true);
		saveAs.setEnabled(true);
		newProj.setEnabled(false);
		load.setEnabled(false);
	}

	private class FileListener implements ActionListener {

		private JFrame frame;

		FileListener() {
			frame = GUIController.getInstance().getFrame();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();

			if(source == newProj) {
				NewProjectWindow win = new NewProjectWindow(frame);
				win.setVisible(true);
				win.setLocationRelativeTo(frame);
			} else if(source == save) {
				try {
					if(Gallery.getInstance().hasSaveLocation())
						Gallery.getInstance().saveProject(null);
					else
						saveAs();
				} catch(IOException ex) {
					GUIController.getInstance().notifyStatus(this, StatusEvent.ERROR_MSG, "Error saving project: " + ex.getMessage());
				}
			} else if(source == saveAs) {
				try {
					saveAs();
				} catch (IOException ex) {
					GUIController.getInstance().notifyStatus(this, StatusEvent.ERROR_MSG, "Error saving project: " + ex.getMessage());
				}
			} else if(source == load) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new ExtensionFilter("Project Files", "vgp"));
				int returnVal = fileChooser.showOpenDialog(frame);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File toLoad = fileChooser.getSelectedFile();
					GUIController.getInstance().loadProject(toLoad);
				}
			} else if(source == export) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new ExtensionFilter("X3D", "x3d"));
				int returnVal = fileChooser.showSaveDialog(frame);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File export = fileChooser.getSelectedFile();
					if(!ExtensionFilter.getExtension(export).equals("x3d")) {
						export = new File(export.getAbsolutePath() + ".x3d");
					}
					GUIController.getInstance().exportX3D(export);
				}
			} else if(source == exit) {
				System.exit(0);
			}
		}

		private void saveAs() throws IOException {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.addChoosableFileFilter(new ExtensionFilter("Project Files", "vgp"));
			int returnVal = fileChooser.showSaveDialog(frame);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File f = fileChooser.getSelectedFile();
				if(!ExtensionFilter.getExtension(f).equals("vgp")) {
					f = new File(f.getAbsolutePath() + ".vgp");
				}
				Gallery.getInstance().saveProject(f);
			}
		}
	}
}