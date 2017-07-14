package util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Utils {
	
	public static boolean isZero(float val) {
		return val > -0.001 && val < 0.001;
	}
	
	public static void setContainerEnabled(Container cont, boolean b) {
		cont.setEnabled(b);
		Component[] comps = cont.getComponents();
		for(Component c : comps)
			if(c instanceof Container)
				setContainerEnabled((Container) c, b);
			else {
				c.setEnabled(b);
			}
	}
	
	public static Image getScaledImage(File f, int size) {
		BufferedImage img = null;
		Image scaled = null;
		try {
			img = ImageIO.read(f);
			int[] dimensions = getDimensions(img, size);
			scaled = img.getScaledInstance(dimensions[0], dimensions[1], BufferedImage.SCALE_FAST);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scaled;
	}
	
	private static int[] getDimensions(BufferedImage img, int size) {
		int h = img.getHeight();
		int w = img.getWidth();
		if(h > size || w > size) {
			if(h > w) {
				w = (int) (((double) w / h) * size);
				h = size;
			} else if(h < w) {
				h = (int) (((double) h / w) * size);
				w = size;
			} else {
				w = size;
				h = size;
			}
		}
		return new int[]{w,h};
	}

	public static void writeScaledImage(File destiny, File img, int size) throws IOException {
		BufferedImage image = ImageIO.read(img);
		int[] dims = getDimensions(image, size);	

		BufferedImage bdest = new BufferedImage(dims[0], dims[1], BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bdest.createGraphics();
		AffineTransform at =
			AffineTransform.getScaleInstance((double)dims[0]/image.getWidth(),
					(double)dims[1]/image.getHeight());
		g.drawRenderedImage(image,at);
		ImageIO.write(bdest,"PNG", destiny);
	}
	
	public static File writeTempScaledImage(File img, int size) throws IOException {
		File dest = File.createTempFile("VirtualGallery_", ".png");
		writeScaledImage(dest, img, size);
		return dest;
	}
}
