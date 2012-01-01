
import static mathray.Expressions.*;
import static org.junit.Assert.*;
import mathray.Args;
import mathray.Multidef;
import mathray.Symbol;
import mathray.concrete.RayD3;
import mathray.concrete.VectorD3;
import mathray.device.FunctionTypes.ZeroOnRayD3;
import mathray.eval.java.JavaCompiler;
import mathray.eval.split.IntervalTransform;
import mathray.plot.Plot3D;

import org.junit.Test;

public class TestSolve {
  
  Symbol x = sym("x");
  Symbol y = sym("y");
  Symbol z = sym("z");
  
  Args args = args(x, y, z);

  private void assertSolvesTo(Multidef def, RayD3 solution, VectorD3 res) {
    Multidef ival = IntervalTransform.intervalize(def, args);
    ZeroOnRayD3 f = JavaCompiler.compile(JavaCompiler.MAYBE_ZERO_ON_RAY3, ival);
    double eps = 0.001;
    assertTrue(Plot3D.solve(f, solution, 100, eps * 2));
    
    assertFalse(solution.pointDistanceSq(res) > eps * 2);
  }
  
  public void assertNoSolution(Multidef def, RayD3 solution) {
    Multidef ival = IntervalTransform.intervalize(def, args);
    ZeroOnRayD3 f = JavaCompiler.compile(JavaCompiler.MAYBE_ZERO_ON_RAY3, ival);
    double eps = 0.001;
    assertFalse(Plot3D.solve(f, solution, 100, eps * 2));
  }
  
  @Test
  public void testNoSolution() {
    assertNoSolution(multidef(args, add(x, y, z, num(-1))), new RayD3(0, 10, 0, 0, 1, 0));
  }
  
  @Test
  public void testPlane() {
    assertSolvesTo(multidef(args, add(x, y, z, num(-1))), new RayD3(0, 10, 0, 0, -1, 0), new VectorD3(0, 1, 0));
  }
  
  /*
    TODO: For now, we can't do pow in intervals.
  @Test
  public void testSphere() {
    assertSolvesTo(compute(args, sub(add(pow(x, 2), pow(y, 2), pow(z, 2)), num(1))), new RayD3(0, 10, 0, 0, -1, 0), new VectorD3(0, 1, 0));
  }*/
}
