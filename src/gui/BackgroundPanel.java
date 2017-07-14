package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class BackgroundPanel extends JPanel {

	private File backgroundImage;
	private int backgroundRepeat = REPEAT;

	static final int NO_REPEAT = -1;
	static final int REPEAT = 0;
	static final int REPEAT_X = 1;
	static final int REPEAT_Y = 2;

	void setImage(File image) {
		this.backgroundImage = image;
	}
	
	void setRepeat(int mode) {
		this.backgroundRepeat = mode;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (backgroundImage != null) {
			Graphics2D g2 = (Graphics2D)g.create();
			Insets inset = this.getInsets();
			ImageIcon image = null;
			try {
				image = new ImageIcon(backgroundImage.toURI().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			Rectangle clip = g.getClipBounds();

			int height = getSize().height - inset.bottom;
			int width = getSize().width - inset.right;

			if (clip.y + clip.height > height)
				clip.height = height - clip.y;

			if (clip.x + clip.width > width)
				clip.width = width - clip.x;

			g2.setClip(clip);
			int xRepeat = 0;
			int yRepeat = 0;

			if (backgroundRepeat == REPEAT || backgroundRepeat == REPEAT_Y)
				yRepeat = (int)Math.ceil(clip.getHeight() / image.getIconHeight());

			if (backgroundRepeat == REPEAT || backgroundRepeat == REPEAT_X)
				xRepeat = (int)Math.ceil(clip.getWidth() / image.getIconWidth());

			for (int i = 0; i <= yRepeat; i++) {
				for (int j = 0; j <= xRepeat; j++) {
					image.paintIcon(this, g2, j * image.getIconWidth() + inset.left, i * image.getIconHeight() + inset.top);
				}
			}
			g2.dispose();
		}
	}
}
