package modules.art3d;

import gui.Transform;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.EventListener;
import util.Utils;
import java.awt.Dimension;

class ObjectPanel extends JPanel implements EventListener<SelectArtEvent> {

	private static final long serialVersionUID = 1L;

	private Art3DModule module;

	private JLabel selectedLabel = null;

	private JLabel selectedTitle = null;

	private JTextField selectedTitleField = null;

	private JLabel selectedAuthor = null;

	private JTextField selectedAuthorField = null;

	private JLabel transformLabel = null;

	private Transform transform = null;

	/**
	 * This is the default constructor
	 */
	public ObjectPanel(Art3DModule module) {
		super();
		initialize();
		this.module = module;
		transform.setMode(module);
		module.addEventListener(this);
		
		Utils.setContainerEnabled(this, false);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.gridwidth = 2;
		gridBagConstraints12.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints12.weightx = 1.0;
		gridBagConstraints12.weighty = 1.0;
		gridBagConstraints12.anchor = GridBagConstraints.NORTH;
		gridBagConstraints12.gridy = 9;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridwidth = 2;
		gridBagConstraints11.anchor = GridBagConstraints.WEST;
		gridBagConstraints11.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints11.ipady = 5;
		gridBagConstraints11.gridy = 8;
		transformLabel = new JLabel();
		transformLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		transformLabel.setText("TRANSFORM");
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.fill = GridBagConstraints.BOTH;
		gridBagConstraints10.gridy = 7;
		gridBagConstraints10.weightx = 1.0;
		gridBagConstraints10.insets = new Insets(2, 2, 2, 0);
		gridBagConstraints10.gridx = 1;
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 0;
		gridBagConstraints9.anchor = GridBagConstraints.WEST;
		gridBagConstraints9.gridy = 7;
		selectedAuthor = new JLabel();
		selectedAuthor.setText("Author:");
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.fill = GridBagConstraints.BOTH;
		gridBagConstraints8.gridy = 6;
		gridBagConstraints8.weightx = 1.0;
		gridBagConstraints8.insets = new Insets(2, 2, 2, 0);
		gridBagConstraints8.gridx = 1;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.anchor = GridBagConstraints.WEST;
		gridBagConstraints7.gridy = 6;
		selectedTitle = new JLabel();
		selectedTitle.setText("Title:");
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.gridwidth = 2;
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints6.ipady = 5;
		gridBagConstraints6.gridy = 5;
		selectedLabel = new JLabel();
		selectedLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		selectedLabel.setText("SELECTED ARTWORK");
		this.setSize(180, 314);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(selectedLabel, gridBagConstraints6);
		this.add(selectedTitle, gridBagConstraints7);
		this.add(getSelectedTitleField(), gridBagConstraints8);
		this.add(selectedAuthor, gridBagConstraints9);
		this.add(getSelectedAuthorField(), gridBagConstraints10);
		this.add(transformLabel, gridBagConstraints11);
		this.add(getTransform(), gridBagConstraints12);
	}

	/**
	 * This method initializes selectedTitleField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSelectedTitleField() {
		if (selectedTitleField == null) {
			selectedTitleField = new JTextField();
			selectedTitleField.setEditable(false);
		}
		return selectedTitleField;
	}

	/**
	 * This method initializes selectedAuthorField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSelectedAuthorField() {
		if (selectedAuthorField == null) {
			selectedAuthorField = new JTextField();
			selectedAuthorField.setEditable(false);
		}
		return selectedAuthorField;
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
	public void eventOccurred(SelectArtEvent e) {
		Artwork3DInfo art = e.getSelectedArt();
		// deselected object
		if(art == null) {
			selectedTitleField.setText("");
			selectedAuthorField.setText("");
			Utils.setContainerEnabled(this, false);
		} else {
			selectedTitleField.setText(art.getName());
			selectedAuthorField.setText(art.getAuthor().getName());
			Utils.setContainerEnabled(this, true);
		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
