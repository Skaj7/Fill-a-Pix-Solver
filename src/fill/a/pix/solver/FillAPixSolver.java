/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fill.a.pix.solver;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
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
    //set before board
    private int bHeight = 10;
    private int bWidth  = 10;
    //motion bot
    Robot bot;

    public FillAPixSolver() throws AWTException {
        bot = new Robot();
    }    
    
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        FillAPixSolver fps = new FillAPixSolver();

        Robot robot = new Robot();
        final Dimension screenSize = Toolkit.getDefaultToolkit().
                getScreenSize();
        BufferedImage screen = robot.createScreenCapture(
                new Rectangle(screenSize));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                fps.ScreenCaptureRectangle(screen);
            }
        });
        
        //number from image
        //fps.testTesseractEngine();
        
//get external data
//        fps.externalData();
        //load pdf
        //fps.loadPDF();
        //make Queue
        System.out.println("Test");
        //fps.makeQueue();
        //Run Queue algo
        //fps.runQueue();
        //print solution
        //fps.printSolution();
    }
    
    Rectangle captureRect;
    BufferedImage screenCopy;
    BufferedImage screen;
    JLabel screenLabel;
    JLabel selectionLabel;
    
    void ScreenCaptureRectangle(BufferedImage screen) {
        screenCopy = new BufferedImage(
                screen.getWidth(),
                screen.getHeight(),
                screen.getType());
        screenLabel = new JLabel(new ImageIcon(screenCopy));
        JScrollPane screenScroll = new JScrollPane(screenLabel);
        screenScroll.getVerticalScrollBar().setUnitIncrement(16);

        screenScroll.setPreferredSize(new Dimension(
                (int)(screen.getWidth()/2),
                (int)(screen.getHeight()/2)));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(screenScroll, c);

        selectionLabel = new JLabel(
                "Drag a rectangle in the screen shot!");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(selectionLabel, c);
        
        JLabel t1 = new JLabel(
                "Number of colums");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(t1, c);
        
        JTextField colums = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 3;
        panel.add(colums, c);
        
        JLabel t2 = new JLabel(
                "Number of rows");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 4;
        panel.add(t2, c);
        
        JTextField rows = new JTextField();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 5;
        panel.add(rows, c);

        repaint(screen, screenCopy);
        screenLabel.repaint();

        screenLabel.addMouseMotionListener(new MouseMotionAdapter() {

            Point start = new Point();

            @Override
            public void mouseMoved(MouseEvent me) {
                start = me.getPoint();
                repaint(screen, screenCopy);
                selectionLabel.setText("Start Point: " + start);
                String temp = colums.getText();
                screenLabel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                Point end = me.getPoint();
                captureRect = new Rectangle(start,
                        new Dimension(end.x-start.x, end.y-start.y));
                repaint(screen, screenCopy);
                screenLabel.repaint();
                selectionLabel.setText("Rectangle: " + captureRect);
            }
        });

        JOptionPane.showMessageDialog(null, panel);
        
        //Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = bot.createScreenCapture(captureRect);
        try {
            ImageIO.write(capture, "png", new File("Board.png"));
        } catch (IOException ex) {
            Logger.getLogger(FillAPixSolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        bWidth = Integer.parseInt(colums.getText());
        bHeight = Integer.parseInt(rows.getText());

        System.out.println("Rectangle of interest: " + captureRect);
        System.out.println("box width+height " + bWidth + " " + bHeight );
        processGame();
    }

    public void repaint(BufferedImage orig, BufferedImage copy) {
        Graphics2D g = copy.createGraphics();
        g.drawImage(orig,0,0, null);
        if (captureRect!=null) {
            g.setColor(Color.RED);
            g.draw(captureRect);
            g.setColor(new Color(255,255,255,150));
            g.fill(captureRect);
        }
        g.dispose();
    }
   
    
    public void makeQueue(){
        System.out.println("test");
        //create queue
        pq = new LinkedList(); 
        pqd = new LinkedList();
        //create Squares
        allSquares = new Square[bWidth][bHeight];
        createSquares(bHeight, bWidth);
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
        eX = Math.min(group.x + 1, bWidth-1);
        sY = Math.max(group.y - 1, 0);
        eY = Math.min(group.y + 1, bHeight-1);
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
    static private MyGlassPane myGlassPane;
    private static final Color tranparentBlack = new Color(0, 0, 0, 1);
    
    
    
    private static void createAndShowGUI(FillAPixSolver fps) {       
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setBackground(tranparentBlack);

        myGlassPane = new MyGlassPane(fps, frame);

        frame.add(myGlassPane);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
    
    private void loadPDF() throws FileNotFoundException, IOException, AWTException, Exception {
        
        createAndShowGUI(this);
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                
//            }
//        });
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
    
    private void UserButtonActionPerformed(java.awt.event.ActionEvent evt) {
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                System.out.println("PRINT THIS AFTER MOUSE CLICK");
            //removeMouseListener(this);
            }
        });
    }    
    
    void processGame(){
        interpretBoard();
        runQueue();
        displaySolution();
    }

    void processGame(Point upperLeft, Point lowerRight) {
        System.out.println("test");
        calibrateBoard(upperLeft, lowerRight);
        displaySolution();
    }
    
    //in pixels
    private int xStep, yStep; 
    private Point origin;
    
    void calibrateBoard(Point upperLeft, Point lowerRight) { 
        xStep = (int)Math.floor((double)(lowerRight.x - upperLeft.x) / bWidth);
        yStep = (int)Math.floor((double)(lowerRight.y - upperLeft.y) / bHeight);
        origin = new Point(upperLeft);
        origin.translate(xStep/2, yStep/2);
    }

    private void displaySolution() {
        while (!pqd.isEmpty()) {
            Group g = (Group)pqd.pollFirst();
            bot.mouseMove(origin.x+(xStep*g.x), origin.y+(yStep*g.y));
            bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            bot.delay(50);
        }
        
    }


    private void interpretBoard() {
        //create queue
        pq = new LinkedList(); 
        pqd = new LinkedList();
        //create Squares
        allSquares = new Square[bWidth][bHeight];
        createSquares(bHeight, bWidth);
        calibrateBoard(new Point(captureRect.x, captureRect.y), new Point(captureRect.x+captureRect.width, captureRect.y+captureRect.height));
        //create groups
        List<Data> data;
        data = recognizeSquares();
        CreateGroups(data);
    }

    private List<Data> recognizeSquares() {
        List<Data> data = new ArrayList<>();
        //split board
        final BufferedImage source;
        Tesseract tesseract = new Tesseract(); 
        tesseract.setDatapath("C:/Users/Kaj75/Desktop/Project/Tess4J/tessdata"); 
        try {
            source = ImageIO.read(new File("Board.png"));
            int padding = 11;
            for (int x = 0; x < bWidth; x++) {
                for (int y = 0; y < bHeight; y++) {
                    File image = new File("PositionX" + x + "Y" + y + ".png");
                    int frameX = x*xStep+padding;
                    int frameY = y*yStep+padding;
                    int frameWidth = xStep-padding;
                    int frameHeight = yStep-padding;
                    if(x == bWidth-1){
                        frameWidth = captureRect.width - frameX - padding;
                    }
                    if(y == bHeight-1){
                        frameHeight = captureRect.height - frameY - padding;
                    }
                    //ImageIO.write(source.getSubimage(frameX, frameY, frameWidth, frameHeight), "png", image);
                    String text = tesseract.doOCR(image);
                    if(x==5){
                        int a=2;
                    }
                    text = text.replaceAll("o", "0");
                    text = text.replaceAll("O", "0");
                    text = text.replaceAll("[^0-9]", "");
                    if(!text.isEmpty()){
                        data.add(new Data(Integer.parseInt(text), x, y));
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FillAPixSolver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TesseractException ex) {
            Logger.getLogger(FillAPixSolver.class.getName()).log(Level.SEVERE, null, ex);
        }

        //using tesseract to fill data points
        return data;
    }
}

class MyGlassPane extends JComponent {

    public MyGlassPane(FillAPixSolver fps, JFrame frame) {
        MyMouseListener listener = new MyMouseListener(fps, frame);
        addMouseListener(listener);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
}

