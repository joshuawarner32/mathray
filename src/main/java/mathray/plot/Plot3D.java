package mathray.plot;

import mathray.concrete.BlockD3;
import mathray.concrete.MatrixD2;
import mathray.concrete.RayD3;
import mathray.concrete.VectorD3;
import mathray.device.FunctionTypes;

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
  private static interface Tester {
    public void test(BlockD3 block);
  }
  
  public static void blockDivide(final FunctionTypes.ZeroInBlockD3 func, final MatrixD2 mat, final double error, final double max, BlockD3 block) {

    new Tester() {
      private void split(BlockD3 block) {
        int xSplit = block.width / 2;
        int ySplit = block.height / 2;
        test(block.split(block.x,          block.y,          xSplit,               ySplit));
        test(block.split(block.x + xSplit, block.y,          block.width - xSplit, ySplit));
        test(block.split(block.x,          block.y + ySplit, xSplit,               ySplit));
        test(block.split(block.x + xSplit, block.y + ySplit, block.width - xSplit, block.height - ySplit));
      }
      public void test(BlockD3 block) {
        if(block.width == 0 || block.height == 0) {
          return;
        }
        while(true) {
          if(func.maybeHasZeroIn(block)) {
            if(block.width == 1 && block.height == 1 && block.depth() <= error) {
              block.putOn(mat);
            } else {
              split(block);
            }
            break;
          } else {
            block.next();
          }
        }
      }
    }.test(block);
  }
  
}
