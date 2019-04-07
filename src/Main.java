import com.sun.java.swing.plaf.windows.WindowsGraphicsUtils;
import org.omg.CORBA.Environment;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Main {
    public static Frame mainFrame;
    private static Robot robo;
    private static int x;
    private static int y;
    private static int w;
    private static int h;
    private static Dimension screenSize;
    private static int[][] map = new int[17][17];
    private static boolean readyForClicks, click1done, click2done;
    private static Window boundsWindow;
    private static BufferedImage boundsScreenshot;
    private static Label click1, click2;
    public static void main(String[] args) {
        //setup a main window for organizational purposes.
        mainFrame = new Frame("AutoStuff");
        mainFrame.setSize(400, 200);
        TextArea mainFrameInfo = new TextArea("This is the main window for AutoStuff. This software is meant to automate the snake game.");
        mainFrame.add(mainFrameInfo);
        mainFrame.setVisible(true);
        System.out.println(Window.getWindows().length);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            robo = new Robot();
            Thread.sleep(2000);
            int width = (int) Math.round(screenSize.getWidth());
            int height = (int) Math.round(screenSize.getHeight());
            boundsScreenshot = robo.createScreenCapture(new Rectangle(0, 0, width, height));
            boundsWindow = new Window(mainFrame);
            boundsWindow.setLocation(0, 0);
            boundsWindow.setSize(width, height);
            boundsWindow.setAlwaysOnTop(true);
            boundsWindow.setVisible(true);
            click1 = new Label("Click upper left corner of playfield");
            click2 = new Label("Click lower right corner of playfield");
            boundsWindow.add((click1));
            boundsWindow.addMouseListener(new MouseListen());

            Graphics boundsGraphics = boundsWindow.getGraphics();
            boundsGraphics.drawImage(boundsScreenshot, 0, 0, width, height, boundsWindow);
            readyForClicks = true;
//            while (true) {
//            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void MouseClicked(MouseEvent e){
        if(readyForClicks){
            if(!click1done){
                x = e.getX();
                y = e.getY();
                click1done = true;
                boundsWindow.remove(click1);
                boundsWindow.add(click2);
            }else if (!click2done){
                w = e.getX() - x;
                h = e.getY() - y;
                click2done = true;
                readyForClicks = false;
                boundsWindow.remove(click2);
                SetupPlay();
            }
        }

    }
    public static void SetupPlay(){
        boundsWindow.dispose();
        Window overlayWindow = new Window(mainFrame)
        {
            @Override
            public void paint(Graphics g)
            {
                int x1 = x - 1;
                int y1 = y - 1;
                int x2 = x+w-1;
                int y2 = y+h-1;
                g.drawRect(x1, y1, 2, 2);
                g.drawRect(x2, y1, 2, 2);
                g.drawRect(x1, y2, 2, 2);
                g.drawRect(x2, y2, 2, 2);
            }
            @Override
            public void update(Graphics g)
            {
                paint(g);
            }
        };
        overlayWindow.setAlwaysOnTop(true);
        overlayWindow.setBounds(overlayWindow.getGraphicsConfiguration().getBounds());
        overlayWindow.setBackground(new Color(0, true));
        overlayWindow.setFocusable(false);
        overlayWindow.setAlwaysOnTop(true);
        overlayWindow.setVisible(true);
        try {
            Thread.sleep(50);
            robo.keyPress(38);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void readPlayingField(){

    }

    public static void loop(){

    }
}
