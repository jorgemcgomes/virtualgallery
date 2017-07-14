package gui.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Test3 extends JFrame {
  public Test3() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container content = getContentPane();
    FloorTab ft = new FloorTab();
    content.add(ft, BorderLayout.CENTER);
    for (int i=0; i<5; i++) {
      JPanel jp = new JPanel();
      jp.setBorder(BorderFactory.createTitledBorder("tab-"+i));
      ft.addTab("Tab-" + i, jp);
    }
    setSize(200,500);
    setVisible(true);
  }
  public static void main(String[] args) { new Test3(); }
}
class FloorTab extends JPanel implements ActionListener {
  GridBagConstraints gbc = new GridBagConstraints(0,1,1,1,1.0,1.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0,0,0,0),0,0);
  GridBagLayout gbl = new GridBagLayout();
  CardLayout cl = new CardLayout();
  JPanel panels = new JPanel(cl);
  java.util.ArrayList buttons = new java.util.ArrayList();
  public FloorTab() {
    setLayout(gbl);
    add(panels, gbc);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weighty=0.0;
  }
  public void addTab(String name, JPanel panel) {
    gbc.gridy = getComponentCount();
    if (gbc.gridy==1) gbc.gridy=0;
    JButton jb = new JButton(name);
    add(jb,gbc);
    buttons.add(jb);
    jb.addActionListener(this);
    panels.add(name,panel);
  }
  public void actionPerformed(ActionEvent ae) {
    int y=0;
    GridBagConstraints tmp;
    JButton srcButton = (JButton)ae.getSource();
    for (int i=0; i<buttons.size(); i++) {
      JButton jb = (JButton)buttons.get(i);
      tmp = gbl.getConstraints(jb);
      tmp.gridy = y++;
      gbl.setConstraints(jb, tmp);
      if (srcButton==jb) {
        tmp = gbl.getConstraints(panels);
        tmp.gridy = y++;
        gbl.setConstraints(panels, tmp);
      }
    }
    cl.show(panels,srcButton.getText());
  }
}