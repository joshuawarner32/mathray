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
    
    right.scale(width / 2);
    up.scale(height / 2);
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
    return new double[] {
        pos.x, pos.y, pos.z,
        right.x, right.y, right.z,
        up.x, up.y, up.z,
        forward.x, forward.y, forward.z};
  }
  
  public void ray(double x, double y, RayD3 res) {
    res.setP(pos);
    res.dx = forward.x + x * right.x + y * up.x;
    res.dy = forward.y + x * right.y + y * up.y;
    res.dz = forward.z + x * right.z + y * up.z;
  }
  
  public VectorD3 transform(VectorD3 vec) {
    return new VectorD3(
      (forward.x + vec.x * right.x + vec.y * up.x) * vec.z + pos.x,
      (forward.y + vec.x * right.y + vec.y * up.y) * vec.z + pos.y,
      (forward.z + vec.x * right.z + vec.y * up.z) * vec.z + pos.z);
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
