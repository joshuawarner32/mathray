package mathray.flow.concrete;

import java.util.Arrays;

import mathray.flow.Data;
import mathray.util.ArrayUtil;

public class DataD implements Data {

  private final int[] dimensions;
  
  private final double[] data;
  
  public DataD(int... dimensions) {
    this(Arrays.copyOf(dimensions, dimensions.length), new double[ArrayUtil.product(dimensions)]);
  }
  
  private DataD(int[] dimensions, double[] data) {
    this.dimensions = dimensions;
    this.data = data;
  }
  
  public double nonInfiniteMin() {
    double res = Double.POSITIVE_INFINITY;
    for(double d : data) {
      if(d < res && !Double.isInfinite(d)) {
        res = d;
      }
    }
    return res;
  }
  
  public double nonInfiniteMax() {
    double res = Double.NEGATIVE_INFINITY;
    for(double d : data) {
      if(d > res && !Double.isInfinite(d)) {
        res = d;
      }
    }
    return res;
  }
  
  public static class Builder3 {
    public final int width;
    public final int height;
    public final int depth;
    
    private final double[] data;
    
    public Builder3(int width, int height, int depth) {
      this.width = width;
      this.height = height;
      this.depth = depth;
      
      this.data = new double[width * height * depth];
    }
    
    public void put(int x, int y, int z, double value) {
      data[x + width * (y + height * z)] = value;
    }
    
    public DataD build() {
      // TODO: prevent modification
      return new DataD(new int[] {width, height, depth}, data);
    }
  }
  
  public class View3 {
    
    public final int width;
    public final int height;
    public final int depth;
    
    private View3() {
      if(dimensions.length != 3) {
        throw new IllegalArgumentException();
      }
      this.width = dimensions[0];
      this.height = dimensions[1];
      this.depth = dimensions[2];
    }
    
    public double get(int x, int y, int z) {
      return data[x + width * (y + height * z)];
    }
    
  }

  @Override
  public int getDimensions() {
    return dimensions.length;
  }

  @Override
  public int getLength(int dim) {
    return dimensions[dim];
  }

  public DataD.View3 asView3() {
    return new View3();
  }
}
