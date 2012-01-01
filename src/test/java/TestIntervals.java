import java.util.Random;

import mathray.Computation;
import mathray.Definition;
import mathray.Symbol;
import mathray.eval.machine.MachineEvaluator;
import mathray.eval.split.IntervalTransform;
import mathray.util.MathEx;
import mathray.util.Vector;

import static mathray.Expressions.*;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestIntervals {
  
  private static Random random = new Random();
  
  private static Symbol x = sym("x");
  
  private static void assertWithin(Definition def, double min, double max, double outMin, double outMax) {
    for(int i = 0; i < 100; i++) {
      double x = random.nextDouble() * (max - min) + min;
      double res = MachineEvaluator.eval(def, vector(x));
      assertTrue(res >= outMin && res <= outMax);
    }
  }
  
  private static void fuzzFunction(Definition def) {
    Computation inter = IntervalTransform.intervalize(def.toComputation(), args(x));
    for(int i = 0; i < 100; i++) {
      double min = 1 / (random.nextDouble() * 2 - 1);
      double max = min + 1 / random.nextDouble();
      Vector<Double> out = MachineEvaluator.eval(inter, vector(min, max));
      assertWithin(def, min, max, out.get(0), out.get(1));
    }
  }

  @Test
  public void testFuzz() {
    //fuzzFunction(def(args(x), sin(x)));
  }
  
  private static void assertRange(Definition def, double a, double b, double oa, double ob) {
    Computation inter = IntervalTransform.intervalize(def.toComputation(), args(x));
    Vector<Double> res = MachineEvaluator.eval(inter, vector(a, b));
    assertEquals(vector(oa, ob), res);
  }
  
  @Test
  public void testSin() {
    assertRange(def(args(x), sin(x)), 0, MathEx.TAU, -1, 1);
    assertRange(def(args(x), sin(x)), -10, 10, -1, 1);
    assertRange(def(args(x), sin(x)), 0.5, MathEx.TAU - 0.5, -1, 1);
  }
  
  @Test
  public void testAbs() {
    assertRange(def(args(x), abs(x)), -2, -1, 1, 2);
    assertRange(def(args(x), abs(x)), -2, 1, 0, 2);
    assertRange(def(args(x), abs(x)), -1, 2, 0, 2);
    assertRange(def(args(x), abs(x)), 1, 2, 1, 2);
  }
  
}
