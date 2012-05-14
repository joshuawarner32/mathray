import static org.junit.Assert.*;
import static mathray.Expressions.*;

import mathray.Multidef;
import mathray.Symbol;
import mathray.eval.linear.LinearArithmetic;

import org.junit.Test;


public class TestLinearArithmetic {
  
  private Symbol x = sym("x");
  
  private void assertLinearizesTo(Multidef def, Multidef result) {
    Multidef out = LinearArithmetic.linearize(def, def.args);
    assertEquals(result, out);
  }
  
  @Test
  public void testConstants() {
    assertLinearizesTo(multidef(args(x), num(2)), multidef(args(x), num(2)));
  }

}
