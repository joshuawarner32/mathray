import static mathray.Expressions.add;
import static mathray.Expressions.args;
import static mathray.Expressions.def;
import static mathray.Expressions.mul;
import static mathray.Expressions.num;
import static mathray.Expressions.var;
import static org.junit.Assert.assertEquals;
import mathray.Definition;
import mathray.Variable;
import mathray.eval.simplify.Simplifications;

import org.junit.Test;


public class Simplify {

  private static void assertSimplifiesTo(Definition def, Definition result) {
    assertEquals(result, Simplifications.simplify(def));
  }
  
  @Test
  public void testSimplify() {
    Variable x = var("x");
    assertSimplifiesTo(def(args(x), x), def(args(x), x));
    assertSimplifiesTo(def(args(x), add(x, num(0))), def(args(x), x));
    assertSimplifiesTo(def(args(x), add(num(0), x)), def(args(x), x));
    assertSimplifiesTo(def(args(x), add(num(1), x)), def(args(x), add(num(1), x)));
    assertSimplifiesTo(def(args(x), mul(x, num(1))), def(args(x), x));
    assertSimplifiesTo(def(args(x), mul(num(1), x)), def(args(x), x));
    assertSimplifiesTo(def(args(x), mul(x, num(0))), def(args(x), num(0)));
    assertSimplifiesTo(def(args(x), mul(num(0), x)), def(args(x), num(0)));
  }

}
