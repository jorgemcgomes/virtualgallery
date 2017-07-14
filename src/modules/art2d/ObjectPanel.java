package modules.art2d;

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

	private Art2DModule art2DMode;  //  @jve:decl-index=0:
	private JLabel transformLabel = null;
	private JLabel selectedLabel = null;
	private JLabel selectedTitle = null;
	private JLabel selectedAuthor = null;
	private JTextField selectedTitleField = null;
	private JTextField selectedAuthorField = null;
	private Transform transform = null;

	/**
	 * This is the default constructor
	 */
	ObjectPanel(Art2DModule module) {
		super();
		initialize();
		art2DMode = module;
		transform.setMode(art2DMode);
		art2DMode.addEventListener(this);
		
		Utils.setContainerEnabled(this, false);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
		gridBagConstraints13.gridx = 0;
		gridBagConstraints13.gridwidth = 2;
		gridBagConstraints13.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints13.weightx = 1.0;
		gridBagConstraints13.weighty = 1.0;
		gridBagConstraints13.anchor = GridBagConstraints.NORTH;
		gridBagConstraints13.gridy = 11;
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.fill = GridBagConstraints.BOTH;
		gridBagConstraints12.gridy = 9;
		gridBagConstraints12.weightx = 1.0;
		gridBagConstraints12.insets = new Insets(2, 2, 2, 0);
		gridBagConstraints12.gridx = 1;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.gridy = 8;
		gridBagConstraints11.weightx = 1.0;
		gridBagConstraints11.insets = new Insets(2, 2, 2, 0);
		gridBagConstraints11.gridx = 1;
		GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.gridx = 0;
		gridBagConstraints10.anchor = GridBagConstraints.WEST;
		gridBagConstraints10.gridy = 9;
		selectedAuthor = new JLabel();
		selectedAuthor.setText("Author:");
		GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
		gridBagConstraints9.gridx = 0;
		gridBagConstraints9.anchor = GridBagConstraints.WEST;
		gridBagConstraints9.gridy = 8;
		selectedTitle = new JLabel();
		selectedTitle.setText("Title:");
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.ipady = 5;
		gridBagConstraints6.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.gridwidth = 2;
		gridBagConstraints6.gridy = 7;
		selectedLabel = new JLabel();
		selectedLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		selectedLabel.setText("SELECTED ARTWORK");
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 0;
		gridBagConstraints5.anchor = GridBagConstraints.WEST;
		gridBagConstraints5.ipady = 5;
		gridBagConstraints5.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints5.gridwidth = 2;
		gridBagConstraints5.gridy = 10;
		transformLabel = new JLabel();
		transformLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		transformLabel.setText("TRANSFORM");
		this.setSize(180, 322);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(transformLabel, gridBagConstraints5);
		this.add(selectedLabel, gridBagConstraints6);
		this.add(selectedTitle, gridBagConstraints9);
		this.add(selectedAuthor, gridBagConstraints10);
		this.add(getSelectedTitleField(), gridBagConstraints11);
		this.add(getSelectedAuthorField(), gridBagConstraints12);
		this.add(getTransform(), gridBagConstraints13);
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
		Artwork2DInfo paint = e.getSelectedArtwork();
		// deselected object
		if(paint == null) {
			selectedTitleField.setText("");
			selectedAuthorField.setText("");
			Utils.setContainerEnabled(this, false);
		} else {
			selectedTitleField.setText(paint.getTitle());
			selectedAuthorField.setText(paint.getAuthor().getName());
			Utils.setContainerEnabled(this, true);
		}
	}
}  //  @jve:decl-index=0:visual-constraint="72,54"
