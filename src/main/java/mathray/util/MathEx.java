package mathray.util;

public class MathEx {
  
  private MathEx() {}
  
  public static final double TAU = 2 * Math.PI;
  
  public static double selectSign(double value, double neg, double zeroOrPos) {
    if(value < 0) {
      return neg;
    } else {
      return zeroOrPos;
    }
  }
  
  public static boolean containsZero(double a, double b) {
    return a <= 0 && b >= 0;
  }

}
