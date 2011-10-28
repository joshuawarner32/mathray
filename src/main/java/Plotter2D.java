import mathray.FunctionD;


public class Plotter2D {
  
  private Plotter2D() {}
  
  public static double[] sample(FunctionD func, double a, double b, int samples) {
    double[] ret = new double[samples];
    
    double[] args = new double[1];
    double[] res = new double[1];
    
    double diff = b - a;
    
    for(int i = 0; i < samples; i++) {
      args[0] = diff * i + a;
      func.call(args, res);
      ret[i] = res[0];
    }
    
    return ret;
  }

}
