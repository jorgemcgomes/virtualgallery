package gui;

import javax.swing.JPanel;

public class ModuleGUI {
	
	private String title, leftTitle, rightTitle;
	private JPanel leftPanel, rightPanel;
	
	public ModuleGUI(String title) {
		this.title = title;
		this.leftTitle = "";
		this.rightTitle = "";
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getLeftTitle() {
		return leftTitle;
	}

	public void setLeftTitle(String leftTitle) {
		this.leftTitle = leftTitle;
	}

	public String getRightTitle() {
		return rightTitle;
	}

	public void setRightTitle(String rightTitle) {
		this.rightTitle = rightTitle;
	}

	public JPanel getLeftPanel() {
		return leftPanel;
	}

	public void setLeftPanel(JPanel leftPanel) {
		this.leftPanel = leftPanel;
	}

	public JPanel getRightPanel() {
		return rightPanel;
	}

	public void setRightPanel(JPanel rightPanel) {
		this.rightPanel = rightPanel;
	}
}
