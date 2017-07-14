package gui;

import java.awt.Cursor;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

abstract class LongOperation extends SwingWorker<Void, Void> {
	
	private WaitDialog wait;
	private JFrame frame;
	private String message;
	
	LongOperation(JFrame frame, String message) {
		this.frame = frame;
		this.message = message;
	}

	@Override
	protected Void doInBackground() {
		wait = new WaitDialog(frame, message);
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		wait.setVisible(true);
		wait.setLocationRelativeTo(frame);
		callOperation();
		return null;
	}
	
	@Override
	public void done() {
		wait.setVisible(false);
		wait.dispose();
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	abstract void callOperation();
	
	
	
}
