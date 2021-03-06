package mathray.concrete;

public class RayD3 {
  
  public double x;
  public double y;
  public double z;
  
  public double dx;
  public double dy;
  public double dz;
  
  public RayD3(double x, double y, double z, double dx, double dy, double dz) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.dx = dx;
    this.dy = dy;
    this.dz = dz;
  }
  
  public RayD3(VectorD3 p, VectorD3 d) {
    this(p.x, p.y, p.z, d.x, d.y, d.z);
  }
  
  public RayD3() {}

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
  
  public double pointDistanceSq(VectorD3 p) {
    double xp = x - p.x;
    double yp = y - p.y;
    double zp = z - p.z;
    return xp * xp + yp * yp + zp * zp;
  }
  
  public VectorD3 point() {
    return new VectorD3(x, y, z);
  }

  public void setP(VectorD3 p) {
    x = p.x;
    y = p.y;
    z = p.z;
  }

}
