package modules.viewpoints;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import util.Utils;

class ViewpointGUI {

	private JPanel left = null;  //  @jve:decl-index=0:visual-constraint="50,49"
	private JPanel right = null;  //  @jve:decl-index=0:visual-constraint="331,52"
	private JLabel placeLabel = null;
	private JLabel decrLabel = null;
	private JTextField descrField = null;
	private JButton createButton = null;
	private JLabel listLabel = null;
	private JLabel modifyLabel = null;
	private JLabel descrLabel = null;
	private JTextField changeDescr = null;
	private JButton changePosition = null;
	private JButton delete = null;
	private JPanel spacer = null;
	private JButton gotoButton = null;
	private JLabel selectedLabel1 = null;

	private ViewpointModule module;
	private JScrollPane listScroll = null;
	private JList viewpointList = null;
	
	ViewpointGUI(ViewpointModule module) {
		this.module = module;
	}
	
	/**
	 * This method initializes left	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	JPanel getLeft() {
		if (left == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.gridy = 4;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.weighty = 1.0;
			gridBagConstraints21.gridwidth = 2;
			gridBagConstraints21.anchor = GridBagConstraints.NORTH;
			gridBagConstraints21.gridx = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.ipady = 5;
			gridBagConstraints4.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 3;
			listLabel = new JLabel();
			listLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			listLabel.setText("DEFINED VIEWPOINTS");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridwidth = 2;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 0);
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 1;
			decrLabel = new JLabel();
			decrLabel.setText("Description:");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.ipady = 5;
			gridBagConstraints.gridy = 0;
			placeLabel = new JLabel();
			placeLabel.setText("NEW VIEWPOINT");
			placeLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			left = new JPanel();
			left.setLayout(new GridBagLayout());
			left.setPreferredSize(new Dimension(180, 200));
			left.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			left.setSize(new Dimension(180, 273));
			left.add(placeLabel, gridBagConstraints);
			left.add(decrLabel, gridBagConstraints1);
			left.add(getDescrField(), gridBagConstraints2);
			left.add(getCreateButton(), gridBagConstraints3);
			left.add(listLabel, gridBagConstraints4);
			left.add(getListScroll(), gridBagConstraints21);
		}
		return left;
	}

	/**
	 * This method initializes right	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	JPanel getRight() {
		if (right == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridwidth = 2;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.ipady = 5;
			gridBagConstraints13.gridy = 0;
			selectedLabel1 = new JLabel();
			selectedLabel1.setFont(new Font("Tahoma", Font.BOLD, 11));
			selectedLabel1.setText("SELECTED VIEWPOINT");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridwidth = 3;
			gridBagConstraints12.gridy = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.weightx = 0.0;
			gridBagConstraints11.gridy = 6;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridwidth = 2;
			gridBagConstraints10.anchor = GridBagConstraints.CENTER;
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints10.gridy = 5;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridwidth = 2;
			gridBagConstraints9.anchor = GridBagConstraints.CENTER;
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints9.gridy = 4;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.gridy = 3;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 0);
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 3;
			descrLabel = new JLabel();
			descrLabel.setText("Description:");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.ipady = 5;
			gridBagConstraints6.gridwidth = 2;
			gridBagConstraints6.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints6.gridy = 2;
			modifyLabel = new JLabel();
			modifyLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			modifyLabel.setText("MODIFY VIEWPOINT");
			right = new JPanel();
			right.setLayout(new GridBagLayout());
			right.setPreferredSize(new Dimension(180, 200));
			right.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			right.setSize(new Dimension(180, 173));
			right.add(modifyLabel, gridBagConstraints6);
			right.add(descrLabel, gridBagConstraints7);
			right.add(getChangeDescr(), gridBagConstraints8);
			right.add(getChangePosition(), gridBagConstraints9);
			right.add(getDelete(), gridBagConstraints10);
			right.add(getSpacer(), gridBagConstraints11);
			right.add(getGotoButton(), gridBagConstraints12);
			right.add(selectedLabel1, gridBagConstraints13);
			
			Utils.setContainerEnabled(right, false);
		}
		return right;
	}

	/**
	 * This method initializes descrField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDescrField() {
		if (descrField == null) {
			descrField = new JTextField();
		}
		return descrField;
	}

	/**
	 * This method initializes createButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCreateButton() {
		if (createButton == null) {
			createButton = new JButton();
			createButton.setText("Create viewpoint");
			createButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					module.defineViewpoint(descrField.getText());
					deselect();
					updateList();
				}
			});
		}
		return createButton;
	}
	
	private String[] viewpoints;  //  @jve:decl-index=0:  //  @jve:decl-index=0:
	
	private void deselect() {
		viewpointList.clearSelection();
		changeDescr.setText("");
		Utils.setContainerEnabled(right, false);
	}
	
	void updateList() {
		 DefaultListModel m = (DefaultListModel) viewpointList.getModel();
		 m.clear();
		 this.viewpoints = module.getViewpoints();
		 for(String s : viewpoints)
			 m.addElement(s);
	}

	/**
	 * This method initializes changeDescr	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getChangeDescr() {
		if (changeDescr == null) {
			changeDescr = new JTextField();
			changeDescr.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					module.changeSelectedDescription(changeDescr.getText());
				}
			});
		}
		return changeDescr;
	}

	/**
	 * This method initializes changePosition	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getChangePosition() {
		if (changePosition == null) {
			changePosition = new JButton();
			changePosition.setText("Change position");
			changePosition.setPreferredSize(new Dimension(120, 23));
			changePosition.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					module.changeSelectedLocation();
				}				
			});
		}
		return changePosition;
	}

	/**
	 * This method initializes delete	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDelete() {
		if (delete == null) {
			delete = new JButton();
			delete.setText("Delete viewpoint");
			delete.setPreferredSize(new Dimension(120, 23));
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					module.deleteSelected();
					deselect();
					updateList();
				}	
			});
		}
		return delete;
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

	/**
	 * This method initializes gotoButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getGotoButton() {
		if (gotoButton == null) {
			gotoButton = new JButton();
			gotoButton.setText("Go to selected viewpoint");
			gotoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					module.goToSelected();
				}
			});
		}
		return gotoButton;
	}

	/**
	 * This method initializes listScroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getListScroll() {
		if (listScroll == null) {
			listScroll = new JScrollPane();
			listScroll.setPreferredSize(new Dimension(100, 150));
			listScroll.setViewportView(getViewpointList());
		}
		return listScroll;
	}

	/**
	 * This method initializes viewpointList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getViewpointList() {
		if (viewpointList == null) {
			viewpointList = new JList();
			viewpointList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			DefaultListModel mod = new DefaultListModel();
			viewpointList.setModel(mod);
			viewpointList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent evt) {
					int index = viewpointList.getSelectedIndex();
					if(index == -1) {
						deselect();
					} else {
						module.setSelected(index);
						String selected = viewpoints[index];
						changeDescr.setText(selected);
						Utils.setContainerEnabled(right, true);
					}
				}
			});
		}
		return viewpointList;
	}

}
