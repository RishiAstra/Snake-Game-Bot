import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//TODO: make a new thread that runs the game loop, this will make the map panel work
public class Main {
    public static final int FIELD_SIZE_X = 17;
    public static final int FIELD_SIZE_Y = 15;
    public static final int SAMPLES_PER_GRID = 20;// number of samples to get from a square in the grid to determine its color. Lower number increases performance

    public static Test test;
    public static MapFrame mapFrame;
    public static Robot robot;
    public static int x;
    public static int y;
    public static int w;
    public static int h;
    public static int playerX = 3;
    public static int playerY = 7;
    private static Dimension screenSize;
    public static int[][] map = new int[FIELD_SIZE_Y][FIELD_SIZE_X];
    public static int[][] oldMap = new int[FIELD_SIZE_Y][FIELD_SIZE_X];
    private static boolean readyForClicks, click1done, click2done;
    private static Window boundsWindow;
    private static BufferedImage boundsScreenshot;
    private static Label click1, click2;
    private static Window overlayWindow;
    public static Thread playGameThread;
    public static Timer updateMapThread;
    public static void main(String[] args) {
        //setup a main window for organizational purposes.
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            robot = new Robot();
            Thread.sleep(2000);
            int width = (int) Math.round(screenSize.getWidth());
            int height = (int) Math.round(screenSize.getHeight());
            boundsScreenshot = robot.createScreenCapture(new Rectangle(0, 0, width, height));
            boundsWindow = new Window(mapFrame);
            boundsWindow.setLocation(0, 0);
            boundsWindow.setSize(width, height);
            boundsWindow.setAlwaysOnTop(true);
            boundsWindow.setVisible(true);
//            click1 = new Label("Click upper left corner of playfield");
//            click2 = new Label("Click lower right corner of playfield");
//            boundsWindow.add((click1));
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
//                boundsWindow.remove(click1);
//                boundsWindow.add(click2);
            }else if (!click2done){
                w = e.getX() - x;
                h = e.getY() - y;
                click2done = true;
                readyForClicks = false;
//                boundsWindow.remove(click2);
                SetupPlay();
                playGameThread = new Thread(new PlayGame());
                playGameThread.start();
                updateMapThread = new Timer(30, new UpdateMap());
                updateMapThread.start();
            }
        }

    }
    public static void SetupPlay(){
        boundsWindow.dispose();
//        mapFrame = new MapFrame();
        test = new Test();
        test.main1(null);
        Window overlayWindow = new Window(mapFrame)
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
//        readPlayingField(false);
//        while(true){
//            try{
//                readPlayingField(true);
//                for(int yy = 0; yy < FIELD_SIZE_Y;yy++){
//                    for(int xx = 0; xx < FIELD_SIZE_X;xx++) {
//                        if(map[yy][xx] == 2 && oldMap[yy][xx] != 2){
//                            playerX = xx;
//                            playerY = yy;
//                        }
//                    }
//                }
////                Move();
//                Thread.sleep(50);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }



}
