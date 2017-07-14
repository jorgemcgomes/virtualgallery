package core;

import java.io.File;
import java.io.Serializable;

class ProjectConfiguration implements Serializable {

	private static final long serialVersionUID = -5513148653308771757L;
	
	private File originalFile;
	private String title;
	private boolean fastMode;
	
	public File getOriginalFile() {
		return originalFile;
	}
	
	void setOriginalFile(File originalFile) {
		this.originalFile = originalFile;
	}
	
	public String getTitle() {
		return title;
	}
	
	void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isFastMode() {
		return fastMode;
	}
	
	void setFastMode(boolean fastMode) {
		this.fastMode = fastMode;
	}
	
}
