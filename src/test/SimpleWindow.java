package gui.test;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimpleWindow extends JFrame {
	
	private JPanel browser;
	
	public SimpleWindow(JPanel browser) {
	    this.browser = browser;
	}
	
	public void init() {
	    setSize(1024, 768);
	    setVisible(true);
	    getContentPane().add(browser);
	}
}
