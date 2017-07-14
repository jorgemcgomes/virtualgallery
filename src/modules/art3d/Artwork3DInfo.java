package modules.art3d;

import java.io.File;
import java.util.LinkedList;

import javax.vecmath.Point3f;

import core.Gallery;
import core.StatusEvent;

import database.AuthorInfo;

class Artwork3DInfo {

	private int id;
	private AuthorInfo author;
	private File model;
	private String name;
	private int year;
	private Point3f upper;
	private Point3f lower;

	private float height, width, length;
	private File thumbnail;

	private Fetcher fetcher;

	Artwork3DInfo(Fetcher f) {
		this.fetcher = f;
	}

	AuthorInfo getAuthor() {
		return author;
	}

	void setAuthor(AuthorInfo author) {
		this.author = author;
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	int getYear() {
		return year;
	}

	void setYear(int year) {
		this.year = year;
	}

	File getModel() {
		return model;
	}

	void setModel(File model) {
		this.model = model;
	}

	int getID() {
		return id;
	}

	void setID(int id) {
		this.id = id;
	}

	void setBounds(Point3f upper, Point3f lower) {
		this.upper = upper;
		this.lower = lower;
	}

	void setBounds(String upper, String lower) {
		this.upper = parsePoint(upper);
		this.lower = parsePoint(lower);
		if(this.lower == null)
			this.upper = null;
		if(this.upper == null)
			this.lower = null;	
	}

	private Point3f parsePoint(String ps) {
		if(ps == null)
			return null;
		String[] coords = ps.split(" ");
		if(coords.length != 3)
			return null;
		Point3f point = new Point3f();
		try {
			point.x = Float.parseFloat(coords[0]);
			point.y = Float.parseFloat(coords[1]);
			point.z = Float.parseFloat(coords[2]);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return point;
	}

	Point3f getUpperBound() {
		return upper;
	}

	Point3f getLowerBound() {
		return lower;
	}

	void setThumbnail(File t) {
		this.thumbnail = t;
	}

	void setDimensions(float height, float width, float length) {
		this.height = height;
		this.width = width;
		this.length = length;
	}

	float getHeight() {
		return height;
	}

	float getWidth() {
		return width;
	}

	float getLength() {
		return length;
	}

	File getThumbnail() {
		return thumbnail;
	}

	boolean hasHighDetail() {
		if(model == null)
			return false;
		
		Point3f nil = new Point3f(0, 0, 0);
		if(upper == null || lower == null) {
			Point3f[] bounds = ModelParser.calculateBounds(model);
			if(bounds != null) {
				upper = bounds[0];
				lower = bounds[1];
				fetcher.insertBounds(this, upper, lower);
			} else {
				upper = nil;
				lower = nil;
				fetcher.insertBounds(this, upper, lower);
				Gallery.getInstance().notifyStatus(this, StatusEvent.ERROR_MSG,
						"Model not loaded: " + name);
			}
		}
		
		return !upper.equals(nil) || !lower.equals(nil);
	}

	boolean hasLowDetail() {
		if(thumbnail != null && thumbnail.exists()) {
			if(hasHighDetail())
				return true;
			if(height != 0 && width != 0 && length != 0)
				return true;
		}
		return false;
	}
}
