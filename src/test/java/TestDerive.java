
import static mathray.Expressions.*;
import static mathray.NamedConstants.*;
import static org.junit.Assert.assertEquals;
import mathray.Symbol;
import mathray.Value;
import mathray.eval.calc.Derivatives;
import mathray.eval.simplify.Simplifications;

import org.junit.Test;


public class TestDerive {


  private static void assertDerivesTo(Value value, Symbol x, Value result) {
    assertEquals(result, Simplifications.simplify(Derivatives.derive(value, x)));
  }
  
  Symbol x = sym("x");
  Symbol y = sym("y");
  
  @Test
  public void testTrivialConstants() {
    assertDerivesTo(TAU, x, num(0));
    assertDerivesTo(num(0), x, num(0));
    assertDerivesTo(num(1), x, num(0));
  }
  
  @Test
  public void testNontrivialConstants() {
    assertDerivesTo(sin(num(1)), x, num(0));
    assertDerivesTo(add(num(1), sin(num(2))), x, num(0));
  }

  @Test
  public void testVariable() {
    assertDerivesTo(x, x, num(1));
  }

  @Test
  public void testSum() {
    assertDerivesTo(add(x, y), x, num(1));
  }

  @Test
  public void testProduct() {
    assertDerivesTo(mul(x, y), x, y);
  }

  @Test
  public void testTrig() {
    assertDerivesTo(cos(x), x, sin(x));
    assertDerivesTo(sin(x), x, neg(cos(x)));
    assertDerivesTo(neg(sin(x)), x, cos(x));
  }
  
  @Test
  public void testPow() {
    assertDerivesTo(sqrt(x), x, div(num(1), mul(num(2), sqrt(x))));
  }
}
