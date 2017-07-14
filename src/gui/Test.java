package gui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.Font;

public class Test {

	private JPanel titlePanel = null;  //  @jve:decl-index=0:visual-constraint="176,125"
	private JLabel titleLabel = null;
	private JButton detach = null;

	/**
	 * This method initializes titlePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTitlePanel() {
		if (titlePanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.anchor = GridBagConstraints.EAST;
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.ipady = 0;
			gridBagConstraints1.ipadx = 0;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints.gridy = 0;
			titleLabel = new JLabel();
			titleLabel.setText("Title");
			titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
			titlePanel = new JPanel();
			titlePanel.setLayout(new GridBagLayout());
			titlePanel.setSize(new Dimension(180, 30));
			titlePanel.add(titleLabel, gridBagConstraints);
			titlePanel.add(getDetach(), gridBagConstraints1);
		}
		return titlePanel;
	}

	/**
	 * This method initializes detach	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDetach() {
		if (detach == null) {
			detach = new JButton();
			detach.setPreferredSize(new Dimension(25, 25));
		}
		return detach;
	}

}
