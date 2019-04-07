import com.sun.java.swing.plaf.windows.WindowsGraphicsUtils;
import org.omg.CORBA.Environment;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Main {
    public static final int FIELD_SIZE_X = 17;
    public static final int FIELD_SIZE_Y = 15;
    public static final int SAMPLES_PER_GRID = 20;// number of samples to get from a square in the grid to determine its color. Lower number increases performance

    public static Frame mainFrame;
    private static Robot robo;
    private static int x;
    private static int y;
    private static int w;
    private static int h;
    private static int playerX = 3;
    private static int playerY = 7;
    private static Dimension screenSize;
    private static int[][] map = new int[FIELD_SIZE_Y][FIELD_SIZE_X];
    private static int[][] oldMap = new int[FIELD_SIZE_Y][FIELD_SIZE_X];
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
        mainFrame.setState(Frame.ICONIFIED);
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

        readPlayingField(false);
        while(true){
            try{
                readPlayingField(true);
                for(int yy = 0; yy < FIELD_SIZE_Y;yy++){
                    for(int xx = 0; xx < FIELD_SIZE_X;xx++) {
                        if(map[yy][xx] == 2 && oldMap[yy][xx] != 2){
                            playerX = xx;
                            playerY = yy;
                        }
                    }
                }
                Move();
                Thread.sleep(50);
            }catch (Exception e){
                System.err.println(e);
            }
        }
    }


    public static void readPlayingField(boolean updateOldMap){
        if(updateOldMap){
            for(int yy = 0; yy < FIELD_SIZE_Y;yy++){
                for(int xx = 0; xx < FIELD_SIZE_X;xx++) {
                    oldMap[yy][xx] = map[yy][xx];
                }
            }
        }

        BufferedImage img = robo.createScreenCapture(new Rectangle(x, y, w, h));
        int stepx = (int)Math.floor(((double)w)/FIELD_SIZE_X);
        int stepy = (int)Math.floor(((double)h)/FIELD_SIZE_Y);
        for(int yy = 0; yy < FIELD_SIZE_Y;yy++){
            for(int xx = 0; xx < FIELD_SIZE_X;xx++){
                int type = 0;
                int avgR = 0;
                int avgG = 0;
                int avgB = 0;
                int[] pixels = img.getRGB((int)Math.floor(xx * w / FIELD_SIZE_X), (int)Math.floor(yy * h / FIELD_SIZE_Y), stepx, stepy, null, 0, stepx);
                int samples = 0;
                for(int i = 0;i < pixels.length;i+=Math.round(pixels.length/SAMPLES_PER_GRID)){
                    Color temp = new Color(pixels[i]);
                    avgR += temp.getRed();
                    avgG += temp.getGreen();
                    avgB += temp.getBlue();
                    samples++;
                }
                avgR = Math.round(avgR/samples);
                avgG = Math.round(avgG/samples);
                avgB = Math.round(avgB/samples);

                if(avgR > 128){
                    type = 2;//apple
                }
                if(avgG > 128){
                    type = 0;//blank
                }
                if(avgB > 128){
                    type = 1;
                }
                map[yy][xx] = type;
            }
        }
        System.out.println("Read map");
    }

    public static void Move(){
        int rx = -1;
        int ry = -1;
        for(int yy = 0;yy < FIELD_SIZE_Y;yy++){
            for(int xx = 0;xx < FIELD_SIZE_X;xx++){
                if(map[yy][xx] == 2){
                    rx = xx;
                    ry = yy;
                }
            }
        }

        int[][] count = new int[FIELD_SIZE_Y][FIELD_SIZE_X];
        for(int yy = 0;yy < FIELD_SIZE_Y;yy++){
            for(int xx = 0;xx < FIELD_SIZE_X;xx++){
                count[yy][xx] = 1000;
            }
        }
        ArrayList<Integer> xdone = new ArrayList<Integer>();
        ArrayList<Integer> ydone = new ArrayList<Integer>();
        count [ry][rx] = 0;
        xdone.add(rx);
        ydone.add(ry);
        int done = 0;
        while(done < FIELD_SIZE_X * FIELD_SIZE_Y) {
            ArrayList<Integer> newx = new ArrayList<Integer>();
            ArrayList<Integer> newy = new ArrayList<Integer>();
            for (int i = 0; i < xdone.size(); i++) {
                int num = count[ydone.get(i)][xdone.get(i)] + 1;
                boolean top = ydone.get(i) > 0;
                boolean bottom = ydone.get(i) < FIELD_SIZE_Y - 1;
                boolean left = xdone.get(i) > 0;
                boolean right = xdone.get(i) < FIELD_SIZE_X - 1;
                /////////////////////// top left 1
                int xxx = xdone.get(i) - 1;
                int yyy = ydone.get(i) - 1;
                if(top && left && count[yyy][xxx] > num){
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
                /////////////////////// top middle 2
                xxx = xdone.get(i);
                yyy = ydone.get(i) - 1;
                if(top && count[yyy][xxx] > num){
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
                /////////////////////// top right 3
                xxx = xdone.get(i) + 1;
                yyy = ydone.get(i) - 1;
                if(top && right && count[yyy][xxx] > num){
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
                /////////////////////// middle left 4
                xxx = xdone.get(i) - 1;
                yyy = ydone.get(i);
                if(left && count[yyy][xxx] > num){
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
                /////////////////////// middle right 5
                xxx = xdone.get(i) + 1;
                yyy = ydone.get(i);
                if(right && count[yyy][xxx] > num){
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
                /////////////////////// bottom left 6
                xxx = xdone.get(i) - 1;
                yyy = ydone.get(i) + 1;
                if(bottom && left && count[yyy][xxx] > num){
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
                /////////////////////// bottom middle 7
                xxx = xdone.get(i);
                yyy = ydone.get(i) + 1;
                if(bottom && count[yyy][xxx] > num){
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
                ///////////////////////bottom right 8
                xxx = xdone.get(i) + 1;
                yyy = ydone.get(i) + 1;
                if(bottom && right && count[yyy][xxx] > num){
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
            }
            done += newx.size();
            xdone = newx;
            ydone = newy;
        }

        // count map has been made now

        int dir = 0;//target direction 0=top, 1=right, 2=bottom, 3=left
        int score = 1000;//how good is this move

        if(playerY > 0 && count[playerY - 1][playerX] < score){
            dir = 0;
            score = count[playerY - 1][playerX];
        }
        if(playerX < FIELD_SIZE_X - 1 && count[playerY][playerX + 1] < score){
            dir = 1;
            score = count[playerY][playerX + 1];
        }
        if(playerY < FIELD_SIZE_Y - 1 && count[playerY + 1][playerX] < score){
            dir = 2;
            score = count[playerY + 1][playerX];
        }
        if(playerX > 0 && count[playerY][playerX - 1] < score){
            dir = 3;
            score = count[playerY][playerX - 1];
        }
        robo.keyRelease(37);
        robo.keyRelease(38);
        robo.keyRelease(39);
        robo.keyRelease(40);
        if(dir == 0) robo.keyPress(38);
        if(dir == 1) robo.keyPress(39);
        if(dir == 2) robo.keyPress(40);
        if(dir == 3) robo.keyPress(37);
    }
    public static void loop(){

    }
}
