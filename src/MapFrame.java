import javax.swing.*;
import java.awt.*;
public class MapFrame extends JFrame {
    public MapFrame(){
        super();
        setSize(Main.FIELD_SIZE_X * 20, Main.FIELD_SIZE_Y * 20);
        setTitle("Map");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        MapPanel mapPanel = new MapPanel();
//        setContentPane(mapPanel);


    }

//    @Override
//    public void update(Graphics g)
//    {
//        paint(g);
//    }
//
//    @Override
//    public void paint(Graphics g){
////        super.paint(g);
//        for(int yy = 0;yy < Main.FIELD_SIZE_Y;yy++){
//            for(int xx = 0;xx < Main.FIELD_SIZE_X;xx++){
//                g.setColor(Color.MAGENTA);
//                if(Main.map[yy][xx] == 0) g.setColor(Color.green);
//                if(Main.map[yy][xx] == 1) g.setColor(Color.blue);
//                if(Main.map[yy][xx] == 2) g.setColor(Color.red);
//                g.fillRect(xx * 20, yy * 20, 20, 20);
////                g.drawRect(xx * 20, yy * 20, 20, 20);
//            }
//        }
//    }
}
