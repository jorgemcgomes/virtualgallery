package gui.test;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;

import javax.swing.*;


public class Test {
	public static void main(String[] args) {

		CollapsablePanel cp = new CollapsablePanel("test", buildPanel());

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new JScrollPane(cp));
		//f.getContentPane().add(new JButton("butao1"));
		f.setSize(360, 500);
		f.setLocation(200, 100);
		f.setVisible(true);
	}

	public static JPanel buildPanel() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 1, 2, 1);
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		JPanel p1 = new JPanel(new GridBagLayout());
		gbc.gridwidth = gbc.RELATIVE;
		p1.add(new JButton("button 1"), gbc);
		gbc.gridwidth = gbc.REMAINDER;
		p1.add(new JButton("button 2"), gbc);
		gbc.gridwidth = gbc.RELATIVE;
		p1.add(new JButton("button 3"), gbc);
		gbc.gridwidth = gbc.REMAINDER;
		p1.add(new JButton("button 4"), gbc);

		return p1;
	}
}

class CollapsablePanel extends JPanel {

	private boolean selected;
	JPanel contentPanel;
	HeaderPanel headerPanel;

	public CollapsablePanel(String text, JPanel panel) {
		super(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(1, 3, 0, 3);
		gbc.weightx = 1.0;
		gbc.fill = gbc.HORIZONTAL;
		gbc.gridwidth = gbc.REMAINDER;
	
		selected = false;
		headerPanel = new HeaderPanel(text);
	
		setBackground(new Color(200, 200, 220));
		contentPanel = panel;
	
		add(headerPanel, gbc);
		add(contentPanel, gbc);
		contentPanel.setVisible(false);
	
		JLabel padding = new JLabel();
		gbc.weighty = 1.0;
		add(padding, gbc);
	
	}

	public void toggleSelection() {
		selected = !selected;
	
		if (contentPanel.isShowing())
			contentPanel.setVisible(false);
		else
			contentPanel.setVisible(true);
	
		validate();
	
		headerPanel.repaint();
	}

	private class HeaderPanel extends JPanel implements MouseListener {
		
		private String text;
		private Font font;
		private BufferedImage open, closed;
		final int OFFSET = 30, PAD = 5;

		public HeaderPanel(String t) {
			text = t;
			addMouseListener(this);
			font = UIManager.getDefaults().getFont("Label.font");
			// setRequestFocusEnabled(true);
			setPreferredSize(new Dimension(200, 20));
			int w = getWidth();
			int h = getHeight();
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int h = getHeight();
			/*if (selected)
				g2.drawImage(open, PAD, 0, h, h, this);
			else
				g2.drawImage(closed, PAD, 0, h, h, this);
			 */ // Uncomment once you have your own images
			g2.setFont(font);
			FontRenderContext frc = g2.getFontRenderContext();
			LineMetrics lm = font.getLineMetrics(text, frc);
			float height = lm.getAscent() + lm.getDescent();
			float x = OFFSET;
			float y = (h + height) / 2 - lm.getDescent();
			g2.drawString(text, x, y);
		}

		public void mouseClicked(MouseEvent e) {
			toggleSelection();
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

	}

}