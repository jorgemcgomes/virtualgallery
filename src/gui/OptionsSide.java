package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class OptionsSide {

	private JPanel panel;
	private JPanel content;
	private TitlePanel title;
	private CardLayout cards;
	private JFrame dialog;

	OptionsSide(final JPanel outerPanel) {
		this.panel = outerPanel;
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		title = new TitlePanel();
		panel.add(title);
		cards = new CardLayout();
		content = new JPanel(cards);
		panel.add(content);
		title.detach.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				if(dialog == null) {
					dialog = new JFrame();
					dialog.setAlwaysOnTop(true);
					dialog.setResizable(false);
					dialog.setContentPane(content);
					dialog.setVisible(true);
					dialog.pack();
					dialog.setLocationRelativeTo(panel);
					dialog.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent we) {
							dialog.setVisible(false);
							panel.add(content);
							panel.setVisible(true);
							dialog.dispose();
							dialog = null;
						}
					});

					panel.setVisible(false);
					panel.remove(content);
				}
			}
		});

		panel.setMaximumSize(new Dimension(180, 2000));
		panel.setVisible(false);
	}

	void addCard(Component card, String ref) {
		content.add(card, ref);
	}

	void switchCard(String ref) {
		cards.show(content, ref);
	}

	void setTitle(String str) {
		if(dialog == null)
			title.titleLabel.setText(str);
		else
			dialog.setTitle(str);
	}

	void hide() {
		if(dialog == null) 
			panel.setVisible(false);
		else
			dialog.setVisible(false);
	}

	void show() {
		if(dialog == null)
			panel.setVisible(true);
		else
			dialog.setVisible(true);
	}

	void showCard(String ref, String title) {
		show();
		switchCard(ref);
		setTitle(title);
	}

	private class TitlePanel extends BackgroundPanel {

		private JLabel titleLabel = null;
		private JButton detach = null;

		private TitlePanel() {
			super();
			File bg = new File("images/titleBG.png");
			super.setImage(bg);
			super.setRepeat(BackgroundPanel.REPEAT_X);
			initialize();
		}

		private void initialize() {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.anchor = GridBagConstraints.EAST;
			gridBagConstraints1.fill = GridBagConstraints.NONE;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.ipady = 0;
			gridBagConstraints1.ipadx = 0;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints.gridy = 0;

			setLayout(new GridBagLayout());
			setPreferredSize(new Dimension(180, 25));
			setMaximumSize(new Dimension(180, 25));
			setBackground(new Color(170, 170, 170));

			add(getTitleLabel(), gridBagConstraints);
			add(getDetach(), gridBagConstraints1);
		}

		private JLabel getTitleLabel() {
			if(titleLabel == null) {
				titleLabel = new JLabel();
				titleLabel.setText("Title");
				titleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
			}
			return titleLabel;
		}

		private JButton getDetach() {
			if (detach == null) {
				detach = new JButton();
				detach.setPreferredSize(new Dimension(20, 20));
				detach.setIcon(new ImageIcon("images/detachIcon.png"));
				detach.setOpaque(false);
			}
			return detach;
		}
	}
}
