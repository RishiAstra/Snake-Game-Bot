import javax.swing.*;
import java.awt.*;

public class Test extends JPanel{
    public static void main1(String[] args){
        JFrame frame = new JFrame("Map");
        frame.add(new Test());
        frame.setSize(Main.FIELD_SIZE_X * 20, Main.FIELD_SIZE_Y * 20);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics gh){
        Graphics2D g = (Graphics2D)gh;
        for(int yy = 0;yy < Main.FIELD_SIZE_Y;yy++){
            for(int xx = 0;xx < Main.FIELD_SIZE_X;xx++){
                g.setColor(Color.MAGENTA);
                if(Main.map[yy][xx] == 0) g.setColor(Color.green);
                if(Main.map[yy][xx] == 1) g.setColor(Color.blue);
                if(Main.map[yy][xx] == 2) g.setColor(Color.red);
                g.fillRect(xx * 20, yy * 20, 20, 20);
                g.drawRect(xx * 20, yy * 20, 20, 20);
            }
        }
    }
}
