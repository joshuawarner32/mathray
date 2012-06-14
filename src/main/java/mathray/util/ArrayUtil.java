package mathray.util;

public class ArrayUtil {
  
  public static int product(int... numbers) {
    int res = 1;
    for(int d : numbers) {
      res *= d;
    }
    return res;
  }

}
