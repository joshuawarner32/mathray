package mathray.concrete;

public class Ray3 {
  
  public double x;
  public double y;
  public double z;
  
  public double dx;
  public double dy;
  public double dz;
  
  public Ray3(double x, double y, double z, double dx, double dy, double dz) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.dx = dx;
    this.dy = dy;
    this.dz = dz;
  }
  
  public Ray3(Vector3 p, Vector3 d) {
    this(p.x, p.y, p.z, d.x, d.y, d.z);
  }
  
  public void increment() {
    x += dx;
    y += dy;
    z += dz;
  }
  
  public void scale(double f) {
    dx *= f;
    dy *= f;
    dz *= f;
  }
  
  public double lengthSq() {
    return dx * dx + dy * dy + dz * dz;
  }
  
  public double pointDistanceSq(Vector3 p) {
    double xp = x - p.x;
    double yp = y - p.y;
    double zp = z - p.z;
    return xp * xp + yp * yp + zp * zp;
  }
  
  public Vector3 point() {
    return new Vector3(x, y, z);
  }

}
