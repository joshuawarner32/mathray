package mathray.concrete;

import mathray.flow.concrete.DataD2;

public class BlockD3 {
  
  public double x0;
  public double y0;
  public double z0;
  
  public double x1;
  public double y1;
  public double z1;
  
  public int x;
  public int y;
  public int width;
  public int height;
  
  public BlockD3(double x0, double y0, double z0, double x1, double y1, double z1, int x, int y, int width, int height) {
    this.x0 = x0;
    this.y0 = y0;
    this.z0 = z0;

    this.x1 = x1;
    this.y1 = y1;
    this.z1 = z1;
    
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public boolean single() {
    return width == 1 && height == 1;
  }

  public double depth() {
    return z1 - z0;
  }

  public void putOn(DataD2 mat) {
    mat.put(x, y, (z0 + z1) / 2);
  }

  public BlockD3 split(int x2, int y2, int width2, int height2) {
    double nx0 = x0 + (x2 - x) * (x1 - x0) / width;
    double ny0 = y0 + (y2 - y) * (y1 - y0) / height;
    
    double nx1 = nx0 + (x1 - x0) * width2 / width;
    double ny1 = ny0 + (y1 - y0) * height2 / height;
    double nz1 = (z0 + z1) / 2;
    
    return new BlockD3(nx0, ny0, z0, nx1, ny1, nz1, x2, y2, width2, height2);
    
  }

  public void next() {
    double d = depth();
    z0 = z1;
    z1 = z0 + 2 * d;
  }

  public void half() {
    z1 = (z0 + z1) / 2;
  }
  
}
