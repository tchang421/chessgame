import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class ChessDriver
{
   public static void main(String[]args)
   {
      JFrame Tae = new JFrame("Message Displayer");
      Tae.setSize(800, 800);
      Tae.setLocation(275, 200);
      Tae.setContentPane(new Chess());
      Tae.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Tae.setVisible(true);
   }
}