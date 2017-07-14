package gui;

import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import java.awt.Insets;

class WaitDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel waitText = null;
	private JProgressBar waitBar = null;
	
	private String message;

	/**
	 * @param owner
	 */
	public WaitDialog(Frame owner, String message) {
		super(owner);
		this.message = message;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(319, 131);
		this.setTitle("Please wait...");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridy = 0;
			waitText = new JLabel();
			waitText.setText("<HTML>" + message + "</HTML>");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			jContentPane.add(waitText, gridBagConstraints);
			jContentPane.add(getWaitBar(), gridBagConstraints1);
		}
		return jContentPane;
	}

	/**
	 * This method initializes waitBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getWaitBar() {
		if (waitBar == null) {
			waitBar = new JProgressBar();
			waitBar.setPreferredSize(new Dimension(146, 25));
			waitBar.setIndeterminate(true);
			waitBar.setString(null);
		}
		return waitBar;
	}

}  //  @jve:decl-index=0:visual-constraint="82,107"
