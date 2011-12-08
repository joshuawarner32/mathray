import java.util.Random;

import mathray.Computation;
import mathray.Definition;
import mathray.Symbol;
import mathray.Vector;
import mathray.eval.machine.MachineEvaluator;
import mathray.eval.precision.Intervals;

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
      assertTrue(res >= min && res <= max);
    }
  }
  
  private static void fuzzFunction(Definition def) {
    Computation inter = Intervals.intervalize(def.toComputation(), vector(x));
    for(int i = 0; i < 100; i++) {
      double min = 1 / (random.nextDouble() * 2 - 1);
      double max = min + 1 / random.nextDouble();
      Vector<Double> out = MachineEvaluator.eval(inter, vector(min, max));
      assertWithin(def, min, max, out.get(0), out.get(1));
    }
  }

  @Test
  public void testFuzz() {
    // TODO: teach MachineEvaluator about tau, etc
    //fuzzFunction(def(args(x), sin(x)));
  }
  
}
