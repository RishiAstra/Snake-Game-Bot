import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateMap implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == Main.updateMapThread){
            Main.test.repaint();
            Main.test.frame.repaint();
        }
    }
}
