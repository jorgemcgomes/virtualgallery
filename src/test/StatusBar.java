package gui;

import javax.swing.JPanel;

import util.EventListener;
import core.StatusEvent;
import java.awt.Dimension;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;

class StatusBar extends JPanel implements EventListener<StatusEvent> {

	private JButton windowButton = null;
	private JLabel messageLabel = null;
	private StatusWindow window;

	/**
	 * This method initializes 
	 * 
	 */
	public StatusBar() {
		super();
		initialize();
		window = new StatusWindow();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        messageLabel = new JLabel();
        messageLabel.setText("");
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(280, 25));
        this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        this.add(getWindowButton(), BorderLayout.EAST);
        this.add(messageLabel, BorderLayout.WEST);
			
	}

	@Override
	public void eventOccurred(StatusEvent evt) {
		window.eventOccurred(evt);
	    switch(evt.getType()) {
	    case StatusEvent.OK_MSG:
	    	messageLabel.setForeground(new Color(0,0,0));
	    	messageLabel.setText(evt.getMessage());
	    	break;
	    case StatusEvent.WARNING_MSG:
	    	messageLabel.setForeground(new Color(255,200,0));
	    	messageLabel.setText(evt.getMessage());
	    	break;
	    case StatusEvent.ERROR_MSG:
	    	messageLabel.setForeground(new Color(255,0,0));
	    	messageLabel.setText(evt.getMessage());
	    	break;
	    }
	}
	
	void notifyStatus(Object src, int type, String message) {
		StatusEvent evt = new StatusEvent(src, type, message);
		eventOccurred(evt);
	}

	/**
	 * This method initializes windowButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getWindowButton() {
		if (windowButton == null) {
			windowButton = new JButton();
			windowButton.setPreferredSize(new Dimension(90, 20));
			windowButton.setText("Log window");
			windowButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(window.isVisible()) {
						window.setVisible(false);
					} else {
						window.setVisible(true);
					}
				}
			});
		}
		return windowButton;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"