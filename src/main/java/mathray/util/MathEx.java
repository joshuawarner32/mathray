package mathray.util;

public class MathEx {
  
  private MathEx() {}
  
  public static double selectSign(double value, double neg, double zero, double pos) {
    if(value < 0) {
      return neg;
    } else if(value > 0) {
      return pos;
    } else {
      return zero;
    }
  }
  
  public static boolean containsZero(double a, double b) {
    return a <= 0 && b >= 0;
  }

}
