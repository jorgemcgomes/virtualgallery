package modules.art2d;

import gui.GUIController;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.SoftBevelBorder;

import util.Utils;

import core.StatusEvent;
import database.AuthorInfo;

class PlacementPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final int IMAGE_DIMENSION = 150;
	
	private ChooseListener listener;

	private Art2DModule art2DMode;  //  @jve:decl-index=0:
	private JLabel artChooseLabel = null;
	private JComboBox artChooser = null;
	private JLabel authorLabel = null;
	private JPanel imagePanel = null;
	private JLabel imageLabel = null;
	private JTextField chooseAuthor = null;
	private Artwork2DInfo[] artworks;
	private AuthorInfo[] authors;
	private String[] types;

	private JComboBox authorChooser = null;

	private JComboBox artTypeChooser = null;

	/**
	 * This is the default constructor
	 */
	PlacementPanel(Art2DModule module) {
		super();
		initialize();
		art2DMode = module;
		
		listener = new ChooseListener();
		artChooser.addActionListener(listener);
		authorChooser.addActionListener(listener);
		artTypeChooser.addActionListener(listener);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.fill = GridBagConstraints.BOTH;
		gridBagConstraints15.gridy = 2;
		gridBagConstraints15.weightx = 1.0;
		gridBagConstraints15.gridwidth = 2;
		gridBagConstraints15.gridx = 0;
		GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
		gridBagConstraints14.fill = GridBagConstraints.BOTH;
		gridBagConstraints14.gridy = 3;
		gridBagConstraints14.weightx = 1.0;
		gridBagConstraints14.gridwidth = 2;
		gridBagConstraints14.insets = new Insets(4, 0, 0, 0);
		gridBagConstraints14.gridx = 0;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints8.gridy = 5;
		gridBagConstraints8.weightx = 1.0;
		gridBagConstraints8.anchor = GridBagConstraints.WEST;
		gridBagConstraints8.ipady = 0;
		gridBagConstraints8.insets = new Insets(4, 2, 4, 0);
		gridBagConstraints8.gridx = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridwidth = 2;
		gridBagConstraints4.weighty = 1.0;
		gridBagConstraints4.anchor = GridBagConstraints.NORTH;
		gridBagConstraints4.gridy = 6;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.ipadx = 0;
		gridBagConstraints3.ipady = 2;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints3.gridy = 5;
		authorLabel = new JLabel();
		authorLabel.setText("Author: ");
		authorLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 4;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.gridwidth = 2;
		gridBagConstraints2.insets = new Insets(4, 0, 0, 0);
		gridBagConstraints2.gridx = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints.ipady = 5;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridy = 1;
		artChooseLabel = new JLabel();
		artChooseLabel.setText("CHOOSE ARTWORK");
		artChooseLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		this.setSize(180, 300);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(artChooseLabel, gridBagConstraints);
		this.add(getPaintingChooser(), gridBagConstraints2);
		this.add(authorLabel, gridBagConstraints3);
		this.add(getImagePanel(), gridBagConstraints4);
		this.add(getChooseAuthor(), gridBagConstraints8);
		this.add(getAuthorCooser(), gridBagConstraints14);
		this.add(getArtTypeChooser(), gridBagConstraints15);
	}

	/**
	 * This method initializes paintingChooser	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getPaintingChooser() {
		if (artChooser == null) {
			artChooser = new JComboBox();
		}
		return artChooser;
	}

	/**
	 * This method initializes imagePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getImagePanel() {
		if (imagePanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.ipadx = 0;
			gridBagConstraints7.fill = GridBagConstraints.NONE;
			gridBagConstraints7.gridx = 0;
			imageLabel = new JLabel();
			imagePanel = new JPanel();
			imagePanel.setLayout(new GridBagLayout());
			imagePanel.setBorder(BorderFactory.createCompoundBorder(new SoftBevelBorder(SoftBevelBorder.RAISED), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			imagePanel.setPreferredSize(new Dimension(160, 160));
			imagePanel.add(imageLabel, gridBagConstraints7);
		}
		return imagePanel;
	}

	/**
	 * This method initializes chooseAuthor	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getChooseAuthor() {
		if (chooseAuthor == null) {
			chooseAuthor = new JTextField();
			chooseAuthor.setEditable(false);
		}
		return chooseAuthor;
	}

	/**
	 * This method initializes authorCooser	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getAuthorCooser() {
		if (authorChooser == null) {
			authorChooser = new JComboBox();
		}
		return authorChooser;
	}

	/**
	 * This method initializes artTypeChooser	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getArtTypeChooser() {
		if (artTypeChooser == null) {
			artTypeChooser = new JComboBox();
		}
		return artTypeChooser;
	}

	void refreshArtList() {
		types = art2DMode.getArtTypes();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement("All art types");
		for(String t : types)
			model.addElement(t);
		artTypeChooser.setModel(model);
		artTypeChooser.setSelectedIndex(0);
		listener.chooseType();
	}

	private class ChooseListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent evt) {
			Object src = evt.getSource();
			if(src == artChooser) {
				chooseArtwork();
			} else if(src == authorChooser) {
				chooseAuthor();
			} else if(src == artTypeChooser) {
				chooseType();
			}
		}
	
		private void chooseType() {
			int typeIndex = artTypeChooser.getSelectedIndex();
			String type = typeIndex == 0 ? null : types[typeIndex - 1];
			authors = art2DMode.getAllAuthors(type);
			DefaultComboBoxModel model = new DefaultComboBoxModel();
			model.addElement("All authors");
			for(AuthorInfo a : authors)
				model.addElement(a.getName());
			authorChooser.setModel(model);
			authorChooser.setSelectedIndex(0);
		}
	
		private void chooseAuthor() {
			int authorIndex = authorChooser.getSelectedIndex();
			int typeIndex = artTypeChooser.getSelectedIndex();
			String type = typeIndex == 0 ? null : types[typeIndex - 1];
			AuthorInfo author = authorIndex == 0 ? null : authors[authorIndex - 1];
			artworks = art2DMode.getArtworks(author, type);
			
			DefaultComboBoxModel model = (DefaultComboBoxModel) artChooser.getModel();
			model.removeAllElements();
			model.addElement("Choose artwork");
			for(Artwork2DInfo p : artworks)
				model.addElement(p.getTitle());
			artChooser.setModel(model);
			artChooser.setSelectedIndex(0);
		}
	
		private void chooseArtwork() {
			int index = artChooser.getSelectedIndex();
			if(index > 0) {
				Artwork2DInfo chosen = artworks[index - 1];
				chooseAuthor.setText(chosen.getAuthor().getName());
				art2DMode.selectArtwork(chosen);
				Image img = Utils.getScaledImage(chosen.getImage(), IMAGE_DIMENSION);
				if(img != null) {
					ImageIcon icon = new ImageIcon(img); 
					imageLabel.setIcon(icon);
				} else {
					imageLabel.setIcon(new ImageIcon());
					GUIController.getInstance().notifyStatus(this, StatusEvent.ERROR_MSG, 
					"Image not found");
				}
			}
		}
	}
}  //  @jve:decl-index=0:visual-constraint="72,54"
