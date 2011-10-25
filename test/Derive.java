import static mathray.Expressions.add;
import static mathray.Expressions.args;
import static mathray.Expressions.cos;
import static mathray.Expressions.def;
import static mathray.Expressions.div;
import static mathray.Expressions.mul;
import static mathray.Expressions.neg;
import static mathray.Expressions.num;
import static mathray.Expressions.sin;
import static mathray.Expressions.sqrt;
import static mathray.Expressions.var;
import static org.junit.Assert.assertEquals;
import mathray.Definition;
import mathray.Variable;
import mathray.eval.calc.Derivatives;
import mathray.eval.simplify.Simplifications;

import org.junit.Test;


public class Derive {


  private static void assertDerivesTo(Definition def, Variable x, Definition result) {
    assertEquals(result, Simplifications.simplify(Derivatives.derive(def, x)));
  }
  
  @Test
  public void testDerive() {
    Variable x = var("x");
    Variable y = var("y");
    assertDerivesTo(def(args(x), x), x, def(args(x), num(1)));
    assertDerivesTo(def(args(x, y), add(x, y)), x, def(args(x, y), num(1)));
    assertDerivesTo(def(args(x, y), mul(x, y)), x, def(args(x, y), y));
    
    assertDerivesTo(def(args(x), cos(x)), x, def(args(x), sin(x)));
    assertDerivesTo(def(args(x), sin(x)), x, def(args(x), neg(cos(x))));
    assertDerivesTo(def(args(x), neg(sin(x))), x, def(args(x), cos(x)));

    assertDerivesTo(def(args(x), sqrt(x)), x, def(args(x), div(num(1), mul(num(2), sqrt(x)))));
  }
}
