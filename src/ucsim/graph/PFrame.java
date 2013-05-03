package ucsim.graph;

import javax.swing.JFrame;
import processing.core.PApplet;

public class PFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public PFrame(PApplet p) {
        setBounds(50,50,880,640);
        add(p);
        p.init();
        setVisible(true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    }
}

