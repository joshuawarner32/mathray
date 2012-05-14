import static org.junit.Assert.*;
import static mathray.Expressions.*;
import static mathray.NamedConstants.*;

import mathray.Multidef;
import mathray.Symbol;
import mathray.eval.linear.LinearArithmetic;

import org.junit.Test;


public class TestLinearArithmetic {
  
  private Symbol x = sym("x");
  private Symbol y = sym("y");
  
  private void assertLinearizesTo(Multidef def, Multidef result) {
    Multidef out = LinearArithmetic.linearize(def, def.args);
    assertEquals(result, out);
  }
  
  @Test
  public void testConstants() {
    assertLinearizesTo(multidef(args(x), num(2)), multidef(args(x), num(2)));
    assertLinearizesTo(multidef(args(x), TAU), multidef(args(x), TAU));
  }
  
  @Test
  public void testLinear() {
    assertLinearizesTo(multidef(args(x), x), multidef(args(x), x));
  }

}
