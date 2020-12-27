/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fill.a.pix.solver;

import java.awt.AWTEvent;
import java.awt.MouseInfo;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author Kaj75
 */
public class MyMouseListener extends MouseInputAdapter{
    

    public void mouseClicked(MouseEvent e) {
        System.out.println("clicked");
        int a = 5;
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("pressed");
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("release");
    }

    public void mouseEntered(MouseEvent e) {
        System.out.println("enter");
    }

    public void mouseExited(MouseEvent e) {
        System.out.println("exit");
    }
    
}
