import mathray.FunctionD;


public class Test implements FunctionD {
  @Override
  public void call(double[] args, double[] res) {
    double a = args[0];
    double b = args[1];
    double r = a + b;
    res[0] = r;
  }
}
