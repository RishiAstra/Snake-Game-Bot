import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//TODO: make snake update higher number means toward end
/**
 * Includes the main game play.
 */
public class PlayGame implements Runnable{
    public static final int FPS = 20;
    public static final int FIELD_SIZE_X = 17;
    public static final int FIELD_SIZE_Y = 15;

    public static ArrayList<int[][]> history;
    public static int historyLength = 10;
    public static int[][] map = new int[FIELD_SIZE_Y][FIELD_SIZE_X];
    public static int[][] bluenums = new int[FIELD_SIZE_Y][FIELD_SIZE_X];

    public static int[][] oldMap = new int[FIELD_SIZE_Y][FIELD_SIZE_X];
    public static int[][] playerPos = new int[FIELD_SIZE_Y][FIELD_SIZE_X];
    public static int[][] count = new int[FIELD_SIZE_Y][FIELD_SIZE_X];
    public static int rx;
    public static int ry;
    public static int dir = 1;
    public static int eatapple = 0;
    public static int ex = 0;
    public static int ey = 0;
    public static int pdir;
    public static int moveCount;
    public static int playerX = 3;
    public static int playerY = 7;
    public static int endX;
    public static int endY;
    public static int snakeLength = 2;
    public static Robot robot;
    public static int x;
    public static int y;
    public static int w;
    public static int h;
    public static int stepx;
    public static int stepy;


    public static long previousTime;
    public static long currentTime;
    public static int dead;

    /**
     * Runs the bot
     */
    @Override
    public void run(){
        previousTime = System.nanoTime();
        currentTime = previousTime + 1000;
        x = Main.x;
        y = Main.y;
        w = Main.w;
        h = Main.h;

        playerPos[playerY][playerX] = 2;
        playerPos[playerY][playerX - 1] = 1;

        try{
            robot = new Robot();
        }catch( Exception e){
            e.printStackTrace();
        }
        Test.historyIndex = 0;//set history to current map, don't view history by default
        history = new ArrayList<>();
        while(true){
            previousTime = currentTime;
            currentTime = System.nanoTime();
            try{
                long startTime = System.nanoTime();

                readPlayingField(true);
                boolean isDead = false;
                for(int yy = 0; yy < FIELD_SIZE_Y;yy++) {
                    for (int xx = 0; xx < FIELD_SIZE_X; xx++) {
                        if (map[yy][xx] == 2 && oldMap[yy][xx] != 2) {
//                            playerX = xx;
//                            playerY = yy;
                        }
                        if (map[yy][xx] != oldMap[yy][xx]) isDead = false;
                    }
                }
                if(dead > FPS){//not moved in 1 second
                    System.err.println("dead");
                    break;
                }
                Move();
                moveCount++;
                long endTime = System.nanoTime();
                double diff = (endTime - startTime)/1000000000;
                double target  = 1.0/FPS;
                double delta = target - diff;
                if(delta < 0){
                    System.err.println("Frame took a long time");
                }else{
                    Thread.sleep((long)(delta / 1000));
                }
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
    }

    /**
     * Reads the playing field, puts it into map.
     * @param updateOldMap should the old map be updated?
     */
    public static void readPlayingField(boolean updateOldMap){
        boolean changed = false;
        if(updateOldMap){
            for(int yy = 0; yy < FIELD_SIZE_Y;yy++){
                for(int xx = 0; xx < FIELD_SIZE_X;xx++) {
                    oldMap[yy][xx] = map[yy][xx];
                    changed = true;
                }
            }
        }
        if(changed){
            history.add(map);
            if(history.size() > historyLength){
                history.remove(0);
            }else{
                Test.historyIndex++;
            }
        }

        BufferedImage img = robot.createScreenCapture(new Rectangle(x, y, w, h));
        stepx = (int)Math.floor(((double)w)/FIELD_SIZE_X);
        stepy = (int)Math.floor(((double)h)/FIELD_SIZE_Y);
        for(int yy = 0; yy < FIELD_SIZE_Y;yy++){
            for(int xx = 0; xx < FIELD_SIZE_X;xx++){
                int type = 0;
                int avgR  = 0;
                int avgG  = 0;
                int avgB  = 0;
//                int[] pixels = img.getRGB((int)Math.floor(xx * w / FIELD_SIZE_X), (int)Math.floor(yy * h / FIELD_SIZE_Y), stepx, stepy, null, 0, stepx);
                int samples = 0;
                int blueNum = 0;
                for(int v = 0;v < stepy;v++){
                    for(int u = 0;u < stepx;u++){
                        Color temp = new Color(img.getRGB((int)Math.floor(xx * w / (float)FIELD_SIZE_X) + u,(int)Math.floor(yy * h / (float)FIELD_SIZE_Y) + v));
                        avgR += temp.getRed();
                        avgG += temp.getGreen();
                        if(temp.getBlue() > (temp.getGreen() + temp.getRed())/2){
                            blueNum++;
                        }
                        samples++;
                    }
                }
                bluenums[yy][xx] = Math.round(10 * blueNum / ((float)stepx * stepy));
//
//                for(int i = 0;i < pixels.length;i++){//=Math.round(pixels.length/SAMPLES_PER_GRID)){
//                    Color temp = new Color(pixels[i]);
//                    avgR += temp.getRed();
//                    avgG += temp.getGreen();
//                    if(temp.getBlue() > temp.getGreen() + temp.getRed()){
//                        blueNum++;
//                    }
//                    samples++;
//                }
//                for(int i = 0;i < pixels.length;i++){//=Math.round(pixels.length/SAMPLES_PER_GRID)){
//                    Color temp = new Color(pixels[i]);
//                    if(temp.getBlue() > temp.getGreen() + temp.getRed()){
//                        blueNum++;
//                    }
//                }

                avgR = Math.round(avgR/(float)samples);
                avgG = Math.round(avgG/(float)samples);
                double threshold = 0.4;//playerPos[yy][xx] == 0 ? 0.3 : 0.3;// - 0.3 * (snakeLength-playerPos[yy][xx]) / snakeLength;
                avgB = blueNum / (float)samples > threshold ? 255:0;
                type = 0;
                if(avgB > avgG){
                    type = 1;
                }
                if(avgR > 120 && avgR > avgG){
                    type = 2;
                }
                map[yy][xx] = type;
            }
        }


        UpdatePlayer();


//        System.out.println("Read map");
    }

    public static void Move(){
        rx = 10;
        ry = 10;
        int done = 0;

        for(int yy = 0;yy < FIELD_SIZE_Y;yy++){
            for(int xx = 0;xx < FIELD_SIZE_X;xx++){
                if(map[yy][xx] == 2){
                    rx = xx;
                    ry = yy;
                    done++;
//                    System.out.println(rx + ", " + ry);
                }
                if(map[yy][xx] == 1){
                    done++;
//                    System.out.println(rx + ", " + ry);
                }
            }
        }

        count = new int[FIELD_SIZE_Y][FIELD_SIZE_X];
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
        while(done < FIELD_SIZE_X * FIELD_SIZE_Y) {
            if(xdone.size() == 0){
//                System.err.println("PlayGame infinite loop xdone.size() = 0");
                break;
            }
            ArrayList<Integer> newx = new ArrayList<Integer>();
            ArrayList<Integer> newy = new ArrayList<Integer>();
            for (int i = 0; i < xdone.size(); i++) {
                int num = count[ydone.get(i)][xdone.get(i)] + 1;
                boolean top = ydone.get(i) > 0;
                boolean bottom = ydone.get(i) < FIELD_SIZE_Y - 1;
                boolean left = xdone.get(i) > 0;
                boolean right = xdone.get(i) < FIELD_SIZE_X - 1;

                /////////////////////// top middle 2
                int xxx = xdone.get(i);
                int yyy = ydone.get(i) - 1;
                if (top && map[yyy][xxx] != 1 && count[yyy][xxx] > num) {
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }

                /////////////////////// middle left 4
                xxx = xdone.get(i) - 1;
                yyy = ydone.get(i);
                if (left && map[yyy][xxx] != 1 && count[yyy][xxx] > num) {
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
                /////////////////////// middle right 5
                xxx = xdone.get(i) + 1;
                yyy = ydone.get(i);
                if (right && map[yyy][xxx] != 1 && count[yyy][xxx] > num) {
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
                /////////////////////// bottom middle 7
                xxx = xdone.get(i);
                yyy = ydone.get(i) + 1;
                if (bottom && map[yyy][xxx] != 1 && count[yyy][xxx] > num) {
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
//TODO: not moving in right direction for no reason?

//        int dir = 0;//target direction 0=top, 1=right, 2=bottom, 3=left
        int score = 10000;//how good is this move
        int oldDir = dir;
        if(playerY > 0 &&map[playerY - 1][playerX] != 1&&  count[playerY - 1][playerX] < score){
            dir = 0;
            score = count[playerY - 1][playerX];
        }
        if(playerX < FIELD_SIZE_X - 1 &&map[playerY ][playerX + 1] != 1&&  count[playerY][playerX + 1] < score){
            dir = 1;
            score = count[playerY][playerX + 1];
        }
        if(playerY < FIELD_SIZE_Y - 1 && map[playerY + 1][playerX] != 1&& count[playerY + 1][playerX] < score){
            dir = 2;
            score = count[playerY + 1][playerX];
        }
        if(playerX > 0 &&map[playerY][playerX - 1] != 1&&  count[playerY][playerX - 1] < score){
            dir = 3;
            score = count[playerY][playerX - 1];
        }
        if(dir!=oldDir){
            pdir = oldDir;
        }
        robot.keyRelease(37);
        robot.keyRelease(38);
        robot.keyRelease(39);
        robot.keyRelease(40);
        if(dir == 0) robot.keyPress(38);
        if(dir == 1) robot.keyPress(39);
        if(dir == 2) robot.keyPress(40);
        if(dir == 3) robot.keyPress(37);
    }

    public static void UpdatePlayer(){
//        boolean reducePlayerPos = false;
        for(int yy = 0; yy < FIELD_SIZE_Y;yy++){
            for(int xx = 0; xx < FIELD_SIZE_X;xx++) {
                if(map[yy][xx] == 1 && oldMap[yy][xx] != 1){
                    boolean yes = false;
                    if(oldMap[yy][xx] == 2) {
                        snakeLength++;
                        eatapple = 3;
                        ex = xx;
                        ey = yy;
                        yes = true;
                    }else{
                        if(xx == playerX + 1 || xx == playerX -1 || yy == playerY + 1 || yy == playerY - 1) {
                            yes = true;
                        }
                        if(eatapple > 0){

                            eatapple--;

                        }else{
//                            yes = true;
                        }

                    }
                    if(yes){
                        playerX = xx;
                        playerY = yy;
                        playerPos[yy][xx] = snakeLength + 1;
                    }
                }
            }
        }

//        if(reducePlayerPos){
//            for(int yy = 0; yy < FIELD_SIZE_Y;yy++){
//                for(int xx = 0; xx < FIELD_SIZE_X;xx++) {
//                    if(playerPos[yy][xx] != 0){
//                        playerPos[yy][xx]--;
//                    }
//                }
//            }
//        }
    }
}
