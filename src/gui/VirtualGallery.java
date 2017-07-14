package gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import core.Gallery;


public class VirtualGallery {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final GUIController contr = GUIController.getInstance();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                contr.createAndShowGUI();
            }
        });
		
	}

}
