package gui.test;

import javax.swing.JScrollPane;

public class Scroll extends JScrollPane {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public Scroll() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);

	}

}
