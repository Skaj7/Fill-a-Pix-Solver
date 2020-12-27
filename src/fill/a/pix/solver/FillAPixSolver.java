/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fill.a.pix.solver;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 *
 * @author Kaj75
 */
public class FillAPixSolver {

    private LinkedList pq, pqd;
    private Square[][] allSquares;
    private int progress;
    //set before
    private int height = 10;
    private int width  = 10;
    
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        FillAPixSolver fps = new FillAPixSolver();
        //get external data
//        fps.externalData();
        //load pdf
        fps.loadPDF();
        //make Queue
        System.out.println("Test");
        fps.makeQueue();
        //Run Queue algo
        fps.runQueue();
        //print solution
        fps.printSolution();
    }
    
    public void makeQueue(){
        System.out.println("test");
        //create queue
        pq = new LinkedList(); //PriorityQueue<Group>();
        pqd = new LinkedList();
        //create Squares
        allSquares = new Square[width][height];
        createSquares(height, width);
        //create groups
        List<Data> data;
        data = testData();
        CreateGroups(data);
    }

    private void createSquares(int height, int width) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                allSquares[j][i] = new Square(j, i);
            }
        }
    }

    private List<Data> testData() {
        List<Data> data = new ArrayList<>();
        data.add(new Data(2, 1, 0));
        data.add(new Data(3, 2, 0));
        data.add(new Data(0, 5, 0));
        data.add(new Data(3, 4, 1));
        data.add(new Data(2, 6, 1));
        data.add(new Data(6, 9, 1));
        
        data.add(new Data(5, 2, 2));
        data.add(new Data(5, 4, 2));
        data.add(new Data(3, 5, 2));
        data.add(new Data(5, 7, 2));
        data.add(new Data(7, 8, 2));
        data.add(new Data(4, 9, 2));
        
        data.add(new Data(4, 1, 3));
        data.add(new Data(5, 3, 3));
        data.add(new Data(5, 5, 3));
        data.add(new Data(6, 7, 3));
        data.add(new Data(3, 9, 3));
        
        data.add(new Data(4, 2, 4));
        data.add(new Data(5, 4, 4));
        data.add(new Data(6, 6, 4));
        data.add(new Data(3, 9, 4));
        
        data.add(new Data(2, 3, 5));
        data.add(new Data(5, 5, 5));
        
        data.add(new Data(4, 0, 6));
        data.add(new Data(1, 2, 6));
        data.add(new Data(1, 6, 6));
        data.add(new Data(1, 7, 6));
        
        data.add(new Data(4, 0, 7));
        data.add(new Data(1, 2, 7));
        data.add(new Data(1, 6, 7));
        data.add(new Data(4, 8, 7));
        
        data.add(new Data(6, 4, 8));
        data.add(new Data(4, 9, 8));
        
        data.add(new Data(4, 1, 9));
        data.add(new Data(4, 2, 9));
        data.add(new Data(4, 7, 9));
        
        return data;
    }


    
    public void CreateGroups(List<Data> data){
        for (int i = 0; i < data.size(); i++) {
            Group group = new Group(data.get(i)); 
            group.setSquares(findSquaresGroup(group));
            pq.add(group);
        }
        return;
    }

    private List<Square> findSquaresGroup(Group group) {
        List<Square> squares = new ArrayList<>();
        int sX, eX, sY, eY;
        sX = Math.max(group.x - 1, 0);
        eX = Math.min(group.x + 1, width-1);
        sY = Math.max(group.y - 1, 0);
        eY = Math.min(group.y + 1, height-1);
        for (int i = sY; i <= eY; i++) {
            for (int j = sX; j <= eX; j++) {
                squares.add(allSquares[j][i]);
            }
        }
        return squares;
    }

    private void runQueue() {
        progress = pq.size();
        int start = progress;
        while(!pq.isEmpty()){
            //take group
            Group g = (Group)pq.poll();
            //check done 
            
            //update percentages + advanced logic
            //updateSquarePercentages(g);
            //check certainty 100% and 0%
            //update Square values
            updateSquaresState(g);
            //if all squares of group filled
            if(!squaresMarked(g)){
                //add to bottom of queue
                pq.addLast(g);
            }else{
                pqd.addLast(g);
            }       
            if(pq.size() != progress){
                progress = pq.size();
                System.out.println("Done " + (start-progress) + " Of the "+ start);
            }
        }
    }

    private boolean squaresMarked(Group g) {
        for (Square s : g.getSquares()) {
            if(s.getState() == State.Clear){
                return false;
            }
        }
        return true;
    }

    private void updateSquarePercentages(Group g) {
        //leftover squares
        //divider
        
    }

    //simple logic
    private void updateSquaresState(Group g) {
        int size = g.getSquares().size();
        int marks = g.getNumberOfMarked();
        int blanks = g.getNumberOfBlank();
        int clear = g.getNumberOfClear();
        
        if(size == g.getNumber()){
            setMark(g.getSquares());
        }else if(g.getNumber() == 0){
            setBlank(g.getSquares());
        }else if(marks == g.getNumber()){
            setBlank(g.getSquares());
        }else if(blanks == (size - g.getNumber())){
            setMark(g.getSquares());
        }
    }

    private void setMark(List<Square> squares) {
        for (Square square : squares) {
            if(square.getState() == State.Clear){
                square.setState(State.Mark);
            }
        }
    }

    private void setBlank(List<Square> squares) {
        for (Square square : squares) {
            if(square.getState() == State.Clear){
                square.setState(State.Blank);
            }
        }
    }

    private void printSolution() {
        while (!pqd.isEmpty()) {            
            Group g = (Group)pqd.pollFirst();
            System.out.println("X = " + g.x);
            System.out.println("Y = " + g.y);
            System.out.println("Number = " + g.getNumber());
            System.out.println(" ");
            int a = 3;
        }
    }

    private static JFrame frame;
    private static JDesktopPane desktopPane;
    private static JInternalFrame internalFrame;
    private static int FRAME_SIZE = 500;
    private static Color BACKGROUND_COLOR = Color.BLUE;
    
    private void loadPDF() throws FileNotFoundException, IOException, AWTException, Exception {
          
        Robot robot = new Robot();
        robot.setAutoDelay(20);
        SwingUtilities.invokeAndWait(() -> {
            createAndShowGUI();
        });
        robot.waitForIdle();
        
//        SwingUtilities.invokeAndWait(() -> {
//            UserButtonActionPerformed();
//        });

        final int translate = FRAME_SIZE / 4;
        moveFrame(robot, translate, translate / 2, translate / 2);
        robot.waitForIdle();

        Point p = getDesktopPaneLocation();
        int size = translate / 2;
        Rectangle rect = new Rectangle(p.x, p.y, size, size);
        BufferedImage img = robot.createScreenCapture(rect);
        
        
        File file = new File("C:\\Users\\Kaj75\\Desktop\\Project\\test.png");
        ImageIO.write(img, "png", file);
        MouseInfo.getPointerInfo().getLocation();

        int testRGB = BACKGROUND_COLOR.getRGB();
        for (int i = 0; i < size; i++) {
            int rgbCW = img.getRGB(i, size / 2);
            int rgbCH = img.getRGB(size / 2, i);
            if (rgbCW != testRGB || rgbCH != testRGB) {
                throw new RuntimeException("Background color is wrong!");
            }
        }
    }
    
    private static void moveFrame(Robot robot, int w, int h, int N) throws Exception {
        Point p = getInternalFrameLocation();
        int xs = p.x + 100;
        int ys = p.y + 15;
        robot.mouseMove(xs, ys);
        try {
            robot.mousePress(InputEvent.BUTTON1_MASK);

            int dx = w / N;
            int dy = h / N;

            int y = ys;
            for (int x = xs; x < xs + w; x += dx, y += dy) {
                robot.mouseMove(x, y);
            }
        } finally {
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
    }
    
    private static void createAndShowGUI() {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        desktopPane = new JDesktopPane();
        desktopPane.setBackground(BACKGROUND_COLOR);

        frame.add(desktopPane, BorderLayout.CENTER);
        frame.setSize(FRAME_SIZE, FRAME_SIZE);
        frame.setVisible(true);

        internalFrame = new JInternalFrame("Test");
        internalFrame.setSize(FRAME_SIZE / 2, FRAME_SIZE / 2);
        desktopPane.add(internalFrame);
        internalFrame.setVisible(true);
        internalFrame.setResizable(true);

        frame.setVisible(true);
    }

    private static Point getInternalFrameLocation() throws Exception {
        final Point[] points = new Point[1];
        SwingUtilities.invokeAndWait(() -> {
            points[0] = internalFrame.getLocationOnScreen();
        });
        return points[0];
    }

    private static Point getDesktopPaneLocation() throws Exception {
        final Point[] points = new Point[1];
        SwingUtilities.invokeAndWait(() -> {
            points[0] = desktopPane.getLocationOnScreen();
        });
        return points[0];
    }

//    private void externalData() throws InterruptedException, InvocationTargetException {
//        myMouseListener mml;
//        SwingUtilities.invokeAndWait(() -> {
//            mml = new myMouseListener();
//        });
//    }
    
    private void UserButtonActionPerformed(java.awt.event.ActionEvent evt) {
        addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
            System.out.println("PRINT THIS AFTER MOUSE CLICK");
            //removeMouseListener(this);
        }
    });
}
    
}
