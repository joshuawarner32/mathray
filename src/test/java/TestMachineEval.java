import static mathray.Expressions.add;
import static mathray.Expressions.args;
import static mathray.Expressions.def;
import static mathray.Expressions.mul;
import static mathray.Expressions.num;
import static mathray.Expressions.sin;
import static mathray.Expressions.sym;
import static mathray.Expressions.vector;
import static org.junit.Assert.assertEquals;
import mathray.Definition;
import mathray.Symbol;
import mathray.Vector;
import mathray.eval.machine.MachineEvaluator;

import org.junit.Test;

public class TestMachineEval {

  private static void assertEvaluatesTo(Definition def, Vector<Double> args, double result) {
    assertEquals((Double)result, (Double)MachineEvaluator.eval(def, args));
  }

  @Test
  public void testMachineEvaluation() {
    Symbol x = sym("x");
    assertEvaluatesTo(def(args(x), add(num(2), x)),                   vector(2.0), 4.0);
    assertEvaluatesTo(def(args(x), add(x, add(num(2), num(2)))),      vector(2.0), 6.0);
    assertEvaluatesTo(def(args(x), mul(num(2), add(x, num(2)))),      vector(2.0), 8.0);
    assertEvaluatesTo(def(args(x), add(add(num(2), num(2)), num(2))), vector(2.0), 6.0);
    assertEvaluatesTo(def(args(x), mul(add(num(2), num(2)), x)),      vector(2.0), 8.0);
    assertEvaluatesTo(def(args(x), sin(x)),                           vector(2.0), 0.9092974268256817);
  }
}
