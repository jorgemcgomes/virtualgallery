package modules.division;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

class PlacementPanel extends JPanel {
	
	/**
	 * This method initializes lengthField	
	 * 	
	 * @return javax.swing.JSpinner	
	 */
	private JSpinner getLengthField() {
		if (lengthField == null) {
			lengthField = new JSpinner();
			lengthField.setPreferredSize(new Dimension(60, 25));
			lengthField.setModel(new SpinnerNumberModel(1.0, 0.0, Double.MAX_VALUE, 0.05));
		}
		return lengthField;
	}

	/**
	 * This method initializes widthField	
	 * 	
	 * @return javax.swing.JSpinner	
	 */
	private JSpinner getWidthField() {
		if (widthField == null) {
			widthField = new JSpinner();
			widthField.setPreferredSize(new Dimension(60, 25));
			widthField.setModel(new SpinnerNumberModel(1.0, 0.0, Double.MAX_VALUE, 0.05));
		}
		return widthField;
	}

	/**
	 * This method initializes heigthField	
	 * 	
	 * @return javax.swing.JSpinner	
	 */
	private JSpinner getHeigthField() {
		if (heigthField == null) {
			heigthField = new JSpinner();
			heigthField.setPreferredSize(new Dimension(60, 25));
			heigthField.setModel(new SpinnerNumberModel(1.0, 0.0, Double.MAX_VALUE, 0.05));
		}
		return heigthField;
	}

	/**
	 * This method initializes colorPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getColorPanel() {
		if (colorPanel == null) {
			colorPanel = new JPanel();
			colorPanel.setLayout(new GridBagLayout());
			colorPanel.setPreferredSize(new Dimension(60, 25));
			colorPanel.setBackground(new Color(255,255,255));
			colorPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			colorPanel.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					Color c = JColorChooser.showDialog(	PlacementPanel.this, 
							"Choose division color", colorPanel.getBackground());
					if(c != null)
						colorPanel.setBackground(c);
				}
			});
		}
		return colorPanel;
	}

	private static final long serialVersionUID = 1L;
	private JLabel dimensionLabel = null;
	private JLabel heightLabel = null;
	private JLabel widthLabel = null;
	private JLabel lengthLabel = null;
	private JSpinner lengthField = null;
	private JSpinner widthField = null;
	private JSpinner heigthField = null;
	private JLabel materialLabel = null;
	private JLabel colorLabel = null;
	private JPanel colorPanel = null;
	private DivisionModule divModule;
	private JPanel spacer = null;
	/**
	 * This is the default constructor
	 */
	public PlacementPanel(DivisionModule divMod) {
		super();
		divModule = divMod;
		initialize();
		
		updateSettings();
	}
	
	void updateSettings() {
		float w = ((Double) widthField.getValue()).floatValue();
		float l = ((Double) lengthField.getValue()).floatValue();
		float h = ((Double) heigthField.getValue()).floatValue();
		divModule.setDimensions(w, l, h);
		divModule.setColor(colorPanel.getBackground());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 1;
		gridBagConstraints13.weightx = 1.0;
		gridBagConstraints13.weighty = 1.0;
		gridBagConstraints13.gridy = 7;
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.gridx = 1;
		gridBagConstraints15.insets = new Insets(2, 0, 0, 0);
		gridBagConstraints15.anchor = GridBagConstraints.WEST;
		gridBagConstraints15.weighty = 0.0;
		gridBagConstraints15.gridy = 6;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.anchor = GridBagConstraints.WEST;
		gridBagConstraints12.gridy = 6;
		colorLabel = new JLabel();
		colorLabel.setText("Color:");
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridwidth = 2;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints11.ipady = 5;
		gridBagConstraints11.gridy = 5;
		materialLabel = new JLabel();
		materialLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		materialLabel.setText("MATERIAL");
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.gridx = 1;
		gridBagConstraints10.anchor = GridBagConstraints.WEST;
		gridBagConstraints10.insets = new Insets(2, 2, 2, 0);
		gridBagConstraints10.gridy = 2;
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 1;
		gridBagConstraints9.anchor = GridBagConstraints.WEST;
		gridBagConstraints9.insets = new Insets(2, 2, 2, 0);
		gridBagConstraints9.gridy = 3;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 1;
		gridBagConstraints8.anchor = GridBagConstraints.WEST;
		gridBagConstraints8.insets = new Insets(2, 2, 2, 0);
		gridBagConstraints8.gridy = 4;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints6.gridy = 4;
		lengthLabel = new JLabel();
		lengthLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lengthLabel.setText("Length:");
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints4.gridy = 3;
		widthLabel = new JLabel();
		widthLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		widthLabel.setText("Width:");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints2.anchor = GridBagConstraints.WEST;
		gridBagConstraints2.gridy = 2;
		heightLabel = new JLabel();
		heightLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		heightLabel.setText("Height:");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.ipady = 5;
		gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridy = 1;
		dimensionLabel = new JLabel();
		dimensionLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		dimensionLabel.setText("DIMENSIONS");
		this.setSize(180, 186);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setPreferredSize(new Dimension(118, 182));
		this.add(dimensionLabel, gridBagConstraints1);
		this.add(heightLabel, gridBagConstraints2);
		this.add(widthLabel, gridBagConstraints4);
		this.add(lengthLabel, gridBagConstraints6);
		this.add(getLengthField(), gridBagConstraints8);
		this.add(getWidthField(), gridBagConstraints9);
		this.add(getHeigthField(), gridBagConstraints10);
		this.add(materialLabel, gridBagConstraints11);
		this.add(colorLabel, gridBagConstraints12);
		this.add(getColorPanel(), gridBagConstraints15);
		this.add(getSpacer(), gridBagConstraints13);
	}

	/**
	 * This method initializes spacer	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSpacer() {
		if (spacer == null) {
			spacer = new JPanel();
			spacer.setLayout(new GridBagLayout());
		}
		return spacer;
	}

}  //  @jve:decl-index=0:visual-constraint="49,51"
