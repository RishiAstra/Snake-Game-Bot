import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Displays a window of what the robot (bot) is "thinking", you can see what the bot senses as the playing field.
 */
public class Test extends JPanel{
    public ArrayList<Integer> fps = new ArrayList<Integer>();
    public static JFrame frame;
    public static int historyIndex;
    public static Button hisDown, hisUp;
    public void main1(String[] args){
//        fps = new ArrayList<Integer>();
        frame = new JFrame("Map");
        frame.add(new Test());
        frame.setSize(PlayGame.FIELD_SIZE_X * 20 + 200, PlayGame.FIELD_SIZE_Y * 20 + 250);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        hisDown = new Button("<");//TODO: draw these, when pressed change historyIndex
        hisUp = new Button(">");
    }

    @Override
    public void paint(Graphics gh){
        try {
            Graphics2D g = (Graphics2D) gh;
            int[][] tempMap = PlayGame.map;
            if (historyIndex != PlayGame.history.size()) {
                tempMap = PlayGame.history.get(historyIndex);
            }
            for (int yy = 0; yy < PlayGame.FIELD_SIZE_Y; yy++) {
                for (int xx = 0; xx < PlayGame.FIELD_SIZE_X; xx++) {
                    g.setColor(Color.MAGENTA);
                    if (tempMap[yy][xx] == 0) g.setColor(Color.green);
                    if (tempMap[yy][xx] == 1) g.setColor(Color.blue);
                    if (tempMap[yy][xx] == 2) g.setColor(Color.red);
                    g.fillRect(xx * 20 + 100, yy * 20 + 100, 20, 20);
                    if (tempMap[yy][xx] == 1) {
                        g.setColor(Color.magenta);
                        g.drawString(Integer.toString(PlayGame.bluenums[yy][xx]), xx * 20 + 100 + 5, yy * 20 + 110 + 5);
                    } else {
                        g.setColor(Color.black);
                        g.drawString(Integer.toString(PlayGame.bluenums[yy][xx]), xx * 20 + 100 + 5, yy * 20 + 110 + 5);
                    }
//                g.drawRect(xx * 20 + 25, yy * 20 + 25, 20, 20);
                }
            }
            g.setColor(Color.cyan);
            g.drawRect(PlayGame.playerX * 20 + 100 + 5, PlayGame.playerY * 20 + 100 + 5, 10, 10);
            g.setColor(Color.black);
            g.drawString(Integer.toString(PlayGame.dir), 100, PlayGame.FIELD_SIZE_Y * 20 + 120);
            g.drawString(Integer.toString(PlayGame.moveCount), 100, PlayGame.FIELD_SIZE_Y * 20 + 135);
            g.drawString(Integer.toString(PlayGame.stepx), 100, PlayGame.FIELD_SIZE_Y * 20 + 185);
            if (fps != null) {
                int fpsThisFrame = (int) Math.round(1000000000.0 / (PlayGame.currentTime - PlayGame.previousTime));
                fps.add(fpsThisFrame);
                if (fps.size() > 20) fps.remove(0);
                int total = 0;
                for (int i = 0; i < fps.size(); i++) total += fps.get(i);
                int displayThis = Math.round(1000 / (total / (float) fps.size()));
                g.drawString(Integer.toString(displayThis), 100, PlayGame.FIELD_SIZE_Y * 20 + 150);
            }
            g.drawString(Integer.toString(PlayGame.snakeLength), 100, PlayGame.FIELD_SIZE_Y * 20 + 165);

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
