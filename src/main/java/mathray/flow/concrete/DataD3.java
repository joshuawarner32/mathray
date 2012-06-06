package mathray.flow.concrete;

public class DataD3 {

  private final int width;
  private final int height;
  
  private final double[] data;
  
  public DataD3(int width, int height, int depth) {
    data = new double[width * height * depth];
    this.width = width;
    this.height = height;
  }
  
  public void put(int x, int y, int z, double value) {
    data[x + y * width + z * width * height] = value;
  }
  
  public double get(int x, int y, int z, double value) {
    return data[x + y * width + z * width * height];
  }
}
