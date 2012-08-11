package mathray.plot;

public class Rectangle {
  
  public final double xa;
  public final double xb;
  public final double ya;
  public final double yb;
  
  public Rectangle(Range x, Range y) {
    this(x.min, x.max, y.min, y.max);
  }
  
  public Rectangle(double xa, double xb, double ya, double yb) {
    this.xa = xa;
    this.xb = xb;
    this.ya = ya;
    this.yb = yb;
  }

  public double width() {
    return xb - xa;
  }
  
  public double height() {
    return yb - ya; 
  }

}
