import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlayGame implements Runnable{

    public static int[][] count = new int[Main.FIELD_SIZE_Y][Main.FIELD_SIZE_X];
    public static int rx;
    public static int ry;
    public static int dir;
    public static int moveCount;
    @Override
    public void run(){
        while(true){
            try{
                readPlayingField(true);
                for(int yy = 0; yy < Main.FIELD_SIZE_Y;yy++){
                    for(int xx = 0; xx < Main.FIELD_SIZE_X;xx++) {
                        if(Main.map[yy][xx] == 2 && Main.oldMap[yy][xx] != 2){
                            Main.playerX = xx;
                            Main.playerY = yy;
                        }
                    }
                }
                Move();
                moveCount++;
                Thread.sleep(30);
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
    }
    public static void readPlayingField(boolean updateOldMap){

        BufferedImage img = Main.robot.createScreenCapture(new Rectangle(Main.x, Main.y, Main.w, Main.h));
        int stepx = (int)Math.floor(((double)Main.w)/Main.FIELD_SIZE_X);
        int stepy = (int)Math.floor(((double)Main.h)/Main.FIELD_SIZE_Y);
        for(int yy = 0; yy < Main.FIELD_SIZE_Y;yy++){
            for(int xx = 0; xx < Main.FIELD_SIZE_X;xx++){
                int type = 0;
                int avgR = 0;
                int avgG = 0;
                int avgB = 0;
                int[] pixels = img.getRGB((int)Math.floor(xx * Main.w / Main.FIELD_SIZE_X), (int)Math.floor(yy * Main.h / Main.FIELD_SIZE_Y), stepx, stepy, null, 0, stepx);
                double sizeMult = 0.2;//sample only middle 50% of pixels
                int sizeX = (int)Math.floor(Main.w / (double)Main.FIELD_SIZE_X * sizeMult);//stepx * sizeMult);
                int sizeY = (int)Math.floor(Main.h / (double)Main.FIELD_SIZE_Y * sizeMult);//stepy * sizeMult);
//                int xTemp = (int)Math.floor((xx + (1-sizeMult)/2) * Main.w / (double)Main.FIELD_SIZE_X);
//                int yTemp = (int)Math.floor((yy + (1-sizeMult)/2) * Main.h / (double)Main.FIELD_SIZE_Y);
//                int[] pixels1 = img.getRGB(xTemp, yTemp, sizeX, sizeY, null, 0, sizeX);

                int samples = 0;
                int blueNum = 0;

                for(int i = 0;i < pixels.length;i++){//=Math.round(pixels.length/SAMPLES_PER_GRID)){
                    Color temp = new Color(pixels[i]);
                    avgR += temp.getRed();
                    avgG += temp.getGreen();
//                    avgB += temp.getBlue();
                    samples++;
                }
                for(int i = 0;i < pixels.length;i++){//=Math.round(pixels.length/SAMPLES_PER_GRID)){
                    Color temp = new Color(pixels[i]);
//                    avgR += temp.getRed();
//                    avgG += temp.getGreen();
                    if(temp.getBlue() > temp.getGreen()){
                        blueNum++;
                    }
//                    avgB += temp.getBlue();
                }
                avgR = Math.round(avgR/(float)samples);
                avgG = Math.round(avgG/(float)samples);
                avgB = blueNum / (float)pixels.length > 0.15 ? 255:0;//Math.round(avgB/(float)samples1);//blue needs different test
//                int min = avgR;
//                if(avgG < min) min = avgG;
//                if(avgB < min) min = avgB;
//                avgR -= min;
//                avgG -= min;
//                avgB -= min;

//
//                if(avgG > 50){
//                    type = 0;//blank
//                }
//                if(avgB > 50){
//                    type = 1;
//                }
//                if(avgR > 50){
//                    type = 2;//apple
////                    System.out.println("apple found at " + xx + ", " + yy);
//                }

                type = 0;
                if(avgB > avgG){
                    type = 1;
                }
                if(avgR > 120 && avgR > avgG){
                    type = 2;
                }
                Main.map[yy][xx] = type;
            }
        }
        for(int yy = 0; yy < Main.FIELD_SIZE_Y;yy++){
            for(int xx = 0; xx < Main.FIELD_SIZE_X;xx++) {
                if(Main.map[yy][xx] == 1 && Main.oldMap[yy][xx] != 1){
                    Main.playerX = xx;
                    Main.playerY = yy;
                }
            }
        }

        if(updateOldMap){
            for(int yy = 0; yy < Main.FIELD_SIZE_Y;yy++){
                for(int xx = 0; xx < Main.FIELD_SIZE_X;xx++) {
                    Main.oldMap[yy][xx] = Main.map[yy][xx];
                }
            }
        }
//        System.out.println("Read map");
    }

    public static void Move(){
        rx = 10;
        ry = 10;
        int done = 1;

        for(int yy = 0;yy < Main.FIELD_SIZE_Y;yy++){
            for(int xx = 0;xx < Main.FIELD_SIZE_X;xx++){
                if(Main.map[yy][xx] == 2){
                    rx = xx;
                    ry = yy;
                    done++;
//                    System.out.println(rx + ", " + ry);
                }
                if(Main.map[yy][xx] == 1){
                    done++;
//                    System.out.println(rx + ", " + ry);
                }
            }
        }

        count = new int[Main.FIELD_SIZE_Y][Main.FIELD_SIZE_X];
        for(int yy = 0;yy < Main.FIELD_SIZE_Y;yy++){
            for(int xx = 0;xx < Main.FIELD_SIZE_X;xx++){
                count[yy][xx] = 1000;
            }
        }
        ArrayList<Integer> xdone = new ArrayList<Integer>();
        ArrayList<Integer> ydone = new ArrayList<Integer>();
        count [ry][rx] = 0;
        xdone.add(rx);
        ydone.add(ry);
        while(done < Main.FIELD_SIZE_X * Main.FIELD_SIZE_Y) {
            if(xdone.size() == 0){
                System.err.println("PlayGame infinite loop xdone.size() = 0");
                break;
            }
            ArrayList<Integer> newx = new ArrayList<Integer>();
            ArrayList<Integer> newy = new ArrayList<Integer>();
            for (int i = 0; i < xdone.size(); i++) {
                int num = count[ydone.get(i)][xdone.get(i)] + 1;
                boolean top = ydone.get(i) > 0;
                boolean bottom = ydone.get(i) < Main.FIELD_SIZE_Y - 1;
                boolean left = xdone.get(i) > 0;
                boolean right = xdone.get(i) < Main.FIELD_SIZE_X - 1;

                /////////////////////// top middle 2
                int xxx = xdone.get(i);
                int yyy = ydone.get(i) - 1;
                if (top && Main.map[yyy][xxx] != 1 && count[yyy][xxx] > num) {
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }

                /////////////////////// middle left 4
                xxx = xdone.get(i) - 1;
                yyy = ydone.get(i);
                if (left && Main.map[yyy][xxx] != 1 && count[yyy][xxx] > num) {
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
                /////////////////////// middle right 5
                xxx = xdone.get(i) + 1;
                yyy = ydone.get(i);
                if (right && Main.map[yyy][xxx] != 1 && count[yyy][xxx] > num) {
                    count[yyy][xxx] = num;
                    newx.add(xxx);
                    newy.add(yyy);
                }
                /////////////////////// bottom middle 7
                xxx = xdone.get(i);
                yyy = ydone.get(i) + 1;
                if (bottom && Main.map[yyy][xxx] != 1 && count[yyy][xxx] > num) {
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

        if(Main.playerY > 0 &&Main.map[Main.playerY - 1][Main.playerX] != 1&&  count[Main.playerY - 1][Main.playerX] < score){
            dir = 0;
            score = count[Main.playerY - 1][Main.playerX];
        }
        if(Main.playerX < Main.FIELD_SIZE_X - 1 &&Main.map[Main.playerY ][Main.playerX + 1] != 1&&  count[Main.playerY][Main.playerX + 1] < score){
            dir = 1;
            score = count[Main.playerY][Main.playerX + 1];
        }
        if(Main.playerY < Main.FIELD_SIZE_Y - 1 && Main.map[Main.playerY + 1][Main.playerX] != 1&& count[Main.playerY + 1][Main.playerX] < score){
            dir = 2;
            score = count[Main.playerY + 1][Main.playerX];
        }
        if(Main.playerX > 0 &&Main.map[Main.playerY][Main.playerX - 1] != 1&&  count[Main.playerY][Main.playerX - 1] < score){
            dir = 3;
            score = count[Main.playerY][Main.playerX - 1];
        }
        Main.robot.keyRelease(37);
        Main.robot.keyRelease(38);
        Main.robot.keyRelease(39);
        Main.robot.keyRelease(40);
        if(dir == 0) Main.robot.keyPress(38);
        if(dir == 1) Main.robot.keyPress(39);
        if(dir == 2) Main.robot.keyPress(40);
        if(dir == 3) Main.robot.keyPress(37);
    }
}
