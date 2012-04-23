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
    JFrame frame = new JFrame(name);
    frame.setBounds(0, 0, image.getWidth() + 100, image.getHeight() + 100);
    frame.add(new JLabel(new ImageIcon(image)));
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
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
