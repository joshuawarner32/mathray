package mathray.concrete;

import java.util.Arrays;

public class MatrixD2 {
  
  private double[] data;
  public final int width;
  public final int height;
  
  public MatrixD2(int width, int height) {
    data = new double[width * height];
    this.width = width;
    this.height = height;
  }
  
  public void put(int x, int y, double value) {
    data[x + y * width] = value;
  }
  
  public void putEverywhere(double value) {
    Arrays.fill(data, value);
  }
  
}
