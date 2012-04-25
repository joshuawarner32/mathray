package mathray.plot;

import mathray.Definition;
import mathray.Multidef;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaDevice;
import mathray.eval.split.IntervalTransform;

public class Frame {

  private Frame() {}
  
  public static Rectangle frameFor(Definition def, double xmin, double xmax) {
    double ymin = Double.MAX_VALUE;
    double ymax = Double.MIN_NORMAL;
    Multidef inter = IntervalTransform.intervalize(def.toMultidef(), def.args);
    FunctionTypes.RepeatD funcRepeat = FunctionTypes.toRepeatD(JavaDevice.compile(inter));
    
    final int intervals = 10;
    double[] inputs = new double[intervals * 2];
    double[] vals = new double[intervals * 2];
    
    for(int i = 0; i < intervals; i++) {
      inputs[2 * i] = (xmax - xmin) * i / intervals + xmin;
      inputs[2 * i + 1] = (xmax - xmin) * (i + 1) / intervals + xmin;
    }
    
    funcRepeat.repeat(inputs, vals);
    
    for(int i = 0; i < intervals; i++) {
      double a = vals[i * 2];
      double b = vals[i * 2 + 1];
      if(!Double.isInfinite(a) && a < ymin) {
        ymin = a;
      }
      if(!Double.isInfinite(b) && b > ymax) {
        ymax = b;
      }
    }
    double ycenter = (ymax + ymin) / 2;
    double yrad = (ymax - ymin) / 2;
    ymax = ycenter + yrad * 1.1;
    ymin = ycenter - yrad * 1.1;
    return new Rectangle(xmin, xmax, ymin, ymax);
  }

}
