package modules.division;

import gui.Transform;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.EventListener;
import util.Utils;
import java.awt.Dimension;

class ObjectPanel extends JPanel implements EventListener<DivisionSelected> {
	
	private static final long serialVersionUID = 1L;
	private DivisionModule divModule;
	private JLabel transformLabel = null;
	private Transform transform = null;
	/**
	 * This is the default constructor
	 */
	public ObjectPanel(DivisionModule divMod) {
		super();
		divModule = divMod;
		initialize();
		
		divMod.addEventListener(this);
		transform.setMode(divModule);
		
		Utils.setContainerEnabled(this, false);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.fill = GridBagConstraints.NONE;
		gridBagConstraints41.gridwidth = 2;
		gridBagConstraints41.anchor = GridBagConstraints.NORTH;
		gridBagConstraints41.weighty = 1.0;
		gridBagConstraints41.gridy = 8;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.gridwidth = 2;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.ipady = 5;
		gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.gridy = 7;
		transformLabel = new JLabel();
		transformLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		transformLabel.setText("TRANSFORM");
		this.setSize(180, 235);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(transformLabel, gridBagConstraints3);
		this.add(getTransform(), gridBagConstraints41);
	}

	/**
	 * This method initializes transform	
	 * 	
	 * @return gui.Transform	
	 */
	private Transform getTransform() {
		if (transform == null) {
			transform = new Transform();
		}
		return transform;
	}

	@Override
	public void eventOccurred(DivisionSelected e) {
		if(e.getSelected() == null)
			Utils.setContainerEnabled(this, false);
		else
			Utils.setContainerEnabled(this, true);
	}

}  //  @jve:decl-index=0:visual-constraint="49,51"
