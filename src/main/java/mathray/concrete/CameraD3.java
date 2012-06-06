package mathray.concrete;

public class CameraD3 {
  
  public final VectorD3 pos;
  
  public final VectorD3 forward;
  public final VectorD3 up;
  public final VectorD3 right;
  
  public double width;
  public double height;
  
  private CameraD3(VectorD3 pos, VectorD3 forward, VectorD3 up, VectorD3 right, double width, double height) {
    this.pos = pos;
    this.forward = forward;
    this.up = up;
    this.right = right;
    this.width = width;
    this.height = height;
  }
  
  public static CameraD3 lookAt(VectorD3 pos, VectorD3 lookAt, double fov, double aspect, VectorD3 up) {
    VectorD3 forward = VectorD3.sub(lookAt, pos);
    forward.normalizeInPlace();
    
    VectorD3 right = VectorD3.cross(forward, up);
    right.normalizeInPlace();
    
    VectorD3 nup = VectorD3.cross(right, forward);
    nup.normalizeInPlace();
    
    double width = Math.sin(fov / 2);
    double height = width / aspect;
    
    return new CameraD3(pos, forward, nup, right, width, height);
  }
  
  public double[] args() {
    return new double[] {right.x, right.y, right.z, up.x, up.y, up.z, forward.x, forward.y, forward.z, pos.x, pos.y, pos.z};
  }
  
  public void ray(double x, double y, RayD3 res) {
    res.setP(pos);
    res.dx = forward.x + (x * width / 2) * right.x + (y * height / 2) * up.x;
    res.dy = forward.y + (x * width / 2) * right.y + (y * height / 2) * up.y;
    res.dz = forward.z + (x * width / 2) * right.z + (y * height / 2) * up.z;
  }
  
  public VectorD3 transform(VectorD3 vec) {
    return new VectorD3(
      (forward.x + (vec.x * width / 2) * right.x + (vec.y * height / 2) * up.x) * vec.z,
      (forward.y + (vec.x * width / 2) * right.y + (vec.y * height / 2) * up.y) * vec.z,
      (forward.z + (vec.x * width / 2) * right.z + (vec.y * height / 2) * up.z) * vec.z);
  }

  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    b.append("CameraD3(\n");
    b.append("  pos:     ").append(pos).append('\n');
    b.append("  forward: ").append(forward).append('\n');
    b.append("  up:      ").append(up).append('\n');
    b.append("  right:   ").append(right).append('\n');
    b.append(')');
    return b.toString();
  }
}
