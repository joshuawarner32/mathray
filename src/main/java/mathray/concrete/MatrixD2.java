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
  
  public double nonInfiniteMax() {
    double max = Double.NEGATIVE_INFINITY;
    for(double d : data) {
      if(d > max && !Double.isInfinite(d) && !Double.isNaN(d)) {
        max = d;
      }
    }
    return max;
  }
  
  public double nonInfiniteMin() {
    double min = Double.POSITIVE_INFINITY;
    for(double d : data) {
      if(d < min && !Double.isInfinite(d) && !Double.isNaN(d)) {
        min = d;
      }
    }
    return min;
  }

  public double get(int x, int y) {
    return data[x + y * width];
  }
  
}
