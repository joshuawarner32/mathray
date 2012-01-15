import mathray.Multidef;
import mathray.Definition;
import mathray.Symbol;
import mathray.eval.machine.MachineEvaluator;
import mathray.eval.split.IntervalTransform;
import mathray.util.MathEx;
import mathray.util.Vector;

import static mathray.Expressions.*;
import static mathray.NamedConstants.*;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestIntervals {
  
  private static Symbol x = sym("x");
  
  private static void assertRange(Definition def, double a, double b, double oa, double ob) {
    Multidef inter = IntervalTransform.intervalize(def.toMultidef(), args(x));
    Vector<Double> res = MachineEvaluator.eval(inter, vector(a, b));
    assertEquals(vector(oa, ob), res);
  }
  
  @Test
  public void testSin() {
    Definition sin = def(args(x), sin(x));
    assertRange(sin, 0, MathEx.TAU, -1, 1);
    assertRange(sin, -10, 10, -1, 1);
    assertRange(sin, 0.5, MathEx.TAU - 0.5, -1, 1);
  }
  
  @Test
  public void testAbs() {
    Definition abs = def(args(x), abs(x));
    assertRange(abs, -2, -1, 1, 2);
    assertRange(abs, -2, 1, 0, 2);
    assertRange(abs, -1, 2, 0, 2);
    assertRange(abs, 1, 2, 1, 2);
  }
  
  @Test
  public void testPow() {
    assertRange(def(args(x), pow(x, 2)), 0, 1, 0, 1);
    assertRange(def(args(x), pow(E, x)), 0, 1, 1, Math.E);
    assertRange(def(args(x), pow(x, 2)), -1, 2, 0, 4);
  }
  
}
