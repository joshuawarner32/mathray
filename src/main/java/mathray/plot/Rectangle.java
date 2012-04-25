package mathray.plot;

public class Rectangle {
  
  public final double xa;
  public final double xb;
  public final double ya;
  public final double yb;
  
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
