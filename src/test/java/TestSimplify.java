
import static mathray.Expressions.*;
import static org.junit.Assert.assertEquals;
import mathray.Definition;
import mathray.Variable;
import mathray.eval.simplify.Simplifications;

import org.junit.Test;


public class TestSimplify {

  private static void assertSimplifiesTo(Definition def, Definition result) {
    assertEquals(result, Simplifications.simplify(def));
  }
  
  Variable x = var("x");
  
  @Test
  public void testIrreducable() {
    assertSimplifiesTo(def(args(x), x), def(args(x), x));
    assertSimplifiesTo(def(args(x), num(0)), def(args(x), num(0)));
    assertSimplifiesTo(def(args(x), num(1, 2)), def(args(x), num(1, 2)));
  }
  
  @Test
  public void testRationalReduction() {
    assertSimplifiesTo(def(args(x), div(num(1), num(2))), def(args(x), num(1, 2)));
  }
  
  @Test
  public void testAdditiveIdentity() {
    assertSimplifiesTo(def(args(x), add(x, num(0))), def(args(x), x));
    assertSimplifiesTo(def(args(x), add(num(0), x)), def(args(x), x));
    assertSimplifiesTo(def(args(x), add(num(3), num(0))), def(args(x), num(3)));
    assertSimplifiesTo(def(args(x), add(num(0), num(3))), def(args(x), num(3)));
  }
  
  @Test
  public void testOffsetCompression() {
    assertSimplifiesTo(def(args(x), add(num(1), num(2), x)), def(args(x), add(num(3), x)));
    assertSimplifiesTo(def(args(x), add(num(1), x, num(2))), def(args(x), add(num(3), x)));
    assertSimplifiesTo(def(args(x), add(x, num(1), num(2))), def(args(x), add(num(3), x)));
  }
  
  @Test
  public void testMultiplicitiveIdentity() {
    assertSimplifiesTo(def(args(x), mul(x, num(1))), def(args(x), x));
    assertSimplifiesTo(def(args(x), mul(num(1), x)), def(args(x), x));
    assertSimplifiesTo(def(args(x), mul(num(3), num(1))), def(args(x), num(3)));
    assertSimplifiesTo(def(args(x), mul(num(1), num(3))), def(args(x), num(3)));
  }
  
  @Test
  public void testMultiplicitiveNulling() {
    assertSimplifiesTo(def(args(x), mul(x, num(0))), def(args(x), num(0)));
    assertSimplifiesTo(def(args(x), mul(num(0), x)), def(args(x), num(0)));
    assertSimplifiesTo(def(args(x), mul(num(3), num(0))), def(args(x), num(0)));
    assertSimplifiesTo(def(args(x), mul(num(0), num(3))), def(args(x), num(0)));
  }
  
  @Test
  public void testPowerSpecialCases() {
    //assertSimplifiesTo(def(args(x), pow(x, num(0))), def(args(x), num(1)));
    assertSimplifiesTo(def(args(x), pow(x, num(1))), def(args(x), x));
    assertSimplifiesTo(def(args(x), pow(x, num(-1))), def(args(x), div(num(1), x)));
    assertSimplifiesTo(def(args(x), pow(x, num(1, 2))), def(args(x), sqrt(x)));
  }
  
  @Test
  public void testCoefficientCompression() {
    assertSimplifiesTo(def(args(x), mul(num(1), num(2), x)), def(args(x), mul(num(2), x)));
    assertSimplifiesTo(def(args(x), mul(num(1), x, num(2))), def(args(x), mul(num(2), x)));
    assertSimplifiesTo(def(args(x), mul(x, num(1), num(2))), def(args(x), mul(num(2), x)));
    assertSimplifiesTo(def(args(x), sin(mul(num(1), num(-2)))), def(args(x), sin(num(-2))));
  }
  
  @Test
  public void testDoubleNegative() {
    assertSimplifiesTo(def(args(x), sub(num(1), neg(x))), def(args(x), add(num(1), x)));
    assertSimplifiesTo(def(args(x), sub(x, neg(num(1)))), def(args(x), add(num(1), x)));
    assertSimplifiesTo(def(args(x), sub(x, num(-1))), def(args(x), add(num(1), x)));
    assertSimplifiesTo(def(args(x), add(num(1), neg(neg(x)))), def(args(x), add(num(1), x)));
  }

}
