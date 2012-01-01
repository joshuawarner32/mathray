package mathray.device;

import mathray.concrete.RayD3;
import mathray.concrete.VectorD2;

public class FunctionTypes {
  
  private FunctionTypes() {}
  
  public interface D {
    public void call(double[] args, double[] res);
  }
  
  public static final FunctionType<D> FUNCTION_D = new FunctionType<D>(D.class);
  
  public static D toD(final F func) {
    return new D() {
      @Override
      public void call(double[] args, double[] res) {
        float[] a2 = new float[args.length];
        float[] r2 = new float[res.length];
        for(int i = 0; i < args.length; i++) {
          a2[i] = (float)args[i];
        }
        func.call(a2, r2);
        for(int i = 0; i < res.length; i++) {
          res[i] = r2[i];
        }
      }
    };
  }
  
  public interface F {
    public void call(float[] args, float[] res);
  }

  public static final FunctionType<F> FUNCTION_F = new FunctionType<F>(F.class);
  
  public static F toF(final D func) {
    return new F() {
      @Override
      public void call(float[] args, float[] res) {
        double[] a2 = new double[args.length];
        double[] r2 = new double[res.length];
        for(int i = 0; i < args.length; i++) {
          a2[i] = args[i];
        }
        func.call(a2, r2);
        for(int i = 0; i < res.length; i++) {
          res[i] = (float)r2[i];
        }
      }
    };
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
  
  public interface IntervalOnRayD3 {
    public void call(RayD3 args, VectorD2 res);
  }
  
  public static ZeroOnRayD3 hasZero(final IntervalOnRayD3 func) {
    return new ZeroOnRayD3() {
      @Override
      public boolean maybeHasZeroOn(RayD3 args) {
        VectorD2 res = new VectorD2(0, 0);
        func.call(args, res);
        return res.x <= 0 && res.y >= 0;
      }
    };
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
  
  public interface All extends D, F, D1_1, F1_1, D2_1, F2_1, D3_1, F3_1, ZeroOnRayD3, IntervalOnRayD3, FillerD, FillerF, RepeatD, RepeatF {
    
  }

}
