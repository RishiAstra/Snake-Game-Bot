import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Used to automatically repaint the playing field map, used in a timer (see Main)
 */
public class UpdateMap implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == Main.updateMapThread){
            Main.test.repaint();
            Main.test.frame.repaint();
        }
    }
}
