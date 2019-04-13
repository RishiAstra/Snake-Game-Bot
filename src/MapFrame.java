import javax.swing.*;
import java.awt.*;
public class MapFrame extends JFrame {
    public MapFrame(){
        super();
        setSize(Main.FIELD_SIZE_X * 10, Main.FIELD_SIZE_Y * 10);
        setTitle("Map");
        setState(JFrame.ICONIFIED);
        setVisible(true);
    }

    public static void main(String[] args) {
        MapFrame m = new MapFrame();
        m.repaint();
    }

    @Override
    public void paint(Graphics g)
    {
        for(int yy = 0;yy < Main.FIELD_SIZE_Y;yy++){
            for(int xx = 0;xx < Main.FIELD_SIZE_X;xx++){
                if(Main.map[yy][xx] == 0) g.setColor(Color.green);
                if(Main.map[yy][xx] == 1) g.setColor(Color.blue);
                if(Main.map[yy][xx] == 2) g.setColor(Color.red);

                g.drawRect(xx * 10, yy * 10, 10, 10);
            }
        }
    }
}
