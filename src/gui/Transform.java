package gui;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.ImageIcon;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JRadioButton;

import util.Utils;

import core.module.ObjectPlacingMode;
import core.module.ObjectPlacingModeInterface;

import modules.art2d.Art2DModule;

public class Transform extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton upButton = null;
	private JButton downButton = null;
	private JButton resetButton = null;
	private JButton rightButton = null;
	private JButton leftButton = null;
	private JButton clockwiseButton = null;
	private JButton cclockwiseButton = null;
	private JButton deleteButton = null;
	private JRadioButton coarseRadio = null;
	private JRadioButton fineRadio = null;
	private JPanel radioPanel = null;

	/**
	 * This is the default constructor
	 */
	public Transform() {
		super();
		initialize();
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(getCoarseRadio());
		bg.add(getFineRadio());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.gridwidth = 3;
		gridBagConstraints41.anchor = GridBagConstraints.WEST;
		gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints41.gridy = 2;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.gridwidth = 3;
		gridBagConstraints7.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints7.gridy = 6;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.gridy = 3;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 2;
		gridBagConstraints5.gridy = 3;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 4;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 2;
		gridBagConstraints3.gridy = 4;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.ipadx = 0;
		gridBagConstraints2.ipady = 0;
		gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints2.gridy = 4;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 5;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		this.setLayout(new GridBagLayout());
		this.setBounds(new Rectangle(0, 0, 124, 193));
		this.add(getUpButton(), gridBagConstraints);
		this.add(getDownButton(), gridBagConstraints1);
		this.add(getResetButton(), gridBagConstraints2);
		this.add(getRightButton(), gridBagConstraints3);
		this.add(getLeftButton(), gridBagConstraints4);
		this.add(getClockwiseButton(), gridBagConstraints5);
		this.add(getCclockwiseButton(), gridBagConstraints6);
		this.add(getDeleteButton(), gridBagConstraints7);
		this.add(getRadioPanel(), gridBagConstraints41);
	}

	/**
	 * This method initializes upButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getUpButton() {
		if (upButton == null) {
			upButton = new JButton();
			upButton.setPreferredSize(new Dimension(40, 40));
			upButton.setMnemonic(KeyEvent.VK_UP);
			upButton.setIcon(new ImageIcon("images/up_arrow.png"));
		}
		return upButton;
	}

	/**
	 * This method initializes downButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getDownButton() {
		if (downButton == null) {
			downButton = new JButton();
			downButton.setPreferredSize(new Dimension(40, 40));
			downButton.setMnemonic(KeyEvent.VK_DOWN);
			downButton.setIcon(new ImageIcon("images/down_arrow.png"));
		}
		return downButton;
	}

	/**
	 * This method initializes resetButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getResetButton() {
		if (resetButton == null) {
			resetButton = new JButton();
			resetButton.setPreferredSize(new Dimension(40, 40));
			resetButton.setIcon(new ImageIcon("images/reset_arrow.png"));
		}
		return resetButton;
	}

	/**
	 * This method initializes rightButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getRightButton() {
		if (rightButton == null) {
			rightButton = new JButton();
			rightButton.setPreferredSize(new Dimension(40, 40));
			rightButton.setMnemonic(KeyEvent.VK_RIGHT);
			rightButton.setIcon(new ImageIcon("images/right_arrow.png"));
		}
		return rightButton;
	}

	/**
	 * This method initializes leftButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getLeftButton() {
		if (leftButton == null) {
			leftButton = new JButton();
			leftButton.setPreferredSize(new Dimension(40, 40));
			leftButton.setMnemonic(KeyEvent.VK_LEFT);
			leftButton.setIcon(new ImageIcon("images/left_arrow.png"));
		}
		return leftButton;
	}

	/**
	 * This method initializes clockwiseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getClockwiseButton() {
		if (clockwiseButton == null) {
			clockwiseButton = new JButton();
			clockwiseButton.setPreferredSize(new Dimension(40, 40));
			clockwiseButton.setIcon(new ImageIcon("images/c_arrow.png"));
		}
		return clockwiseButton;
	}

	/**
	 * This method initializes cclockwiseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getCclockwiseButton() {
		if (cclockwiseButton == null) {
			cclockwiseButton = new JButton();
			cclockwiseButton.setPreferredSize(new Dimension(40, 40));
			cclockwiseButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
			cclockwiseButton.setIcon(new ImageIcon("images/cc_arrow.png"));
			cclockwiseButton.setText("");
		}
		return cclockwiseButton;
	}

	/**
	 * This method initializes deleteButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton();
			deleteButton.setText("Delete");
			deleteButton.setMnemonic(KeyEvent.VK_DELETE);
			deleteButton.setPreferredSize(new Dimension(63, 30));
		}
		return deleteButton;
	}
	
	public void setMode(ObjectPlacingModeInterface mode) {
		ActionListener ml = new MoveListener(mode);
		getUpButton().addActionListener(ml);
		getLeftButton().addActionListener(ml);
		getDownButton().addActionListener(ml);
		getRightButton().addActionListener(ml);
		getResetButton().addActionListener(ml);
		getCclockwiseButton().addActionListener(ml);
		getClockwiseButton().addActionListener(ml);
		getDeleteButton().addActionListener(ml);
		getCoarseRadio().addActionListener(ml);
		getFineRadio().addActionListener(ml);
		
		Utils.setContainerEnabled(this, false);
	}
	
	
	
	private class MoveListener implements ActionListener {

		private ObjectPlacingModeInterface mode;
		
		MoveListener(ObjectPlacingModeInterface mode) {
			this.mode = mode;
		}
		
		@Override
		public void actionPerformed(ActionEvent evt) {
			Object o = evt.getSource();
			
			if(o == getCoarseRadio()) {
				mode.setCoarse();
			} else if(o == getFineRadio()) {
				mode.setFine();
			} else if(o == getUpButton()) {
				mode.translateSelected(ObjectPlacingModeInterface.UP);
			} else if(o == getLeftButton()) {
				mode.translateSelected(ObjectPlacingModeInterface.LEFT);
			} else if(o == getDownButton()) {
				mode.translateSelected(ObjectPlacingModeInterface.DOWN);
			} else if(o == getRightButton()) {
				mode.translateSelected(ObjectPlacingModeInterface.RIGHT);
			} else if(o == getClockwiseButton()) {
				mode.rotateSelected(ObjectPlacingModeInterface.CLOCKWISE);
			} else if(o == getCclockwiseButton()) {
				mode.rotateSelected(ObjectPlacingModeInterface.COUNTERCLOCKWISE);
			} else if(o == getResetButton()) {
				mode.resetSelected();
			} else if(o == getDeleteButton()) {
				mode.deleteSelected();
			}
			
		}
		
	}



	/**
	 * This method initializes coarseRadio	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCoarseRadio() {
		if (coarseRadio == null) {
			coarseRadio = new JRadioButton();
			coarseRadio.setText("Coarse");
			coarseRadio.setSelected(true);
		}
		return coarseRadio;
	}

	/**
	 * This method initializes fineRadio	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getFineRadio() {
		if (fineRadio == null) {
			fineRadio = new JRadioButton();
			fineRadio.setText("Fine");
		}
		return fineRadio;
	}

	/**
	 * This method initializes radioPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRadioPanel() {
		if (radioPanel == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridx = -1;
			gridBagConstraints9.gridy = -1;
			gridBagConstraints9.insets = new Insets(0, 4, 0, 0);
			gridBagConstraints9.weightx = 0.5;
			gridBagConstraints9.gridwidth = 3;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.gridx = -1;
			gridBagConstraints8.gridy = -1;
			gridBagConstraints8.weightx = 0.5;
			gridBagConstraints8.gridwidth = 3;
			radioPanel = new JPanel();
			radioPanel.setLayout(new GridBagLayout());
			radioPanel.add(getCoarseRadio(), gridBagConstraints8);
			radioPanel.add(getFineRadio(), gridBagConstraints9);
		}
		return radioPanel;
	}

}  //  @jve:decl-index=0:visual-constraint="186,191"
