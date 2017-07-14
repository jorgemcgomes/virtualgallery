package gui.test;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.CardLayout;

public class Teste2 extends JPanel {

	private static final long serialVersionUID = 1L;
	/**
	 * This is the default constructor
	 */
	public Teste2() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(230, 173);
		this.setLayout(new CardLayout());
	}

}  //  @jve:decl-index=0:visual-constraint="61,65"
