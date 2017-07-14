package modules.art3d;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import database.AuthorInfo;
import java.awt.Dimension;
import java.io.File;

import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JRadioButton;

import util.Utils;

class PlacementPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Art3DModule module;

	private JLabel artChooseLabel = null;

	private JComboBox authorChooser = null;

	private JComboBox artChooser = null;

	private JLabel authorLabel = null;

	private JTextField chooseAuthor = null;

	private ChooseListener listener;

	private Artwork3DInfo[] artworks;

	private AuthorInfo[] authors;

	private JPanel thumbnail = null;

	private JLabel detailLabel = null;

	private JRadioButton highDetail = null;

	private JRadioButton lowDetail = null;

	private JPanel spacer = null;

	private JLabel imageLabel = null;
	
	private static final int IMAGE_SIZE = 150;

	/**
	 * This is the default constructor
	 */
	public PlacementPanel(Art3DModule module) {
		super();
		initialize();
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(getHighDetail());
		bg.add(getLowDetail());
		this.module = module;
		listener = new ChooseListener();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridy = 9;
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 0;
		gridBagConstraints8.anchor = GridBagConstraints.WEST;
		gridBagConstraints8.gridwidth = 2;
		gridBagConstraints8.gridy = 8;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.gridwidth = 2;
		gridBagConstraints7.anchor = GridBagConstraints.WEST;
		gridBagConstraints7.gridy = 7;
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 0;
		gridBagConstraints6.gridwidth = 2;
		gridBagConstraints6.insets = new Insets(10, 0, 0, 0);
		gridBagConstraints6.ipady = 5;
		gridBagConstraints6.anchor = GridBagConstraints.WEST;
		gridBagConstraints6.gridy = 6;
		detailLabel = new JLabel();
		detailLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		detailLabel.setText("DETAIL LEVEL");
		GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
		gridBagConstraints51.gridx = 0;
		gridBagConstraints51.gridwidth = 2;
		gridBagConstraints51.gridy = 5;
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.fill = GridBagConstraints.BOTH;
		gridBagConstraints5.gridy = 4;
		gridBagConstraints5.weightx = 1.0;
		gridBagConstraints5.insets = new Insets(4, 2, 4, 0);
		gridBagConstraints5.weighty = 0.0;
		gridBagConstraints5.gridx = 1;
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.anchor = GridBagConstraints.WEST;
		gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints4.ipady = 2;
		gridBagConstraints4.weighty = 0.0;
		gridBagConstraints4.weightx = 0.0;
		gridBagConstraints4.gridy = 4;
		authorLabel = new JLabel();
		authorLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		authorLabel.setText("Author: ");
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.gridy = 3;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.gridwidth = 2;
		gridBagConstraints3.insets = new Insets(4, 0, 0, 0);
		gridBagConstraints3.gridx = 0;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 2;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.gridwidth = 2;
		gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints2.gridx = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.anchor = GridBagConstraints.WEST;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints1.ipady = 5;
		gridBagConstraints1.gridy = 1;
		artChooseLabel = new JLabel();
		artChooseLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		artChooseLabel.setText("CHOOSE ARTWORK");
		this.setSize(180, 357);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.add(artChooseLabel, gridBagConstraints1);
		this.add(getAuthorChooser(), gridBagConstraints2);
		this.add(getArtChooser(), gridBagConstraints3);
		this.add(authorLabel, gridBagConstraints4);
		this.add(getChooseAuthor(), gridBagConstraints5);
		this.add(getThumbnail(), gridBagConstraints51);
		this.add(detailLabel, gridBagConstraints6);
		this.add(getHighDetail(), gridBagConstraints7);
		this.add(getLowDetail(), gridBagConstraints8);
		this.add(getSpacer(), gridBagConstraints);
	}

	void refreshArtList() {
		authors = module.getAllAuthors();
		DefaultComboBoxModel model = (DefaultComboBoxModel) authorChooser.getModel();
		model.addElement("All authors");
		for(AuthorInfo a : authors)
			model.addElement(a.getName());
		authorChooser.setSelectedIndex(0);
		listener.chooseAuthor();

		authorChooser.addActionListener(listener);
		artChooser.addActionListener(listener);
	}

	private class ChooseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent evt) {			
			Object src = evt.getSource();
			if(src == authorChooser) {
				chooseAuthor();
			} else if(src == artChooser) {
				chooseArt();
			}
		}

		private void chooseAuthor() {			
			int authorIndex = authorChooser.getSelectedIndex();
			AuthorInfo author = authorIndex == 0 ? null : authors[authorIndex - 1];
			artworks = module.getArtworks(author);

			DefaultComboBoxModel model = (DefaultComboBoxModel) artChooser.getModel();
			model.removeAllElements();
			model.addElement("Choose artwork");
			for(Artwork3DInfo a : artworks)
				model.addElement(a.getName());
			artChooser.setSelectedIndex(0);
		}

		private void chooseArt() {
			int index = artChooser.getSelectedIndex();
			if(index > 0) {
				Artwork3DInfo chosen = artworks[index - 1];
				chooseAuthor.setText(chosen.getAuthor().getName());
				module.selectArtwork(chosen);
				
				File t = chosen.getThumbnail();
				if(t != null && t.exists()) {
					Image img = Utils.getScaledImage(t, IMAGE_SIZE);
					imageLabel.setText("");
					imageLabel.setIcon(new ImageIcon(img));
				} else {
					imageLabel.setText("Image unavailable");
					imageLabel.setIcon(null);
				}
				
				boolean hasHigh = chosen.hasHighDetail();
				highDetail.setEnabled(hasHigh);
				highDetail.setSelected(hasHigh);
				module.selectDetail(true);
				
				boolean hasLow = chosen.hasLowDetail();
				lowDetail.setEnabled(hasLow);
				if(!hasHigh) {
					lowDetail.setSelected(true);
					module.selectDetail(false);
				}
			}
		}

	}

	/**
	 * This method initializes authorChooser	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getAuthorChooser() {
		if (authorChooser == null) {
			authorChooser = new JComboBox();
		}
		return authorChooser;
	}

	/**
	 * This method initializes artChooser	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getArtChooser() {
		if (artChooser == null) {
			artChooser = new JComboBox();
		}
		return artChooser;
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
	 * This method initializes thumbnail	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getThumbnail() {
		if (thumbnail == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 0;
			imageLabel = new JLabel();
			imageLabel.setText("Image unavailable");
			thumbnail = new JPanel();
			thumbnail.setLayout(new GridBagLayout());
			thumbnail.setPreferredSize(new Dimension(160, 160));
			thumbnail.setBorder(BorderFactory.createCompoundBorder(new SoftBevelBorder(SoftBevelBorder.RAISED), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			thumbnail.add(imageLabel, gridBagConstraints9);
		}
		return thumbnail;
	}

	/**
	 * This method initializes highDetail	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getHighDetail() {
		if (highDetail == null) {
			highDetail = new JRadioButton();
			highDetail.setText("High (3D Model)");
			highDetail.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					module.selectDetail(highDetail.isSelected());
				}
			});
		}
		return highDetail;
	}

	/**
	 * This method initializes lowDetail	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getLowDetail() {
		if (lowDetail == null) {
			lowDetail = new JRadioButton();
			lowDetail.setText("Low (Model from image)");
			lowDetail.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					module.selectDetail(highDetail.isSelected());
				}
			});
		}
		return lowDetail;
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

}  //  @jve:decl-index=0:visual-constraint="10,10"
