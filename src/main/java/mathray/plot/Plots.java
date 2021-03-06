package mathray.plot;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


import mathray.Definition;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaDevice;

public class Plots {

  private static void eval2(FunctionTypes.D f, double[] in, double[] out, double xa, double xb) {
    in[0] = xa;
    in[1] = xb;
    f.call(in, out);
  }
  
  public static BufferedImage intervalPlot(Definition def, double min, double max, int intervals, int width, int height) {
    BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D)ret.getGraphics();
    FunctionTypes.D f = JavaDevice.compile(def.toMultidef());
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

  public static Graph2D graphPlot(FunctionTypes.D1_1 f, Rectangle rect, int samples) {
    Graph2D.Builder builder = Graph2D.builder();
    
    for(int i = 0; i < samples - 1; i++) {
      double x = rect.width() * i / (samples - 1) + rect.xa;
      double y = f.call(x);
      builder.point(x, y);
    }
    builder.point(rect.xb, f.call(rect.xb));
    return builder.build(rect);
  }

}
