/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fill.a.pix.solver;

import java.awt.AWTEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author Kaj75
 */
public class MyMouseListener extends MouseInputAdapter{

    FillAPixSolver fps;
    JFrame frame;
    Point upperLeft=null, lowerRight=null;
    public MyMouseListener(FillAPixSolver fps, JFrame frame) {
        this.fps = fps;
        this.frame = frame;
    }

    public void mouseClicked(MouseEvent e) {
        if(upperLeft == null){
            upperLeft = e.getPoint();
        }else{
            lowerRight = e.getPoint();
            Thread thread = new Thread() {
                public void run(){
                    fps.processGame(upperLeft, lowerRight);
                }
            };
            thread.start();
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
        System.out.println("clicked");
    }

    public void mousePressed(MouseEvent e) {
//        System.out.println("pressed");
    }

    public void mouseReleased(MouseEvent e) {
//        System.out.println("release");
    }

    public void mouseEntered(MouseEvent e) {
//        System.out.println("enter");
    }

    public void mouseExited(MouseEvent e) {
//        System.out.println("exit");
    }
    
}
