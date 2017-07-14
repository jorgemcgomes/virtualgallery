package gui;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import util.EventListener;
import core.StatusEvent;

class StatusWindow extends JFrame implements EventListener<StatusEvent> {

	private DefaultListModel list;
	private JList jList;
	private ImageIcon errorIcon, warningIcon, okIcon;
	private JScrollPane pane;

	StatusWindow() {
		super();
		setSize(300, 300);
		setVisible(false);
		setTitle("Log window");
		setAlwaysOnTop(true);
		pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setDoubleBuffered(true);
		initList();
		initImages();
		pane.setViewportView(jList);
		add(pane);
	}

	private void initList() {
		list = new DefaultListModel();

		jList = new JList(list);
		jList.setDoubleBuffered(true);
		jList.setLayoutOrientation(JList.VERTICAL);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JLabelCellRenderer rend = new JLabelCellRenderer();
		jList.setCellRenderer(rend);	
		jList.setFixedCellHeight(17);
		jList.setVisibleRowCount(3);
	}

	private void initImages() {
		errorIcon = new ImageIcon("images/error.png");
		warningIcon = new ImageIcon("images/warning.png");
		okIcon = new ImageIcon("images/ok.png");	
	}

	private class Message {

		private String text;
		private ImageIcon icon;

		Message(String t, ImageIcon im) {
			this.text = t;
			this.icon = im;
		}

		ImageIcon getIcon() {
			return icon;
		}

		String getText() {
			return text;
		}
	}

	private class JLabelCellRenderer extends JLabel implements ListCellRenderer{

		public JLabelCellRenderer() {
			setOpaque(true);
			setIconTextGap(10);
			setBorder(new EmptyBorder(1,10,1,10));
		}

		public Component getListCellRendererComponent(JList jL, Object value, int cellIndex, 
				boolean isSelected, boolean cellHasFocus) {

			Message msg = (Message) value;
			setIcon(msg.getIcon());
			setText(msg.getText());
			if (isSelected) {
				setBackground(jL.getSelectionBackground());
			} else
				setBackground(jL.getBackground());
			
			return this;
		}
	}

	@Override
	public void eventOccurred(StatusEvent evt) {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	    String time = "[" + sdf.format(cal.getTime()) + "]";
	    String withTime = time + " " + evt.getMessage();
	    
	    Message m;
	    switch(evt.getType()) {
	    case StatusEvent.OK_MSG:
	    	m = new Message(withTime, okIcon);
	    	break;
	    case StatusEvent.WARNING_MSG:
	    	m = new Message(withTime, warningIcon);
	    	break;
	    case StatusEvent.ERROR_MSG:
	    	m = new Message(withTime, errorIcon);
	    	break;
	    default:
	    	m = new Message("Undefined error", errorIcon);	
	    }
	    
		list.add(0, m);
		jList.setSelectedIndex(0);
	}
	
	void notifyStatus(Object src, int type, String message) {
		StatusEvent evt = new StatusEvent(src, type, message);
		eventOccurred(evt);
	}
	
}
