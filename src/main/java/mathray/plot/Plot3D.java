package mathray.plot;

import java.util.Arrays;

import mathray.FunctionD;

public class Plot3D {
  
  private Plot3D() {}
  
  private static void next(double[] solution, double[] dir) {
    solution[0] += dir[0];
    solution[1] += dir[1];
    solution[2] += dir[2];
  }
  
  private static void extend(double[] dir, double d) {
    dir[0] *= d;
    dir[1] *= d;
    dir[2] *= d;
  }
  
  private static double diffLenSq(double[] start, double[] cur) {
    double d0 = cur[0] - start[0];
    double d1 = cur[1] - start[1];
    double d2 = cur[2] - start[2];
    return d0 * d0 + d1 * d1 + d2 * d2;
  }
  
  private static double lenSq(double[] vec) {
    return vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2];
  }
  
  private static boolean solve(FunctionD func, double[] solution, double[] dir, double max, double error) {
    double maxSq = max * max;
    double errorSq = error * error;
    double[] start = Arrays.copyOf(solution, 2);
    double[] out = new double[2];
    boolean found;
    func.call(solution, out);
    do {
      while(out[0] > 0 || out[1] < 0) {
        next(solution, dir);
        extend(dir, 2);
        func.call(solution, out);
        if(diffLenSq(start, solution) > maxSq) {
          return false;
        }
      }
      found = true;
      while(lenSq(dir) > errorSq) {
        extend(dir, 0.5);
        func.call(solution, out);
        if(out[0] > 0 || out[1] < 0) {
          next(solution, dir);
          func.call(solution, out);
          if(out[0] > 0 || out[1] < 0) {
            found = false;
            break;
          }
        }
      }
      next(solution, dir);
      func.call(solution, out);
    } while(!found);
    return true;
  }
}
