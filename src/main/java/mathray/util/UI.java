package mathray.util;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class UI {
  
  private UI() {}
  
  public static void show(String name, BufferedImage image) {
    final Object lock = new Object();
    final JFrame frame = new JFrame(name);
    frame.add(new JLabel(new ImageIcon(image)));
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.addWindowListener(new WindowListener() {
      
      @Override
      public void windowOpened(WindowEvent e) {}
      
      @Override
      public void windowIconified(WindowEvent e) {}
      
      @Override
      public void windowDeiconified(WindowEvent e) {}
      
      @Override
      public void windowDeactivated(WindowEvent e) {}
      
      @Override
      public void windowClosing(WindowEvent e) {}
      
      @Override
      public void windowClosed(WindowEvent e) {
        synchronized(lock) {
          lock.notify();
        }
      }
      
      @Override
      public void windowActivated(WindowEvent e) {}
    });
    synchronized(lock) {
      try {
        lock.wait();
      } catch (InterruptedException ignored) {}
    }
  }

}
