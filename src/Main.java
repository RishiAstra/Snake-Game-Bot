import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Sets up the bot, calls other scripts.
 */
public class Main {

//    public static final int SAMPLES_PER_GRID = 20;// number of samples to get from a square in the grid to determine its color. Lower number increases performance
    public static Test test;
    public static Robot robot;
    public static int x;
    public static int y;
    public static int w;
    public static int h;


    public static Timer updateMapThread;

    private static Dimension screenSize;
    private static boolean readyForClicks, click1done, click2done;
    private static Window boundsWindow;
    private static Thread playGameThread;

    //Try creating robot, measuring screen size, and creating the screenshot window to cover the screen for bounds.
    //Also setup the mouse listener MouseListen to sense mouse clicks.
    public static void main(String[] args) {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            robot = new Robot();
            Thread.sleep(2000);
            int width = (int) Math.round(screenSize.getWidth());
            int height = (int) Math.round(screenSize.getHeight());
            BufferedImage boundsScreenshot = robot.createScreenCapture(new Rectangle(0, 0, width, height));
            boundsWindow = new Window(null);
            boundsWindow.setLocation(0, 0);
            boundsWindow.setSize(width, height);
            boundsWindow.setAlwaysOnTop(true);
            boundsWindow.setVisible(true);
            boundsWindow.addMouseListener(new MouseListen());

            Graphics boundsGraphics = boundsWindow.getGraphics();
            boundsGraphics.drawImage(boundsScreenshot, 0, 0, width, height, boundsWindow);
            readyForClicks = true;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //Used to set bounds of game using clicks. Called by MouseListen.
    public static void MouseClicked(MouseEvent e){
        if(readyForClicks){
            if(!click1done){
                x = e.getX();
                y = e.getY();
                click1done = true;
            }else if (!click2done){
                w = e.getX() - x;
                h = e.getY() - y;
                click2done = true;
                readyForClicks = false;
                SetupPlay();
                playGameThread = new Thread(new PlayGame());
                playGameThread.start();
                updateMapThread = new Timer(30, new UpdateMap());
                updateMapThread.start();
            }
        }

    }
    private static void SetupPlay(){
        boundsWindow.dispose();
        test = new Test();
        test.main1(null);
        Window overlayWindow = new Window(null)
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
    }
}
