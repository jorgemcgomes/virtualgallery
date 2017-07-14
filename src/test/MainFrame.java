package gui.test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import app.Gallery;

/**
 * @author Jorge
 *
 */
class MainFrame extends JFrame implements UserInterface {

	private StatusBar status;
	private ModeChooser chooser;
	private JPanel switcher;
	private MainMenu menu;

	private static MainFrame instance;

	private MainFrame() {
		super("Galeria Virtual");
	}

	static MainFrame getInstance() {
		return instance;
	}

	static void initialize() {
		instance = new MainFrame();
		instance.init();
	}

	private void init() {
		super.setSize(new Dimension(800,600));
		super.setVisible(true);
		BorderLayout layout = new BorderLayout();
		super.setLayout(layout);

		// menu
		menu = new MainMenu();
		setJMenuBar(menu);

		// opcoes dependentes do contexto
		switcher = new JPanel(new CardLayout(), true);
		getContentPane().add(switcher, BorderLayout.WEST);

		// botoes dos modos
		chooser = new ModeChooser(switcher);
		getContentPane().add(chooser, BorderLayout.NORTH);

		// barra de estado
		status = new StatusBar();
		getContentPane().add(status, BorderLayout.SOUTH);

		validate();
	}
	
	boolean initializeProject(File f) {
		Gallery.getInstance().newProject(f);
		
		chooser.initializeOptions();
		chooser.validate();
		getContentPane().add(Gallery.getInstance().getBrowser(), BorderLayout.CENTER);
		validate();
		
		return true;
	}

	@Override
	public String getFile(File f) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showErrorPopup(String error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void errorStatusMessage(String message) {
		status.errorStatusMessage(message);
	}

	@Override
	public void warningStatusMessage(String message) {
		status.warningStatusMessage(message);
	}

	@Override
	public void normalStatusMessage(String message) {
		status.normalStatusMessage(message);
	}
}
