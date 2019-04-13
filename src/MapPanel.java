import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    @Override
    public Dimension getPreferredSize(){
        return  new Dimension(Main.FIELD_SIZE_X * 20, Main.FIELD_SIZE_Y * 20);
    }
    @Override
    public void paintComponent(Graphics g){
        System.out.println("drew");
        super.paintComponent(g);
        for(int yy = 0;yy < Main.FIELD_SIZE_Y;yy++){
            for(int xx = 0;xx < Main.FIELD_SIZE_X;xx++){
                g.setColor(Color.MAGENTA);
                if(Main.map[yy][xx] == 0) g.setColor(Color.green);
                if(Main.map[yy][xx] == 1) g.setColor(Color.blue);
                if(Main.map[yy][xx] == 2) g.setColor(Color.red);
//                g.fillRect(xx * 20, yy * 20, 20, 20);
                g.drawRect(xx * 20, yy * 20, 20, 20);
            }
        }

    }
}
