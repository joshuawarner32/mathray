package mathray.flow.concrete;

import java.util.Arrays;

import mathray.flow.Data;
import mathray.util.ArrayUtil;

public class DataD implements Data {

  private final int[] dimensions;
  
  private final double[] data;
  
  public DataD(int... dimensions) {
    this.dimensions = Arrays.copyOf(dimensions, dimensions.length);
    this.data = new double[ArrayUtil.product(dimensions)];
  }
}
