package mathray.concrete;

public class FunctionTypes {
  
  private FunctionTypes() {}
  
  public interface D {
    public void call(double[] args, double[] res);
  }
  
  public interface F {
    public void call(float[] args, float[] res);
  }
  
  public interface D1_1 {
    public double call(double x);
  }
  
  public interface F1_1 {
    public float call(float x);
  }
  
  public interface D2_1 {
    public double call(double x, double y);
  }
  
  public interface F2_1 {
    public float call(float x, float y);
  }
  
  public interface D3_1 {
    public double call(double x, double y, double z);
  }
  
  public interface F3_1 {
    public float call(float x, float y, float z);
  }
  
  public interface ZeroOnRayD3 {
    public boolean maybeHasZeroOn(RayD3 args);
  }
  
  public interface FillerD {
    public void fill(double[] limits, int[] counts, double[] result);
  }
  
  public interface FillerF {
    public void fill(float[] limits, int[] counts, float[] result);
  }
  
  public interface RepeatD {
    public void repeat(double[] args, double[] res);
  }
  
  public interface RepeatF {
    public void repeat(float[] args, float[] res);
  }

}
