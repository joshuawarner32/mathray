package mathray.concrete;

public class VectorD3 {
  
  public double x;
  public double y;
  public double z;
  
  public VectorD3() {
    this(0, 0, 0);
  }
  
  public VectorD3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public double lengthSq() {
    return x * x + y * y + z * z;
  }
  
  public double length() {
    return Math.sqrt(lengthSq());
  }
  
  public void normalizeInPlace() {
    double l = length();
    x /= l;
    y /= l;
    z /= l;
  }
  
  public void crossInPlace(VectorD3 a, VectorD3 b) {
    x = a.y * b.z - a.z * b.y;
    y = a.z * b.x - a.x * b.z;
    z = a.x * b.y - a.y * b.x;
  }
  
  public static VectorD3 cross(VectorD3 a, VectorD3 b) {
    VectorD3 res = new VectorD3();
    res.crossInPlace(a, b);
    return res;
  }
  
  public VectorD3 subInPlace(VectorD3 a, VectorD3 b) {
    x = a.x - b.x;
    y = a.y - b.y;
    z = a.z - b.z;
    return this;
  }

  public static VectorD3 sub(VectorD3 a, VectorD3 b) {
    return new VectorD3().subInPlace(a, b);
  }
  
  public VectorD3 addInPlace(VectorD3 a, VectorD3 b) {
    x = a.x + b.x;
    y = a.y + b.y;
    z = a.z + b.z;
    return this;
  }
  
  public static VectorD3 add(VectorD3 a, VectorD3 b) {
    return new VectorD3().addInPlace(a, b);
  }
  
  @Override
  public String toString() {
    return "{" + x + ", " + y + ", " + z + "}";
  }

  public double[] toArray() {
    return new double[] {x, y, z};
  }

  public static VectorD3 fromArray(double[] a) {
    return new VectorD3(a[0], a[1], a[2]);
  }
  
  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof VectorD3)) {
      return false;
    }
    VectorD3 v = (VectorD3)obj;
    return x == v.x && y == v.y && z == v.z;
  }
  
  @Override
  public int hashCode() {
    return new Double(x).hashCode() + 2 * new Double(y).hashCode() + 3 * new Double(z).hashCode();
  }

  public void scale(double d) {
    x *= d;
    y *= d;
    z *= d;
  }

  public VectorD3 add(VectorD3 vectorD3) {
    return null;
  }

}
