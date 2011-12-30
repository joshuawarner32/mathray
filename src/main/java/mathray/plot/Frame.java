package mathray.plot;

import mathray.Definition;

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
  
  public static Frame frameFor(Definition def, double xmin, double xmax) {
    return new Frame(xmin, xmax, -10, 10);
  }

  public double width() {
    return xmax - xmin;
  }
  
  public double height() {
    return ymax - ymin; 
  }

}
