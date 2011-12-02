package mathray.plot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import mathray.Definition;
import mathray.FunctionD;
import mathray.eval.java.JavaCompiler;

public class Plotter {
  
  /*private static double eval(FunctionD f, double[] in, double[] out, double x) {
    in[0] = x;
    f.call(in, out);
    return out[0];
  }
  
  public static BufferedImage simplePlot(Definition def, double min, double max, int width, int height) {
    BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D)ret.getGraphics();
    FunctionD f = JavaCompiler.compile(def);
    double[] in = new double[1];
    double[] out = new double[1];
    double[] vals = new double[width + 1];
    double minh = Double.POSITIVE_INFINITY;
    double maxh = Double.NEGATIVE_INFINITY;
    for(int i = 0; i <= width; i++) {
      double x = (max - min) * i / width + min;
      double y = vals[i] = eval(f, in, out, x);
      if(y < minh) {
        minh = y;
      }
      if(y > maxh) {
        maxh = y;
      }
    }
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g.setBackground(Color.WHITE);
    g.clearRect(0, 0, width, height);
    g.setColor(Color.BLACK);
    for(int i = 0; i < width; i++) {
      double y1 = (vals[i] - minh) / (maxh - minh) * height;
      double y2 = (vals[i + 1] - minh) / (maxh - minh) * height;
      g.draw(new Line2D.Double(i, height - y1, i + 1, height - y2));
    }
    return ret;
  }

  private static void eval2(FunctionD f, double[] in, double[] out, double xa, double xb) {
    in[0] = xa;
    in[1] = xb;
    f.call(in, out);
  }
  
  public static BufferedImage intervalPlot(Definition def, double min, double max, int intervals, int width, int height) {
    BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D)ret.getGraphics();
    FunctionD f = JavaCompiler.compile(def);
    double[] in = new double[2];
    double[] out = new double[2];
    double[] vals = new double[intervals * 2];
    double minh = Double.POSITIVE_INFINITY;
    double maxh = Double.NEGATIVE_INFINITY;
    for(int i = 0; i < intervals; i++) {
      double xa = (max - min) * i / intervals + min;
      double xb = (max - min) * (i + 1) / intervals + min;
      
      eval2(f, in, out, xa, xb);
      vals[i * 2] = out[0];
      vals[i * 2 + 1] = out[1];
      if(out[0] < minh) {
        minh = out[0];
      }
      if(out[1] > maxh) {
        maxh = out[1];
      }
    }
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g.setBackground(Color.WHITE);
    g.clearRect(0, 0, width, height);
    g.setColor(Color.BLACK);
    for(int i = 0; i < intervals; i++) {
      double xa = width * i / intervals;
      double xb = width * (i + 1) / intervals;
      double ya = (vals[2 * i] - minh) / (maxh - minh) * height;
      double yb = (vals[2 * i + 1] - minh) / (maxh - minh) * height;
      g.fill(new Rectangle2D.Double(xa, height - yb, xb - xa, yb - ya));
    }
    return ret;
  }
  
  public static Graph2D graphPlot(Definition def, double xa, double xb, int points) {
    FunctionD f = JavaCompiler.compile(def);
    double[] in = new double[1];
    double[] out = new double[1];
    double[] vals = new double[points];
    double minh = Double.POSITIVE_INFINITY;
    double maxh = Double.NEGATIVE_INFINITY;
    for(int i = 0; i < points; i++) {
      double x = (xb - xa) * i / points + xa;
      double y = vals[i] = eval(f, in, out, x);
      if(y < minh) {
        minh = y;
      }
      if(y > maxh) {
        maxh = y;
      }
    }
    Graph2D.Builder b = Graph2D.builder();
    for(int i = 0; i < points; i++) {
      double x = (xb - xa) * i / points + xa;
      b.point((float)x, (float)vals[i]);
    }
    return b.build();
  }*/

}
