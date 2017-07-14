package modules.art2d;

import java.io.File;

import database.AuthorInfo;

class Artwork2DInfo {
	
	private int id;
	private String title;
	private AuthorInfo author;
	private int year;
	private String type;

	private File image;
	private float height;
	private float width;
	
	
	int getID() {
		return id;
	}
	
	File getImage() {
		return image;
	}
	
	float getWidth() {
		return width;
	}
	
	float getHeight() {
		return height;
	}
	
	String getTitle() {
		return title;
	}
	
	AuthorInfo getAuthor() {
		return author;
	}
	
	int getYear() {
		return year;
	}

	void setId(int id) {
		this.id = id;
	}

	void setTitle(String title) {
		this.title = title;
	}

	void setAuthor(AuthorInfo author) {
		this.author = author;
	}

	void setYear(int year) {
		this.year = year;
	}

	void setImage(File image) {
		this.image = image;
	}

	void setHeight(float height) {
		this.height = height;
	}

	void setWidth(float width) {
		this.width = width;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
