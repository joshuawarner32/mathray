import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import mathray.Args;
import mathray.Definition;
import mathray.NamedConstants;
import mathray.Symbol;
import mathray.concrete.FunctionTypes;
import mathray.eval.java.JavaCompiler;
import mathray.plot.Frame;
import mathray.plot.Plotter;
import mathray.random.ValueRandom;
import mathray.util.Vector;
import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Main {

  public static void main(String[] args) {
    printRandomExpressionForever();
  }
  
  private static void show(String name, BufferedImage image) {
    final Object lock = new Object();
    JFrame frame = new JFrame(name);
    frame.setBounds(0, 0, 600, 600);
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
  
  private static void process(Definition def) {
    System.out.println(def.toString());
    BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    g.setBackground(Color.WHITE);
    g.clearRect(0, 0, 512, 512);
    FunctionTypes.D1_1 f = JavaCompiler.compile(JavaCompiler.D1_1, def.toComputation());
    Plotter.simplePlot(f, Frame.frameFor(def, -10, 10), image, Color.BLACK);
    show(def.toString(), image);
  }

  private static void printRandomExpressionForever() {
    ValueRandom random = new ValueRandom(Vector.<Symbol>empty(), vector(ADD, SUB, MUL, DIV, SIN, SQRT, NEG));
    Symbol x = sym("x");
    Args a = args(x);
    for(Definition def : random.randomDefinitions(a, 0.99, 0.5, 0.2)) {
      process(def);
    }
  }
}
