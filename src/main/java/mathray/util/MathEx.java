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
  
  public static double selectEqual(double a, double b, double ifEqual, double ifNotEqual) {
    if(a == b) {
      return ifEqual;
    } else {
      return ifNotEqual;
    }
  }
  
  public static double selectInteger(double value, double ifEven, double ifOdd, double otherwise) {
    long i = (long)value;
    if((double)i == value) {
      if((i & 1) == 0) {
        return ifEven;
      } else {
        return ifOdd;
      }
    } else {
      return otherwise;
    }
  }

}
