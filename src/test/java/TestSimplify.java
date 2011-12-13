
import static mathray.NamedConstants.*;
import static mathray.Expressions.*;
import static org.junit.Assert.assertEquals;
import mathray.Definition;
import mathray.Symbol;
import mathray.eval.simplify.Simplifications;

import org.junit.Test;


public class TestSimplify {

  private static void assertSimplifiesTo(Definition def, Definition result) {
    assertEquals(result, Simplifications.simplify(def.toComputation()).get(0));
  }
  
  Symbol x = sym("x");
  Symbol y = sym("y");
  Symbol z = sym("z");
  
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
  public void testMultiplicitiveReassociation() {
    assertSimplifiesTo(def(args(x), mul(x, mul(y, z))), def(args(x), mul(mul(y, z), x)));
    assertSimplifiesTo(def(args(x), mul(mul(x, y), z)), def(args(x), mul(mul(x, y), z)));
  }
  
  @Test
  public void testAdditiveReassociation() {
    assertSimplifiesTo(def(args(x), add(x, add(y, z))), def(args(x), add(add(y, z), x)));
    assertSimplifiesTo(def(args(x), add(add(x, y), z)), def(args(x), add(add(x, y), z)));
  }
  
  @Test
  public void testReorderSubtract() {
    assertSimplifiesTo(def(args(x), sub(neg(x), num(-3))), def(args(x), sub(num(3), x)));
  }
  
  @Test
  public void testAddNegative() {
    //assertSimplifiesTo(def(args(x), add(x, num(-3))), def(args(x), sub(x, num(3))));
    //assertSimplifiesTo(def(args(x), add(x, neg(y))), def(args(x), sub(x, y)));
    assertSimplifiesTo(def(args(x), neg(add(num(1, 2), num(-2)))), def(args(x), num(3, 2)));    
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
    assertSimplifiesTo(def(args(x), neg(neg(sin(x)))), def(args(x), sin(x)));
  }
  
  @Test
  public void testRationalReciprocal() {
    assertSimplifiesTo(def(args(x), div(num(-2), num(1, 4))), def(args(x), num(-8)));
  }
  
  @Test
  public void testPowers() {
    assertSimplifiesTo(def(args(x), pow(pow(x, x), num(2))), def(args(x), pow(x, mul(num(2), x))));
    assertSimplifiesTo(def(args(x), pow(x, pow(x, num(2)))), def(args(x), pow(x, pow(x, num(2)))));
    assertSimplifiesTo(def(args(x), div(num(1), pow(x, num(2)))), def(args(x), pow(x, num(-2))));
    assertSimplifiesTo(def(args(x), div(num(1), pow(x, x))), def(args(x), pow(x, neg(x))));
  }

}
