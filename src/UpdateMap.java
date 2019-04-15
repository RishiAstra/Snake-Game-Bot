public class UpdateMap implements Runnable{
    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(50);
//                frame.invalidate();
//                frame.validate();
//                frame.repaint();
                Main.test.invalidate();
                Main.test.validate();
                Main.test.repaint();//TODO: update map repaint
            }catch (Exception e){
                System.err.println(e);
            }
        }
    }
}
