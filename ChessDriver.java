import javax.swing.*;

public class ChessDriver{
      public static void main(String[]args){
      JFrame frame = new JFrame("Message Displayer");
      frame.setSize(800, 800);
      frame.setLocation(275, 200);
      frame.setContentPane(new Chess());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}