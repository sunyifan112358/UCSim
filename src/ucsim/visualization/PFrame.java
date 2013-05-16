/**
  Part of the UCSim project

  Copyright (c) 2013-13 Yifan Sun

  This library is free software; you can redistribute it and/or
  modify it under the terms of version 2.01 of the GNU Lesser General
  Public License as published by the Free Software Foundation.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library
*/
package ucsim.visualization;

import javax.swing.JFrame;

import processing.core.PApplet;

/**
 * Visualization windows
 * @author yifan
 *
 */
public class PFrame extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param p Processing Applet 
     */
    public PFrame(PApplet p) {
        setBounds(50,50,880,640);
        add(p);
        p.init();
        setVisible(true);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

}
