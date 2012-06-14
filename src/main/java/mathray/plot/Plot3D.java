package mathray.plot;

import java.awt.image.BufferedImage;

import com.google.common.base.Stopwatch;

import mathray.concrete.BlockD3;
import mathray.concrete.RayD3;
import mathray.concrete.VectorD3;
import mathray.device.FunctionTypes;
import mathray.flow.Data;
import mathray.flow.concrete.DataD;
import mathray.flow.concrete.DataD2;

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
    public void print();
  }
  
  public static DataD2 blockDivide(final FunctionTypes.ZeroInBlockD3 func, int width, int height, final double error, final double max, BlockD3 block) {
    final DataD2 mat = new DataD2(width, height, Double.POSITIVE_INFINITY);
    Tester tester = new Tester() {
      private void split(BlockD3 block) {
        int xSplit = block.width / 2;
        int ySplit = block.height / 2;
        test(block.split(block.x,          block.y,          xSplit,               ySplit));
        test(block.split(block.x + xSplit, block.y,          block.width - xSplit, ySplit));
        test(block.split(block.x,          block.y + ySplit, xSplit,               ySplit));
        test(block.split(block.x + xSplit, block.y + ySplit, block.width - xSplit, block.height - ySplit));
      }
      int evals = 0;
      public void test(BlockD3 block) {
        if(block.width == 0 || block.height == 0) {
          return;
        }
        int count = 0;
        while(true) {
          if(block.z1 >= max) {
            return;
          }
          evals++;
          if(func.maybeHasZeroIn(block)) {
            if(block.width == 1 && block.height == 1) {
              if(block.depth() <= error) {
                block.putOn(mat);
                break;
              } else {
                block.half();
              }
            } else if(count < 2) {
              count++;
              block.half();
            } else {
              split(block);
              break;
            }
          } else {
            block.next();
          }
        }
      }
      public void print() {
        System.out.println("evals: " + evals);
      }
    };
    tester.test(block);
    tester.print();
    return mat;
  }
  
  public static BufferedImage plotBlockDepth(final FunctionTypes.ZeroInBlockD3 func, int width, int height, double error, double max) {
    BlockD3 block = new BlockD3(-1, -1, 0, 1, 1, 1, 0, 0, width, height);
    Stopwatch watch = new Stopwatch();
    watch.start();
    DataD2 mat = blockDivide(func, width, height, error, max, block);
    watch.stop();
    double time = watch.elapsedMillis() / 1000.0;
    System.out.println("time: " + time);
    double dmin = mat.nonInfiniteMin();
    double dmax = mat.nonInfiniteMax();
    
    BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        double val = (mat.get(x, y) - dmin) / (dmax - dmin);
        int r = (int)(val * 255);
        int g = (int)(val * 255);
        int b = (int)(val * 255);
        
        ret.setRGB(x, height - 1 - y, (0xff << 24) | (r << 16) | (g << 8) | (b << 0));
      }
    }
    return ret;
  }
  
  public static BufferedImage plotBlockFunction(FunctionTypes.ZeroInBlockD3 func, FunctionTypes.D output, int width, int height, double error, double max) {
    BlockD3 block = new BlockD3(-1, -1, 0, 1, 1, 1, 0, 0, width, height);
    Stopwatch watch = new Stopwatch();
    watch.start();
    Data mat = blockDivide(func, width, height, error, max, block);
    watch.stop();
    
    return null;//output.map(mat);
  }
  
}
