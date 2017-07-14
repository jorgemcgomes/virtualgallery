package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;

import core.Gallery;
import core.module.OperationModule;

class ModeChooser extends BackgroundPanel implements ActionListener {
	
	final static String BLANK_OPTIONS = "blank";
	
	private Option activeOption;
	private Option[] options;
	private OptionsSide left, right;
	
	ModeChooser(JPanel leftPanel, JPanel rightPanel) {
		super();
		setImage(new File("images/chooserBG.png"));
		setRepeat(BackgroundPanel.REPEAT_X);
		setLayout(new FlowLayout(FlowLayout.LEFT, 7, 7));
		setDoubleBuffered(true);
		setBackground(new Color(205,205,205));
		setAlignmentX(LEFT_ALIGNMENT);
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(180,180,180)));
		//add(Box.createRigidArea(new Dimension(1, 25)));
		
		left = new OptionsSide(leftPanel);
		right = new OptionsSide(rightPanel);
		
		options = getOptions();
	}
	
	/**
	 * Acrescentar aqui as novas opcoes
	 */
	private Option[] getOptions() {
		OperationModule[] modules = Gallery.getInstance().getAllModules();
		Option[] opts = new Option[modules.length];
		for(int i = 0 ; i < modules.length ; i++)
			opts[i] = new Option(modules[i]);
		return opts;
	}
	
	void enableOptions() {
		for(Option o : options) {
			o.button.setEnabled(true);
		}
	}
	
	void disableOptions() {
		left.hide();
		right.hide();
		activeOption = null;
		for(Option o : options) {
			o.button.setSelected(false);
			o.button.setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		AbstractButton b = (AbstractButton) evt.getSource();
		if(b.isSelected()) {
			for(Option o : options) {
				if(b == o.button) {
					Gallery.getInstance().switchMode(o.module);
					
					if(activeOption != null)
						activeOption.button.setSelected(false);
					activeOption = o;
					
					if(o.module.getGUI().getLeftPanel() != null)
						left.showCard(o.getName(), o.module.getGUI().getLeftTitle());
					else
						left.hide();
					
					if(o.module.getGUI().getRightPanel() != null)
						right.showCard(o.getName(), o.module.getGUI().getRightTitle());
					else
						right.hide();
					
					break;
				}
			}
		} else {		
			left.hide();
			right.hide();
			activeOption = null;
			Gallery.getInstance().switchMode(null);
		}
	}
	
	private class Option {
		
		private OperationModule module;
		private JToggleButton button;
		
		Option(OperationModule mod) {	
			module = mod;
			ModuleGUI gui = module.getGUI();
			
			JPanel leftPan = gui.getLeftPanel();
			if(leftPan != null)
				left.addCard(leftPan, module.getGUI().getTitle());	
			
			JPanel rightPan = gui.getRightPanel();
			if(rightPan != null)
				right.addCard(rightPan, module.getGUI().getTitle());
			
			button = new JToggleButton(gui.getTitle());
			button.setPreferredSize(new Dimension(140,25));
			button.addActionListener(ModeChooser.this);
			button.setEnabled(false);
			button.setOpaque(false);
			ModeChooser.this.add(button);
		}
	
		String getName() {
			return module.getGUI().getTitle();
		}
	}
	

}
