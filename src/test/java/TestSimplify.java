
import static mathray.Expressions.*;
import static mathray.NamedConstants.*;
import static org.junit.Assert.*;
import mathray.Call;
import mathray.Symbol;
import mathray.Value;
import mathray.eval.simplify.Simplifications;

import org.junit.Test;


public class TestSimplify {

  private static void assertSimplifiesTo(Value value, Value result) {
    assertEquals(result, Simplifications.simplify(value));
  }
  
  Symbol x = sym("x");
  Symbol y = sym("y");
  Symbol z = sym("z");
  
  @Test
  public void testIrreducable() {
    assertSimplifiesTo(x, x);
    assertSimplifiesTo(num(0), num(0));
    assertSimplifiesTo(num(1, 2), num(1, 2));
  }
  
  @Test
  public void testRationalReduction() {
    assertSimplifiesTo(div(num(1), num(2)), num(1, 2));
  }
  
  @Test
  public void testAdditiveIdentity() {
    assertSimplifiesTo(add(x, num(0)), x);
    assertSimplifiesTo(add(num(0), x), x);
    assertSimplifiesTo(add(num(3), num(0)), num(3));
    assertSimplifiesTo(add(num(0), num(3)), num(3));
  }
  
  @Test
  public void testOffsetCompression() {
    assertSimplifiesTo(add(num(1), num(2), x), add(num(3), x));
    assertSimplifiesTo(add(num(1), x, num(2)), add(num(3), x));
    assertSimplifiesTo(add(x, num(1), num(2)), add(num(3), x));
  }
  
  @Test
  public void testMultiplicitiveReassociation() {
    assertSimplifiesTo(mul(x, mul(y, z)), mul(mul(y, z), x));
    assertSimplifiesTo(mul(mul(x, y), z), mul(mul(x, y), z));
  }
  
  @Test
  public void testAdditiveReassociation() {
    assertSimplifiesTo(add(x, add(y, z)), add(add(y, z), x));
    assertSimplifiesTo(add(add(x, y), z), add(add(x, y), z));
  }
  
  @Test
  public void testReorderSubtract() {
    assertSimplifiesTo(sub(neg(x), num(-3)), sub(num(3), x));
  }
  
  @Test
  public void testAddNegative() {
    assertSimplifiesTo(add(x, num(-3)), sub(x, num(3)));
    assertSimplifiesTo(add(x, neg(y)), sub(x, y));
    assertSimplifiesTo(neg(add(num(1, 2), num(-2))), num(3, 2));
    assertSimplifiesTo(add(x, mul(x, num(-3))), sub(x, mul(num(3), x)));
  }
  
  @Test
  public void testMultiplyReciprocal() {
    assertSimplifiesTo(mul(x, div(num(1), y)), div(x, y));
    assertSimplifiesTo(mul(x, div(num(1), y), div(num(1), z)), div(x, mul(y, z)));
    assertSimplifiesTo(mul(x, pow(y, num(-1))), div(x, y));
  }
  
  @Test
  public void testMultiplicitiveIdentity() {
    assertSimplifiesTo(mul(x, num(1)), x);
    assertSimplifiesTo(mul(num(1), x), x);
    assertSimplifiesTo(mul(num(3), num(1)), num(3));
    assertSimplifiesTo(mul(num(1), num(3)), num(3));
  }
  
  @Test
  public void testMultiplicitiveNulling() {
    assertSimplifiesTo(mul(x, num(0)), num(0));
    assertSimplifiesTo(mul(num(0), x), num(0));
    assertSimplifiesTo(mul(num(3), num(0)), num(0));
    assertSimplifiesTo(mul(num(0), num(3)), num(0));
  }
  
  @Test
  public void testPowerSpecialCases() {
    //assertSimplifiesTo(pow(x, num(0))), num(1)));
    assertSimplifiesTo(pow(x, num(1)), x);
    assertSimplifiesTo(pow(x, num(-1)), div(num(1), x));
    assertSimplifiesTo(pow(x, num(1, 2)), sqrt(x));
  }
  
  @Test
  public void testCoefficientCompression() {
    assertSimplifiesTo(mul(num(1), num(2), x), mul(num(2), x));
    assertSimplifiesTo(mul(num(1), x, num(2)), mul(num(2), x));
    assertSimplifiesTo(mul(x, num(1), num(2)), mul(num(2), x));
    assertSimplifiesTo(sin(mul(num(1), num(-2))), sin(num(-2)));
  }
  
  @Test
  public void testDoubleNegative() {
    assertSimplifiesTo(sub(num(1), neg(x)), add(num(1), x));
    assertSimplifiesTo(sub(x, neg(num(1))), add(num(1), x));
    assertSimplifiesTo(sub(x, num(-1)), add(num(1), x));
    assertSimplifiesTo(add(num(1), neg(neg(x))), add(num(1), x));
    assertSimplifiesTo(neg(neg(sin(x))), sin(x));
  }
  
  @Test
  public void testRationalReciprocal() {
    assertSimplifiesTo(div(num(-2), num(1, 4)), num(-8));
  }
  
  @Test
  public void testPowers() {
    assertSimplifiesTo(pow(pow(x, x), num(2)), pow(x, mul(num(2), x)));
    assertSimplifiesTo(pow(x, pow(x, num(2))), pow(x, pow(x, num(2))));
    assertSimplifiesTo(div(num(1), pow(x, num(2))), pow(x, num(-2)));
    assertSimplifiesTo(div(num(1), pow(x, x)), pow(x, neg(x)));
  }
  
  @Test
  public void testPiTau() {
    assertSimplifiesTo(PI, div(TAU, num(2)));
  }
  
  @Test
  public void testTrigIdentities() {
    assertSimplifiesTo(sin(num(0)), num(0));
    assertSimplifiesTo(sin(TAU), num(0));
    assertSimplifiesTo(sin(mul(num(5), TAU)), num(0));
    assertSimplifiesTo(sin(mul(TAU, num(5))), num(0));
    assertSimplifiesTo(sin(mul(TAU, x)), sin(mul(TAU, x)));

    assertSimplifiesTo(cos(num(0)), num(1));
    assertSimplifiesTo(cos(TAU), num(1));
    assertSimplifiesTo(cos(mul(num(5), TAU)), num(1));
    assertSimplifiesTo(cos(mul(TAU, num(5))), num(1));
  }
  
  @Test
  public void testCommonSubexpression() {
    Call c = (Call)Simplifications.simplify(add(sin(x), sin(x)));
    assertSame(c.args.get(0), c.args.get(1));
  }

}
