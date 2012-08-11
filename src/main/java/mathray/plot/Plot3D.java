package mathray.plot;

import java.awt.image.BufferedImage;

import mathray.Definition;
import mathray.Lambda;
import mathray.Multidef;
import mathray.concrete.BlockD3;
import mathray.concrete.CameraD3;
import mathray.concrete.RayD3;
import mathray.concrete.VectorD3;
import mathray.device.FunctionTypes;
import mathray.eval.calc.Derivatives;
import mathray.eval.java.JavaDevice;
import mathray.eval.simplify.Simplifications;
import mathray.eval.split.IntervalTransform;
import mathray.eval.transform.Project;
import mathray.flow.Data;
import mathray.flow.concrete.DataD;
import mathray.util.ImageUtil;

import static mathray.Expressions.*;

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
  
  public static DataD blockDivide(final FunctionTypes.ZeroInBlockD3 func, int width, int height, final double error, final double max, BlockD3 block) {
    final DataD.Builder3 mat = new DataD.Builder3(width, height, 1);
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
                mat.put(block.x, block.y, 0, (block.z0 + block.z1) / 2);
                
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
    return mat.build();
  }
  
  public static BufferedImage plotBlockFunction(FunctionTypes.ZeroInBlockD3 func, FunctionTypes.D output, int width, int height, double error, double max) {
    BlockD3 block = new BlockD3(-1, -1, 0, 1, 1, 1, 0, 0, width, height);
    Data mat = blockDivide(func, width, height, error, max, block);
    
    return renderData(mat, output, -1, 1, -1, 1);
  }
  
  private static byte fixByte(float data) {
    data = Math.min(Math.max(data, 0), 1);
    return (byte)(data * 255);
  }
  
  private static int color(byte r, byte g, byte b) {
    return (r << 16) + (g << 8) + (b << 0);
  }
  
  private static BufferedImage renderData(Data input, FunctionTypes.D f, double xmin, double xmax, double ymin, double ymax) {
    DataD.View3 in = ((DataD)input).asView3();
    int[] out = new int[in.width * in.height];

    double[] ia = new double[3];
    double[] oa = new double[3];
    
    for(int y = 0; y < in.height; y++) {
      double yfact = y / (double)(in.height - 1);
      double yval = ymin * (1 - yfact) + ymax * yfact;
      
      ia[1] = yval;
      
      for(int x = 0; x < in.width; x++) {
        double xfact = x / (double)(in.width - 1);
        double xval = xmin * (1 - xfact) + xmax * xfact;
        
        ia[0] = xval;
        
        double zval = in.get(x, y, 0);
        
        ia[2] = zval;
        
        f.call(ia, oa);
        int color;
        if(hasInfOrNaN(oa)) {
          color = 0xff00ffff;
        } else {
          color = color(
              fixByte((float)oa[0]),
              fixByte((float)oa[1]),
              fixByte((float)oa[2]));
        }
        out[x + (in.height - y - 1) * in.width] = color;
      }
    }
    return ImageUtil.imageFromArray(out, in.width, in.height);
  }
  
  private static boolean hasInfOrNaN(double[] oa) {
    for(double d : oa) {
      if(Double.isInfinite(d) || Double.isNaN(d)) {
        System.out.println(d);
        return true;
      }
    }
    return false;
  }

  public static BufferedImage plotBlockDefault(Definition def, CameraD3 cam, int width, int height, double error, double max) {
    Lambda<Multidef> proj = Project.project(def.toMultidef(), def.args);
    proj = proj.close(Simplifications.simplify(proj.value));
    System.out.println(proj);
    Lambda<Multidef> inter = proj.close(Simplifications.simplify(IntervalTransform.intervalize(proj.value, proj.value.args)));
    FunctionTypes.ClosureD<FunctionTypes.ZeroInBlockD3> func = JavaDevice.compile(JavaDevice.closureD(JavaDevice.MAYBE_ZERO_IN_BLOCKD3), inter);
    System.out.println(cam);
    
    Multidef deriv = Simplifications.simplify(Derivatives.multiDerive(def, def.args));
    System.out.println("original derivitive");
    System.out.println(deriv);
    deriv = normalize(deriv);
    deriv = Simplifications.simplify(deriv);
    deriv = zip(def(args(x), div(add(x, 1), 2)), deriv);
    deriv = Simplifications.simplify(deriv);
    Lambda<Multidef> derivProj = Project.project(deriv, def.args);
    FunctionTypes.ClosureD<FunctionTypes.D> derivFunc = JavaDevice.compile(JavaDevice.closureD(JavaDevice.FUNCTION_D), derivProj);
    
    return plotBlockFunction(func.close(cam.args()), derivFunc.close(cam.args()), width, height, 0.001, 100);
  }
  
}
