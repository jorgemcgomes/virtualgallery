package gui.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

abstract class ModeOptions extends JScrollPane {
	
	protected String title;
	protected int width;
	protected JPanel panel;
	
	ModeOptions(String title) {
		super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.title = title;
		width = width();
		
		panel = new JPanel(true);
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		int hGap = 5, vGap = 3;
		layout.setHgap(hGap);
		layout.setVgap(vGap);
		panel.setLayout(layout);
		panel.setBorder(new EmptyBorder(10-vGap, 10-hGap, 10-vGap, 10-hGap));
		panel.setPreferredSize(new Dimension(width, height() ));
		//panel.setMaximumSize(new Dimension(200, 5000));
		
		super.setViewportView(panel);
		
		addTitle(title);
	}
	
	private void addTitle(String tt) {
		JLabel t = new JLabel("<HTML>" + tt + "</HTML>");
		Font curFont = t.getFont();
		t.setFont(new Font(curFont.getFontName(), curFont.getStyle(), 16));
		panel.add(t);
		changeLine(5);
	}
	
	void addSubtitle(String tt) {
		JLabel t = new JLabel("<HTML>" + tt.toUpperCase() + "</HTML>");
		Font curFont = t.getFont();
		t.setFont(new Font(curFont.getFontName(), Font.BOLD, curFont.getSize()));
		panel.add(t);
		changeLine(0);
	}
	
	void changeLine(int space) {
		panel.add(Box.createRigidArea(new Dimension(width, space)));
	}
	
	String getTitle() {
		return title;
	}
	
	void scrollUp() {
		JScrollBar bar = super.getVerticalScrollBar();  
		bar.setValue(bar.getMinimum());  
	}
	
	abstract int height();
	
	int width() {
		return 200;
	}

	abstract void activate();
	
	abstract void terminate();
}
