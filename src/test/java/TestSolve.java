
import static mathray.Expressions.*;
import static org.junit.Assert.assertFalse;
import mathray.Computation;
import mathray.Symbol;
import mathray.concrete.FunctionTypes.ZeroOnRayD3;
import mathray.concrete.RayD3;
import mathray.concrete.VectorD3;
import mathray.eval.java.JavaCompiler;
import mathray.eval.precision.Intervals;
import mathray.plot.Plot3D;
import mathray.util.Vector;

import org.junit.Test;


public class TestSolve {
  
  Symbol x = sym("x");
  Symbol y = sym("y");
  Symbol z = sym("z");
  
  Vector<Symbol> vars = vector(x, y, z);

  private void assertSolvesTo(Computation comp, RayD3 solution, VectorD3 res) {
    Computation ival = Intervals.intervalize(comp, vars);
    ZeroOnRayD3 f = JavaCompiler.compile(JavaCompiler.MAYBE_ZERO_ON_RAY3, ival);
    double eps = 0.001;
    Plot3D.solve(f, solution, 100, eps * 2);
    
    assertFalse(solution.pointDistanceSq(res) > eps * 2);
  }
  
  @Test
  public void testPlane() {
    assertSolvesTo(compute(args(x, y, z), add(x, y, z, num(-1))), new RayD3(0, 10, 0, 0, -1, 0), new VectorD3(0, 1, 0));
  }
  
  @Test
  public void testSphere() {
    assertSolvesTo(compute(args(x, y, z), sub(add(pow(x, 2), pow(y, 2), pow(z, 2)), num(1))), new RayD3(0, 10, 0, 0, -1, 0), new VectorD3(0, 1, 0));
  }
}
