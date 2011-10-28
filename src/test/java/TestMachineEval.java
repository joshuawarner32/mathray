import static mathray.Expressions.add;
import static mathray.Expressions.args;
import static mathray.Expressions.def;
import static mathray.Expressions.mul;
import static mathray.Expressions.num;
import static mathray.Expressions.sin;
import static mathray.Expressions.var;
import static mathray.Expressions.vector;
import static org.junit.Assert.assertEquals;
import mathray.Definition;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.machine.MachineEvaluator;

import org.junit.Test;

public class TestMachineEval {

  private static void assertEvaluatesTo(Definition def, Vector<Double> args, Vector<Double> results) {
    assertEquals(results, MachineEvaluator.eval(def, args));
  }

  @Test
  public void testMachineEvaluation() {
    Variable x = var("x");
    assertEvaluatesTo(def(args(x), add(num(2), x)),                   vector(2.0), vector(4.0));
    assertEvaluatesTo(def(args(x), add(x, add(num(2), num(2)))),      vector(2.0), vector(6.0));
    assertEvaluatesTo(def(args(x), mul(num(2), add(x, num(2)))),      vector(2.0), vector(8.0));
    assertEvaluatesTo(def(args(x), add(add(num(2), num(2)), num(2))), vector(2.0), vector(6.0));
    assertEvaluatesTo(def(args(x), mul(add(num(2), num(2)), x)),      vector(2.0), vector(8.0));
    assertEvaluatesTo(def(args(x), sin(x)),                           vector(2.0), vector(0.9092974268256817));
  }
}
