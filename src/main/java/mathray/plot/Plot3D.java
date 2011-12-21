package mathray.plot;


import java.util.Arrays;

import mathray.concrete.FunctionTypes;
import mathray.concrete.RayD3;
import mathray.concrete.VectorD3;




public class Plot3D {
  
  private Plot3D() {}
  
  public static boolean solve(FunctionTypes.ZeroOnRayD3 func, RayD3 solution, double max, double error) {
    double maxSq = max * max;
    double errorSq = error * error;
    VectorD3 start = solution.point();
    while(true) {
      while(!func.maybeHasZeroOn(solution)) {
        solution.increment();
        solution.scale(2);
        if(solution.pointDistanceSq(start) > maxSq) {
          return false;
        }
      }
      while(true) {
        solution.scale(0.5);
        if(!func.maybeHasZeroOn(solution)) {
          solution.increment();
          if(!func.maybeHasZeroOn(solution)) {
            break;
          }
        }
        if(solution.lengthSq() <= errorSq) {
          return true;
        }
      }
      solution.increment();
    }
  }
}
