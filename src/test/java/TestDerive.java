
import static mathray.Expressions.*;
import static mathray.NamedConstants.*;
import static org.junit.Assert.assertEquals;
import mathray.Definition;
import mathray.Variable;
import mathray.eval.calc.Derivatives;
import mathray.eval.simplify.Simplifications;

import org.junit.Test;


public class TestDerive {


  private static void assertDerivesTo(Definition def, Variable x, Definition result) {
    assertEquals(result, Simplifications.simplify(Derivatives.derive(def, x)));
  }
  
  Variable x = var("x");
  Variable y = var("y");
  
  @Test
  public void testConstants() {
    assertDerivesTo(def(args(x), PI), x, def(args(x), num(0)));
    assertDerivesTo(def(args(x), num(0)), x, def(args(x), num(0)));
    assertDerivesTo(def(args(x), num(1)), x, def(args(x), num(0)));
    assertDerivesTo(def(args(x), sin(num(1))), x, def(args(x), num(0)));
    assertDerivesTo(def(args(x), add(num(1), sin(num(2)))), x, def(args(x), num(0)));
  }

  @Test
  public void testVariable() {
    assertDerivesTo(def(args(x), x), x, def(args(x), num(1)));
  }

  @Test
  public void testSum() {
    assertDerivesTo(def(args(x, y), add(x, y)), x, def(args(x, y), num(1)));
  }

  @Test
  public void testProduct() {
    assertDerivesTo(def(args(x, y), mul(x, y)), x, def(args(x, y), y));
  }

  @Test
  public void testTrig() {
    assertDerivesTo(def(args(x), cos(x)), x, def(args(x), sin(x)));
    assertDerivesTo(def(args(x), sin(x)), x, def(args(x), neg(cos(x))));
    assertDerivesTo(def(args(x), neg(sin(x))), x, def(args(x), cos(x)));
  }
  
  @Test
  public void testPow() {
    assertDerivesTo(def(args(x), sqrt(x)), x, def(args(x), div(num(1), mul(num(2), sqrt(x)))));
  }
}
