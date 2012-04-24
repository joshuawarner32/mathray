package mathray.device;

import mathray.Multidef;
import mathray.concrete.BlockD3;
import mathray.concrete.RayD3;

public class FunctionTypes {
  
  private FunctionTypes() {}
  
  public interface Func {
    public int getInputArity();
    public int getOutputArity();
  }
  
  public interface D extends Func {
    public void call(double[] args, double[] res);
  }
  
  public static final FunctionType<D, Multidef> FUNCTION_D = new FunctionType<D, Multidef>(D.class, Multidef.class);
  
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
      
      @Override
      public int getInputArity() {
        return func.getInputArity();
      }
      
      @Override
      public int getOutputArity() {
        return func.getOutputArity();
      }
    };
  }
  
  public interface F extends Func {
    public void call(float[] args, float[] res);
  }

  public static final FunctionType<F, Multidef> FUNCTION_F = new FunctionType<F, Multidef>(F.class, Multidef.class);
  
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
      
      @Override
      public int getInputArity() {
        return func.getInputArity();
      }
      
      @Override
      public int getOutputArity() {
        return func.getOutputArity();
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
  
  public interface ZeroInBlockD3 {
    public boolean maybeHasZeroIn(BlockD3 args);
  }
  
  public interface FillerD {
    public void fill(double[] limits, int[] counts, double[] result);
  }
  
  public static final FunctionType<FillerD, Multidef> FUNCTION_FILLER_D = new FunctionType<FillerD, Multidef>(FillerD.class, Multidef.class);
  
  public static FillerD toFillerD(final D func) {
    return new FillerD() {
      private void next(int[] state, int[] counts) {
        for(int i = 0; i < counts.length; i++) {
          state[i]++;
          if(state[i] < counts[i]) {
            break;
          }
        }
      }
      
      @Override
      public void fill(double[] limits, int[] counts, double[] result) {
        double[] in = new double[func.getInputArity()];
        double[] out = new double[func.getOutputArity()];
        
        int samples = 1;
        for(int i = 0; i < counts.length; i++) {
          samples += counts[i];
        }
        
        int[] state = new int[counts.length];
        int outputIndex = 0;
        
        for(int i = 0; i < samples; i++) {
          for(int d = 0; d < counts.length; d++) {
            in[i] = state[i] * (limits[i] - limits[i + counts.length]) / counts[i];
          }
          func.call(in, out);
          for(int d = 0; d < func.getOutputArity(); d++) {
            result[outputIndex++] = out[i];
          }
          next(state, counts);
        }
      }
    };
  }
  
  public interface FillerF {
    public void fill(float[] limits, int[] counts, float[] result);
  }
  
  public static final FunctionType<FillerF, Multidef> FUNCTION_FILLER_F = new FunctionType<FillerF, Multidef>(FillerF.class, Multidef.class);
  
  public interface RepeatD {
    public void repeat(double[] args, double[] res);
  }
  
  public static final FunctionType<RepeatD, Multidef> FUNCTION_REPEAT_D = new FunctionType<RepeatD, Multidef>(RepeatD.class, Multidef.class);
  
  public static RepeatD toRepeatD(final D func) {
    return new RepeatD() {
      @Override
      public void repeat(double[] args, double[] res) {
        double[] in = new double[func.getInputArity()];
        double[] out = new double[func.getOutputArity()];
        if(args.length % func.getInputArity() != 0) {
          throw new IllegalArgumentException();
        }
        int count = args.length / func.getInputArity();
        if(res.length != count * func.getOutputArity()) {
          throw new IllegalArgumentException();
        }
        int inputIndex = 0;
        int outputIndex = 0;
        for(int i = 0; i < count; i++) {
          for(int d = 0; d < in.length; d++) {
            in[d] = args[inputIndex++];
          }
          func.call(in, out);
          for(int d = 0; d < out.length; d++) {
            res[outputIndex++] = out[d];
          }
        }
      }
    };
  }
  
  public interface RepeatF {
    public void repeat(float[] args, float[] res);
  }
  
  public static final FunctionType<RepeatF, Multidef> FUNCTION_REPEAT_F = new FunctionType<RepeatF, Multidef>(RepeatF.class, Multidef.class);
  
  public interface All extends D, F, D1_1, F1_1, D2_1, F2_1, D3_1, F3_1, ZeroOnRayD3, ZeroInBlockD3, FillerD, FillerF, RepeatD, RepeatF {
    
  }
  
  public interface ClosureD<T> {
    public T close(double... args);
  }
  
  public interface ClosureF<T> {
    public T close(float... args);
  }

}
