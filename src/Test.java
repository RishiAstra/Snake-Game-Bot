import javax.swing.*;
import java.awt.*;

/**
 * Displays a window of what the robot (bot) is "thinking", you can see what the bot senses as the playing field.
 */
public class Test extends JPanel{
    public static JFrame frame;
    public void main1(String[] args){
        frame = new JFrame("Map");
        frame.add(new Test());
        frame.setSize(PlayGame.FIELD_SIZE_X * 20 + 200, PlayGame.FIELD_SIZE_Y * 20 + 200);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics gh){
        Graphics2D g = (Graphics2D)gh;
        for(int yy = 0;yy < PlayGame.FIELD_SIZE_Y;yy++){
            for(int xx = 0;xx < PlayGame.FIELD_SIZE_X;xx++){
                g.setColor(Color.MAGENTA);
                if(PlayGame.map[yy][xx] == 0) g.setColor(Color.green);
                if(PlayGame.map[yy][xx] == 1) g.setColor(Color.blue);
                if(PlayGame.map[yy][xx] == 2) g.setColor(Color.red);
                g.fillRect(xx * 20 + 100, yy * 20 + 100, 20, 20);
                g.setColor(Color.black);
                g.drawString(Integer.toString(PlayGame.count[yy][xx]), xx * 20 + 100, yy * 20 + 110);
//                g.drawRect(xx * 20 + 25, yy * 20 + 25, 20, 20);
            }
        }
        g.setColor(Color.cyan);
        g.fillRect(PlayGame.playerX * 20 + 100, PlayGame.playerY* 20 + 100, 15, 15);
        g.setColor(Color.black);
        g.drawString(Integer.toString(PlayGame.dir), 100, PlayGame.FIELD_SIZE_Y * 20 + 120);
        g.drawString(Integer.toString(PlayGame.moveCount), 100, PlayGame.FIELD_SIZE_Y * 20 + 150);

    }
}
