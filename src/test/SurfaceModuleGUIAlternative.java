package core.surface;

import gui.GUIController;
import gui.ModuleGUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import util.EventListener;
import core.StatusEvent;
import core.surface.filters.AreaFilter;
import core.surface.filters.DisjunctionFilter;
import core.surface.filters.FilterSet;
import core.surface.filters.NormalFilter;

class SurfaceModuleGUIAlternative extends JPanel implements EventListener<SurfaceEvent> {

	private static final long serialVersionUID = 1L;
	private JLabel areaTitleLabel = null;
	private JPanel areaPanel = null;
	private JLabel areaLabel = null;
	private JTextField areaTextField = null;
	private JLabel areaUnitLabel = null;
	private JLabel normalTitleLabel = null;
	private JTabbedPane normalTabs = null;
	private JPanel normalBasicPanel = null;
	private JPanel normalAdvancedPanel = null;
	private JLabel normalBasicLabel = null;
	private JCheckBox normalHorizontal = null;
	private JCheckBox normalVertical = null;
	private JCheckBox normalOblique = null;
	private JLabel normalAdvancedLabel = null;
	private JCheckBox normalXZero = null;
	private JCheckBox normalYZero = null;
	private JCheckBox normalZZero = null;
	private JCheckBox normalXNonzero = null;
	private JCheckBox normalYNonzero = null;
	private JCheckBox normalZNonzero = null;
	private JLabel optionsTitleLabel = null;
	private JRadioButton selectSurfaces = null;
	private JRadioButton rejectSurfaces = null;
	private JButton autoSelectionButton = null;
	private JButton selectAllButton = null;
	private JButton clearSelectionButton = null;
	private JLabel infosTitleLabel = null;
	private JTextField detectedInfoField = null;
	private JPanel detectedInfoPanel = null;
	private JLabel detectedInfoLabel = null;
	private JPanel selectedInfoPanel = null;
	private JTextField selectedInfoField = null;
	private JLabel selectedInfoLabel = null;
	/**
	 * This is the default constructor
	 */
	SurfaceModuleGUIAlternative(SurfaceModule sm) {
		super();
		initialize();
		surfMode = sm;
		surfMode.addSurfaceListener(this);
		
		ButtonGroup g = new ButtonGroup();
		g.add(selectSurfaces);
		g.add(rejectSurfaces);
		
		addListeners();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.gridx = 0;
		gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints15.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints15.gridy = 9;
		GridBagConstraints gridBagConstraints131 = new GridBagConstraints();
		gridBagConstraints131.gridx = 0;
		gridBagConstraints131.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints131.weighty = 1.0;
		gridBagConstraints131.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints131.gridy = 14;
		GridBagConstraints gridBagConstraints121 = new GridBagConstraints();
		gridBagConstraints121.gridx = 0;
		gridBagConstraints121.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints121.anchor = GridBagConstraints.WEST;
		gridBagConstraints121.gridy = 13;
		GridBagConstraints gridBagConstraints101 = new GridBagConstraints();
		gridBagConstraints101.gridx = 0;
		gridBagConstraints101.anchor = GridBagConstraints.WEST;
		gridBagConstraints101.ipady = 5;
		gridBagConstraints101.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints101.gridy = 11;
		infosTitleLabel = new JLabel();
		infosTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		infosTitleLabel.setText("INFORMATIONS");
		GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
		gridBagConstraints91.gridx = 0;
		gridBagConstraints91.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints91.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints91.gridy = 10;
		GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
		gridBagConstraints81.gridx = 0;
		gridBagConstraints81.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints81.gridy = 8;
		GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
		gridBagConstraints71.gridx = 0;
		gridBagConstraints71.anchor = GridBagConstraints.WEST;
		gridBagConstraints71.gridy = 7;
		GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
		gridBagConstraints61.gridx = 0;
		gridBagConstraints61.anchor = GridBagConstraints.WEST;
		gridBagConstraints61.gridy = 6;
		GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
		gridBagConstraints51.gridx = 0;
		gridBagConstraints51.anchor = GridBagConstraints.WEST;
		gridBagConstraints51.ipady = 5;
		gridBagConstraints51.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints51.gridy = 5;
		optionsTitleLabel = new JLabel();
		optionsTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		optionsTitleLabel.setText("OPTIONS");
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = GridBagConstraints.BOTH;
		gridBagConstraints4.gridy = 4;
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.weighty = 0.0;
		gridBagConstraints4.gridx = 0;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.ipady = 5;
		gridBagConstraints11.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints11.gridy = 3;
		normalTitleLabel = new JLabel();
		normalTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		normalTitleLabel.setText("NORMAL FILTER");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.gridy = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.ipady = 5;
		gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints1.gridy = 1;
		areaTitleLabel = new JLabel();
		areaTitleLabel.setText("AREA FILTER");
		areaTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		this.setSize(180, 445);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setPreferredSize(new Dimension(180, 445));
		this.add(areaTitleLabel, gridBagConstraints1);
		this.add(getAreaPanel(), gridBagConstraints2);
		this.add(normalTitleLabel, gridBagConstraints11);
		this.add(getNormalTabs(), gridBagConstraints4);
		this.add(optionsTitleLabel, gridBagConstraints51);
		this.add(getSelectSurfaces(), gridBagConstraints61);
		this.add(getRejectSurfaces(), gridBagConstraints71);
		this.add(getAutoSelectionButton(), gridBagConstraints81);
		this.add(getClearSelectionButton(), gridBagConstraints91);
		this.add(infosTitleLabel, gridBagConstraints101);
		this.add(getDetectedInfoPanel(), gridBagConstraints121);
		this.add(getSelectedInfoPanel(), gridBagConstraints131);
		this.add(getSelectAllButton(), gridBagConstraints15);
	}

	/**
	 * This method initializes areaPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAreaPanel() {
		if (areaPanel == null) {
			areaUnitLabel = new JLabel();
			areaUnitLabel.setText("m\u00B2");
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setHgap(5);
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout.setVgap(0);
			areaLabel = new JLabel();
			areaLabel.setText("Minimum area:");
			areaPanel = new JPanel();
			areaPanel.setLayout(flowLayout);
			areaPanel.add(areaLabel, null);
			areaPanel.add(getAreaTextField(), null);
			areaPanel.add(areaUnitLabel, null);
		}
		return areaPanel;
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
			areaTextField.setText("0.0");
		}
		return areaTextField;
	}

	/**
	 * This method initializes normalTabs	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getNormalTabs() {
		if (normalTabs == null) {
			normalTabs = new JTabbedPane();
			normalTabs.setPreferredSize(new Dimension(100, 120));
			normalTabs.setName("");
			normalTabs.setTabPlacement(JTabbedPane.TOP);
			normalTabs.addTab("Basic", null, getNormalBasicPanel(), null);
			normalTabs.addTab("Advanced", null, getNormalAdvancedPanel(), null);
		}
		return normalTabs;
	}

	/**
	 * This method initializes normalBasicPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNormalBasicPanel() {
		if (normalBasicPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.weighty = 1.0;
			gridBagConstraints7.gridy = 3;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 2;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridy = 0;
			normalBasicLabel = new JLabel();
			normalBasicLabel.setText("Select surface orientations:");
			normalBasicPanel = new JPanel();
			normalBasicPanel.setLayout(new GridBagLayout());
			normalBasicPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			normalBasicPanel.add(normalBasicLabel, gridBagConstraints3);
			normalBasicPanel.add(getNormalHorizontal(), gridBagConstraints5);
			normalBasicPanel.add(getNormalVertical(), gridBagConstraints6);
			normalBasicPanel.add(getNormalOblique(), gridBagConstraints7);
		}
		return normalBasicPanel;
	}

	/**
	 * This method initializes normalAdvancedPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNormalAdvancedPanel() {
		if (normalAdvancedPanel == null) {
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 1;
			gridBagConstraints17.weighty = 1.0;
			gridBagConstraints17.weightx = 0.5;
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.gridy = 3;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 1;
			gridBagConstraints16.weightx = 0.5;
			gridBagConstraints16.anchor = GridBagConstraints.WEST;
			gridBagConstraints16.gridy = 2;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 1;
			gridBagConstraints14.weightx = 0.5;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.weighty = 1.0;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.ipadx = 0;
			gridBagConstraints12.weightx = 0.5;
			gridBagConstraints12.gridy = 3;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.ipadx = 0;
			gridBagConstraints10.weightx = 0.5;
			gridBagConstraints10.gridy = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.ipadx = 0;
			gridBagConstraints9.weightx = 0.5;
			gridBagConstraints9.gridy = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.gridwidth = 2;
			gridBagConstraints8.gridy = 0;
			normalAdvancedLabel = new JLabel();
			normalAdvancedLabel.setText("<html>Normal restrictions:</html>");
			normalAdvancedPanel = new JPanel();
			normalAdvancedPanel.setLayout(new GridBagLayout());
			normalAdvancedPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			normalAdvancedPanel.add(normalAdvancedLabel, gridBagConstraints8);
			normalAdvancedPanel.add(getNormalXZero(), gridBagConstraints9);
			normalAdvancedPanel.add(getNormalYZero(), gridBagConstraints10);
			normalAdvancedPanel.add(getNormalZZero(), gridBagConstraints12);
			normalAdvancedPanel.add(getNormalXNonzero(), gridBagConstraints14);
			normalAdvancedPanel.add(getNormalYNonzero(), gridBagConstraints16);
			normalAdvancedPanel.add(getNormalZNonzero(), gridBagConstraints17);
		}
		return normalAdvancedPanel;
	}

	/**
	 * This method initializes normalHorizontal	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNormalHorizontal() {
		if (normalHorizontal == null) {
			normalHorizontal = new JCheckBox();
			normalHorizontal.setText("Horizontal");
			normalHorizontal.setSelected(true);
		}
		return normalHorizontal;
	}

	/**
	 * This method initializes normalVertical	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNormalVertical() {
		if (normalVertical == null) {
			normalVertical = new JCheckBox();
			normalVertical.setText("Vertical");
			normalVertical.setSelected(true);
		}
		return normalVertical;
	}

	/**
	 * This method initializes normalOblique	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNormalOblique() {
		if (normalOblique == null) {
			normalOblique = new JCheckBox();
			normalOblique.setText("Oblique");
			normalOblique.setSelected(true);
		}
		return normalOblique;
	}

	/**
	 * This method initializes normalXNonzero	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNormalXNonzero() {
		if (normalXNonzero == null) {
			normalXNonzero = new JCheckBox();
			normalXNonzero.setText("X \u2260 0");
		}
		return normalXNonzero;
	}


	/**
	 * This method initializes normalYNonzero	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNormalYNonzero() {
		if (normalYNonzero == null) {
			normalYNonzero = new JCheckBox();
			normalYNonzero.setText("Y \u2260 0");
		}
		return normalYNonzero;
	}


	/**
	 * This method initializes normalZNonzero	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNormalZNonzero() {
		if (normalZNonzero == null) {
			normalZNonzero = new JCheckBox();
			normalZNonzero.setText("Z \u2260 0");
		}
		return normalZNonzero;
	}


	/**
	 * This method initializes normalXZero	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNormalXZero() {
		if (normalXZero == null) {
			normalXZero = new JCheckBox();
			normalXZero.setText("X = 0");
			normalXZero.setSelected(true);
			normalXZero.setToolTipText("Allow surface normal to have X component");
		}
		return normalXZero;
	}

	/**
	 * This method initializes normalYZero	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNormalYZero() {
		if (normalYZero == null) {
			normalYZero = new JCheckBox();
			normalYZero.setText("Y = 0");
			normalYZero.setSelected(true);
			normalYZero.setToolTipText("Allow surface normal to have Y component");
		}
		return normalYZero;
	}

	/**
	 * This method initializes normalZZero	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNormalZZero() {
		if (normalZZero == null) {
			normalZZero = new JCheckBox();
			normalZZero.setText("Z = 0");
			normalZZero.setSelected(true);
			normalZZero.setToolTipText("Allow surface normal to have Z component");
		}
		return normalZZero;
	}

	/**
	 * This method initializes selectSurfaces	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getSelectSurfaces() {
		if (selectSurfaces == null) {
			selectSurfaces = new JRadioButton();
			selectSurfaces.setText("Select surfaces");
			selectSurfaces.setSelected(true);
		}
		return selectSurfaces;
	}

	/**
	 * This method initializes rejectSurfaces	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getRejectSurfaces() {
		if (rejectSurfaces == null) {
			rejectSurfaces = new JRadioButton();
			rejectSurfaces.setText("Reject surfaces");
		}
		return rejectSurfaces;
	}

	/**
	 * This method initializes autoSelectionButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAutoSelectionButton() {
		if (autoSelectionButton == null) {
			autoSelectionButton = new JButton();
			autoSelectionButton.setText("Automatic Selection");
			autoSelectionButton.setPreferredSize(new Dimension(127, 25));
		}
		return autoSelectionButton;
	}

	/**
	 * This method initializes selectAllButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSelectAllButton() {
		if (selectAllButton == null) {
			selectAllButton = new JButton();
			selectAllButton.setPreferredSize(new Dimension(103, 25));
			selectAllButton.setText("Select All");
		}
		return selectAllButton;
	}


	/**
	 * This method initializes clearSelectionButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getClearSelectionButton() {
		if (clearSelectionButton == null) {
			clearSelectionButton = new JButton();
			clearSelectionButton.setText("Clear Selection");
			clearSelectionButton.setPreferredSize(new Dimension(103, 25));
		}
		return clearSelectionButton;
	}

	/**
	 * This method initializes detectedInfoField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDetectedInfoField() {
		if (detectedInfoField == null) {
			detectedInfoField = new JTextField();
			detectedInfoField.setPreferredSize(new Dimension(50, 20));
			detectedInfoField.setEditable(false);
			detectedInfoField.setText("");
		}
		return detectedInfoField;
	}

	/**
	 * This method initializes detectedInfoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDetectedInfoPanel() {
		if (detectedInfoPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setHgap(5);
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout1.setVgap(0);
			detectedInfoLabel = new JLabel();
			detectedInfoLabel.setText("Detected surfaces");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridx = -1;
			gridBagConstraints13.gridy = -1;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.fill = GridBagConstraints.VERTICAL;
			detectedInfoPanel = new JPanel();
			detectedInfoPanel.setLayout(flowLayout1);
			detectedInfoPanel.add(getDetectedInfoField(), null);
			detectedInfoPanel.add(detectedInfoLabel, null);
		}
		return detectedInfoPanel;
	}

	/**
	 * This method initializes selectedInfoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSelectedInfoPanel() {
		if (selectedInfoPanel == null) {
			selectedInfoLabel = new JLabel();
			selectedInfoLabel.setText("Selected surfaces");
			FlowLayout flowLayout11 = new FlowLayout();
			flowLayout11.setHgap(5);
			flowLayout11.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout11.setVgap(0);
			selectedInfoPanel = new JPanel();
			selectedInfoPanel.setLayout(flowLayout11);
			selectedInfoPanel.add(getSelectedInfoField(), null);
			selectedInfoPanel.add(selectedInfoLabel, null);
		}
		return selectedInfoPanel;
	}

	/**
	 * This method initializes selectedInfoField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSelectedInfoField() {
		if (selectedInfoField == null) {
			selectedInfoField = new JTextField();
			selectedInfoField.setPreferredSize(new Dimension(50, 20));
			selectedInfoField.setEditable(false);
			selectedInfoField.setText("");
		}
		return selectedInfoField;
	}


	/**
	 *  ----------------------------------------------------------------
	 *  USER GENERATED CODE
	 *  ----------------------------------------------------------------
	 */

	private SurfaceSelectionInterface surfMode;  //  @jve:decl-index=0:

	private void addListeners() {
		Listener listener = new Listener();
		autoSelectionButton.addActionListener(listener);
		selectAllButton.addActionListener(listener);
		clearSelectionButton.addActionListener(listener);
	}

	@Override
	public void eventOccurred(SurfaceEvent e) {
		selectedInfoField.setText(Integer.toString(e.getnSelected()));
		detectedInfoField.setText(Integer.toString(e.getnDetected()));
		
		if(e.getSource() instanceof Surface) {
			Surface s = (Surface) e.getSource();
			if(s.isSelectable()) {
				String m = String.format("Surface selected. " +
						"Area: %.3f ; Normal: (%+.3f, %+.3f, %+.3f)", s.area(),
						s.getNormal().x, s.getNormal().y, s.getNormal().z);			
				GUIController.getInstance().notifyStatus(this, StatusEvent.OK_MSG, m);
			} 
		}
	}

	private class Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent evt) {
			Object source  = evt.getSource();
			if(source == autoSelectionButton) {	
				FilterSet filters = new FilterSet();
				
				// Area filter
				float a;
				try {
					a = Float.parseFloat(areaTextField.getText());
				} catch(NumberFormatException e) {
					a = 0.0f;
					areaTextField.setText("0.0");
					GUIController.getInstance().notifyStatus(this, StatusEvent.WARNING_MSG, 
							"Invalid number format, only numeric values allowed " +
							"- Area filter ignored");
				}
				if(a != 0.0f)
					filters.add(new AreaFilter(a));
				
				// Basic normal filter
				if(normalTabs.getSelectedComponent() == normalBasicPanel) {
					DisjunctionFilter disj = new DisjunctionFilter();
					if(normalHorizontal.isSelected())
						disj.addFilter(new NormalFilter(NormalFilter.HORIZONTAL));
					if(normalVertical.isSelected())
						disj.addFilter(new NormalFilter(NormalFilter.VERTICAL));
					if(normalOblique.isSelected())
						disj.addFilter(new NormalFilter(NormalFilter.OBLIQUE));
					filters.add(disj);
					
				// Advanced normal filter
				} else if(normalTabs.getSelectedComponent() == normalAdvancedPanel) {
					int xR = NormalFilter.ALL, yR = NormalFilter.ALL, zR = NormalFilter.ALL;
					if(normalXZero.isSelected() && !normalXNonzero.isSelected())
						xR = NormalFilter.ZERO;
					else if(!normalXZero.isSelected() && normalXNonzero.isSelected())
						xR = NormalFilter.NON_ZERO;
					if(normalYZero.isSelected() && !normalYNonzero.isSelected())
						yR = NormalFilter.ZERO;
					else if(!normalYZero.isSelected() && normalYNonzero.isSelected())
						yR = NormalFilter.NON_ZERO;
					if(normalZZero.isSelected() && !normalZNonzero.isSelected())
						zR = NormalFilter.ZERO;
					else if(!normalZZero.isSelected() && normalZNonzero.isSelected())
						zR = NormalFilter.NON_ZERO;
					filters.add(new NormalFilter(xR, yR, zR));
				}
				
				int nPicked = 0;
				if(selectSurfaces.isSelected())
					nPicked = surfMode.autoPick(filters);
				else if(rejectSurfaces.isSelected())
					nPicked = surfMode.autoReject(filters);
				
				if(selectSurfaces.isSelected())
					GUIController.getInstance().notifyStatus(this, StatusEvent.OK_MSG, 
							nPicked + " new surfaces selected");
				else
					GUIController.getInstance().notifyStatus(this, StatusEvent.OK_MSG,
							nPicked + " surfaces deselected");
				
			} else if(source == clearSelectionButton) {
				surfMode.clearPicks();
			} else if(source == selectAllButton) {
				surfMode.pickAll();
			}
		}

	}
	
}  //  @jve:decl-index=0:visual-constraint="11,11"
