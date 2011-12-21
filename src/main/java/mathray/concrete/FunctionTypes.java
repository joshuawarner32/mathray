package mathray.concrete;

public class FunctionTypes {
  
  private FunctionTypes() {}
  
  public interface D {
    public void call(double[] args, double[] res);
  }
  
  public interface D1_1 {
    public double call(double x);
  }
  
  public interface D2_1 {
    public double call(double x, double y);
  }
  
  public interface D3_1 {
    public double call(double x, double y, double z);
  }
  
  public interface ZeroOnRay3 {
    public boolean maybeHasZeroOn(Ray3 args);
  }

}
