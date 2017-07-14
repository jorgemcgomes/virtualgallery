package gui;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;

import javax.swing.ButtonGroup;
import javax.swing.filechooser.FileFilter;

import util.Utils;

import core.Gallery;
import core.surface.filters.AreaFilter;
import core.surface.filters.FilterSet;

import java.awt.Font;

class NewProjectWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel filePathLabel = null;
	private JTextField filePathText = null;
	private JButton chooseFile = null;
	private JLabel modeLabel = null;
	private JRadioButton fastModeRadio = null;
	private JRadioButton perfectModeRadio = null;
	private JLabel detectionFiltersLabel = null;
	private JLabel areaLabel = null;
	private JTextField areaTextField = null;
	private JButton finishButton = null;
	private JLabel projNameLabel = null;
	private JTextField titleTextField = null;
	/**
	 * @param owner
	 */
	public NewProjectWindow(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(414, 312);
		this.setTitle("New project");
		this.setContentPane(getJContentPane());
		extraInitialize();
		addListeners();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.gridy = 1;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints21.gridx = 0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.ipady = 5;
			gridBagConstraints12.gridy = 0;
			projNameLabel = new JLabel();
			projNameLabel.setText("Project name");
			projNameLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridwidth = 3;
			gridBagConstraints11.anchor = GridBagConstraints.NORTHEAST;
			gridBagConstraints11.fill = GridBagConstraints.NONE;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.gridy = 10;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.NONE;
			gridBagConstraints9.gridy = 9;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(0, 15, 0, 0);
			gridBagConstraints8.ipady = 5;
			gridBagConstraints8.gridy = 9;
			areaLabel = new JLabel();
			areaLabel.setText("Minimum surface area:");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridwidth = 3;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.ipady = 5;
			gridBagConstraints7.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints7.gridy = 8;
			detectionFiltersLabel = new JLabel();
			detectionFiltersLabel.setText("DetectionFilters");
			detectionFiltersLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridwidth = 3;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.fill = GridBagConstraints.NONE;
			gridBagConstraints6.insets = new Insets(0, 15, 0, 0);
			gridBagConstraints6.gridy = 7;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridwidth = 3;
			gridBagConstraints5.fill = GridBagConstraints.NONE;
			gridBagConstraints5.insets = new Insets(0, 15, 0, 0);
			gridBagConstraints5.gridy = 6;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridwidth = 3;
			gridBagConstraints4.fill = GridBagConstraints.NONE;
			gridBagConstraints4.ipady = 5;
			gridBagConstraints4.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints4.gridy = 5;
			modeLabel = new JLabel();
			modeLabel.setText("Surface Detection Mode");
			modeLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints2.gridy = 3;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 3;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.fill = GridBagConstraints.NONE;
			gridBagConstraints.gridwidth = 3;
			gridBagConstraints.gridheight = 1;
			gridBagConstraints.ipady = 5;
			gridBagConstraints.weighty = 0.0;
			gridBagConstraints.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints.gridy = 2;
			filePathLabel = new JLabel();
			filePathLabel.setText("X3D File");
			filePathLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			jContentPane.add(filePathLabel, gridBagConstraints);
			jContentPane.add(getFilePathText(), gridBagConstraints1);
			jContentPane.add(getChooseFile(), gridBagConstraints2);
			jContentPane.add(modeLabel, gridBagConstraints4);
			jContentPane.add(getFastModeRadio(), gridBagConstraints5);
			jContentPane.add(getPerfectModeRadio(), gridBagConstraints6);
			jContentPane.add(detectionFiltersLabel, gridBagConstraints7);
			jContentPane.add(areaLabel, gridBagConstraints8);
			jContentPane.add(getAreaTextField(), gridBagConstraints9);
			jContentPane.add(getFinishButton(), gridBagConstraints11);
			jContentPane.add(projNameLabel, gridBagConstraints12);
			jContentPane.add(getTitleTextField(), gridBagConstraints21);
		}
		return jContentPane;
	}

	/**
	 * This method initializes filePathText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getFilePathText() {
		if (filePathText == null) {
			filePathText = new JTextField();
			filePathText.setPreferredSize(new Dimension(100, 25));
			filePathText.setEditable(false);
		}
		return filePathText;
	}

	/**
	 * This method initializes chooseFile	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getChooseFile() {
		if (chooseFile == null) {
			chooseFile = new JButton();
			chooseFile.setPreferredSize(new Dimension(120, 25));
			chooseFile.setText("Choose File...");
		}
		return chooseFile;
	}

	/**
	 * This method initializes fastModeRadio	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getFastModeRadio() {
		if (fastModeRadio == null) {
			fastModeRadio = new JRadioButton();
			fastModeRadio.setText("Fast Mode (may not work in some scenes)");
			fastModeRadio.setSelected(true);
		}
		return fastModeRadio;
	}

	/**
	 * This method initializes perfectModeRadio	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getPerfectModeRadio() {
		if (perfectModeRadio == null) {
			perfectModeRadio = new JRadioButton();
			perfectModeRadio.setText("Perfect Mode (may take a while)");
			perfectModeRadio.setSelected(false);
		}
		return perfectModeRadio;
	}

	/**
	 * This method initializes areaTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getAreaTextField() {
		if (areaTextField == null) {
			areaTextField = new JTextField();
			areaTextField.setPreferredSize(new Dimension(40, 20));
			areaTextField.setText("0.3");
		}
		return areaTextField;
	}

	/**
	 * This method initializes finishButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getFinishButton() {
		if (finishButton == null) {
			finishButton = new JButton();
			finishButton.setPreferredSize(new Dimension(120, 25));
			finishButton.setText("Finish");
		}
		return finishButton;
	}

	private void extraInitialize() {
		ButtonGroup g = new ButtonGroup();
		g.add(fastModeRadio);
		g.add(perfectModeRadio);


	}

	private void addListeners() {
		NewProjListener listener = new NewProjListener();
		chooseFile.addActionListener(listener);
		finishButton.addActionListener(listener);
	}

	private class NewProjListener implements ActionListener {

		private File chosenFile;

		NewProjListener() {
			chosenFile = null;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();

			if(source == chooseFile) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new ExtensionFilter("X3D", "x3d"));
				int returnVal = fileChooser.showOpenDialog(GUIController.getInstance().getFrame());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					chosenFile = fileChooser.getSelectedFile();
					filePathText.setText(chosenFile.getAbsolutePath());
				}
			} else if(source == finishButton) {
				if(chosenFile == null)
					; // TODO avisar do problema
				else {
					NewProjectWindow.this.setVisible(false);
					NewProjectWindow.this.dispose();
					String name = titleTextField.getText().equals("") ? 
							"untitled" :
								titleTextField.getText();
					FilterSet f = new FilterSet();
					float a;
					try {
						a = Float.parseFloat(areaTextField.getText());
					} catch(NumberFormatException ex) {
						a = 0.0f;
					}
					f.add(new AreaFilter(a));
					GUIController.getInstance().initializeProject(name, chosenFile, fastModeRadio.isSelected(), f);
				}
			}
		}

	}

	/**
	 * This method initializes titleTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTitleTextField() {
		if (titleTextField == null) {
			titleTextField = new JTextField();
			titleTextField.setText("");
			titleTextField.setPreferredSize(new Dimension(6, 25));
		}
		return titleTextField;
	}

}  //  @jve:decl-index=0:visual-constraint="31,80"
