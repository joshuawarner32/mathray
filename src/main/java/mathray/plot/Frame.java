package mathray.plot;

import mathray.Definition;
import mathray.Multidef;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaDevice;
import mathray.eval.split.IntervalTransform;

public class Frame {
  
  public final double xmin;
  public final double xmax;
  public final double ymin;
  public final double ymax;
  
  public Frame(double xmin, double xmax, double ymin, double ymax) {
    this.xmin = xmin;
    this.xmax = xmax;
    this.ymin = ymin;
    this.ymax = ymax;
  }

  private static void eval2(FunctionTypes.D f, double[] in, double[] out, double xa, double xb) {
    in[0] = xa;
    in[1] = xb;
    f.call(in, out);
  }
  
  public static Frame frameFor(Definition def, double xmin, double xmax) {
    double ymin = Double.MAX_VALUE;
    double ymax = Double.MIN_NORMAL;
    Multidef inter = IntervalTransform.intervalize(def.toMultidef(), def.args);
    FunctionTypes.D f = JavaDevice.compile(inter);
    double[] in = new double[2];
    double[] out = new double[2];
    final int intervals = 10;
    double[] vals = new double[intervals * 2];
    for(int i = 0; i < intervals; i++) {
      double xa = (xmax - xmin) * i / intervals + xmin;
      double xb = (xmax - xmin) * (i + 1) / intervals + xmin;
      
      eval2(f, in, out, xa, xb);
      vals[i * 2] = out[0];
      vals[i * 2 + 1] = out[1];
      if(!Double.isInfinite(out[0]) && out[0] < ymin) {
        ymin = out[0];
      }
      if(!Double.isInfinite(out[1]) && out[1] > ymax) {
        ymax = out[1];
      }
    }
    double ycenter = (ymax + ymin) / 2;
    double yrad = (ymax - ymin) / 2;
    ymax = ycenter + yrad * 1.1;
    ymin = ycenter - yrad * 1.1;
    return new Frame(xmin, xmax, ymin, ymax);
  }

  public double width() {
    return xmax - xmin;
  }
  
  public double height() {
    return ymax - ymin; 
  }

}
