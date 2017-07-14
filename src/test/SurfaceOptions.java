package gui.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import util.EventListener;

import app.Gallery;
import app.mode.surface.*;

class SurfaceOptions extends ModeOptions implements EventListener<SurfaceEvent> {

	private SurfaceMode surfMode;

	private JTextField area, selected, detected;
	private JCheckBox xNormal, yNormal, zNormal;
	private JButton applyFilters, clearSelection;
	private JRadioButton selectSurfaces, rejectSurfaces;

	SurfaceOptions() {
		super("Surface Detection");
		surfMode = Gallery.getInstance().getSurfaceMode();
		showOptions();
		surfMode.addEventListener(this);
	}

	private void showOptions() {
		showArea();
		showNormal();
		showButtons();
		showInfos();
		super.validate();
	}

	private void showArea() {
		addSubtitle("Area Filter");
		panel.add(new JLabel("Minimum Area:"));
		area = new JTextField(5);
		area.setToolTipText("<HTML>Type the minimum area a surface<BR>should have" +
		" to be picked</HTML>");
		area.setText("0.0");
		panel.add(area);
		panel.add(new JLabel("m\u00B2"));
		changeLine(5);
	}

	private void showNormal() {
		addSubtitle("Normal Filter");
		JLabel exp = new JLabel("<HTML>Components allowed for the<BR>" +
		" surfaces normals</HTML>");
		panel.add(exp);
		changeLine(0);
		xNormal = new JCheckBox("X", true);
		yNormal = new JCheckBox("Y", true);
		zNormal = new JCheckBox("Z", true);
		panel.add(xNormal);
		panel.add(yNormal);
		panel.add(zNormal);
		changeLine(5);
	}

	private void showButtons() {
		addSubtitle("Opções");
		
		selectSurfaces = new JRadioButton("Select surfaces");
		selectSurfaces.setSelected(true);
		rejectSurfaces = new JRadioButton("Reject surfaces");
		
	    ButtonGroup group = new ButtonGroup();
	    group.add(selectSurfaces);
	    group.add(rejectSurfaces);
		
		panel.add(selectSurfaces);
		changeLine(0);
		panel.add(rejectSurfaces);
		changeLine(0);
		
		applyFilters = new JButton("Selecção automática");
		applyFilters.setPreferredSize(new Dimension(150, 30));
		applyFilters.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				float a;
				try {
					a = Float.parseFloat(area.getText());
				} catch(NumberFormatException e) {
					a = 0.0f;
					area.setText("0.0");
					GUIController.getInstance().warningStatusMessage(
							"Formato de número inválido - filtro não utilizado. Escreve apenas um " +
							"número inteiro ou decimal.");
				}
				surfMode.clearFilters();
				surfMode.toggleAreaFilter(a);
				surfMode.toggleNormalFilter(xNormal.isSelected(), 
						yNormal.isSelected(), zNormal.isSelected());
				int n = 0;
				if(selectSurfaces.isSelected())
					surfMode.autoPick();
				else if(rejectSurfaces.isSelected())
					surfMode.autoReject();
				GUIController.getInstance().normalStatusMessage(n + " novas " +
						"superfícies automaticamente seleccionadas.");
			}
		});

		clearSelection = new JButton("Limpar selecção");
		clearSelection.setPreferredSize(new Dimension(150, 30));
		clearSelection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				surfMode.clearPicks();
			}
			
		});		
		
		panel.add(applyFilters);
		changeLine(0);
		panel.add(clearSelection);
		changeLine(5);
	}

	private void showInfos() {
		addSubtitle("Informações");
		detected = new JTextField(4);
		detected.setEditable(false);
		panel.add(detected);
		panel.add(new JLabel("sup. detectadas"));
		changeLine(0);
		selected = new JTextField(4);
		selected.setEditable(false);
		panel.add(selected);
		panel.add(new JLabel("sup. seleccionadas "));
	}

	@Override
	void activate() {
		scrollUp();
		surfMode.activate();
	}

	@Override
	void terminate() {
		surfMode.terminate();
	}

	@Override
	int height() {
		return 600;
	}

	@Override
	public void eventOccurred(SurfaceEvent e) {
		selected.setText(Integer.toString(e.getnSelected()));
		detected.setText(Integer.toString(e.getnDetected()));
		
		if(e.getSource() instanceof Surface) {
			Surface s = (Surface) e.getSource();
			if(s.isSelectable()) {
				String m = String.format("Superfície seleccionada. " +
						"Área: %.3f ; Normal: (%+.3f, %+.3f, %+.3f)", s.area(),
						s.getNormal().x, s.getNormal().y, s.getNormal().z);			
				GUIController.getInstance().normalStatusMessage(m);
			} 
		}
		
	}

}
