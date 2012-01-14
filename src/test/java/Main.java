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
import mathray.Multidef;
import mathray.Symbol;
import mathray.device.FunctionTypes;
import mathray.device.FunctionTypes.ZeroInBlockD3;
import mathray.eval.java.JavaCompiler;
import mathray.eval.split.IntervalTransform;
import mathray.eval.text.DefaultPrinter;
import mathray.eval.text.ParseInfo;
import mathray.eval.text.InfixOperator.Associativity;
import mathray.plot.Frame;
import mathray.plot.Plot3D;
import mathray.plot.Plotter;
import mathray.random.ValueRandom;
import mathray.util.Vector;
import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Main {

  public static void main(String[] args) {
    //printRandomExpressionForever();
    plot3DStuff();
  }
  
  private static Symbol x = sym("x");
  private static Symbol y = sym("y");
  private static Symbol z = sym("z");
  
  private static void plot3DStuff() {
    ParseInfo info = ParseInfo.builder()
      .group("(", ",", ")")
      .infix("+", 10, Associativity.LEFT, ADD)
      .infix("-", 10, Associativity.LEFT, SUB)
      .infix("*", 20, Associativity.LEFT, MUL)
      .infix("/", 20, Associativity.LEFT, DIV)
      .infix("^", 30, Associativity.RIGHT, POW)
      .prefix("-", 25, NEG)
      .function(SIN)
      .function(SINH)
      .function(ASIN)
      .function(COS)
      .function(COSH)
      .function(ACOS)
      .function(TAN)
      .function(TANH)
      .function(ATAN)
      .function(ATAN2)
      .function(SQRT)
      .function(LOG)
      .function(MIN)
      .function(MAX)
      .sym(x).sym(y).sym(z)
      .build();
    //plot3D(def(args(x, y, z), add(mul(x, x), mul(y, y), mul(z, z), num(-1))), 512, 512);
    //plot3D(def(args(x, y, z), add(pow(x, 2), pow(y, 2), pow(z, 2), num(-1))), 512, 512);
    plot3D(def(args(x, y, z), info.parse("x^2+z^2-(1-y)*y^4")), 512, 512);
  }
  
  private static void plot3D(Definition def, int width, int height) {
    Multidef inter = IntervalTransform.intervalize(def.toMultidef(), def.args);
    System.out.println(inter);
    FunctionTypes.ZeroInBlockD3 func = JavaCompiler.compile(JavaCompiler.MAYBE_ZERO_IN_BLOCKD3, inter);
    BufferedImage image = Plot3D.plotBlockDepth(func, width, height, 0.001, 100);
    show("plot", image);
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
    FunctionTypes.D1_1 f = JavaCompiler.compile(JavaCompiler.D1_1, def.toMultidef());
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
