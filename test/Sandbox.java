import static mathray.Expressions.*;
import static mathray.Functions.ADD;
import static mathray.Functions.DIV;
import static mathray.Functions.MUL;
import static mathray.Functions.SUB;
import static org.junit.Assert.*;

import mathray.Definition;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.precision.Intervals;
import mathray.eval.simplify.Simplifications;
import mathray.eval.text.ParseInfo;

import org.junit.Test;

public class Sandbox {
  
  @Test
  public void testParser() {
    Variable x = var("x");
    ParseInfo parser = ParseInfo.builder()
      .infix("+", 1, ADD)
      .infix("-", 1, SUB)
      .infix("*", 2, MUL)
      .infix("/", 2, DIV)
      .var(x)
      .build();

    assertEquals(num(0), parser.parse("0"));
    assertEquals(x, parser.parse("x"));
    assertEquals(add(num(1), num(2)), parser.parse("1+2"));
    assertEquals(mul(num(1), num(2)), parser.parse("1*2"));
    assertEquals(add(num(1), mul(num(2), num(3))), parser.parse("1+2*3"));
    assertEquals(mul(add(num(1), num(2)), num(3)), parser.parse("1*2+3"));
  }
  
  private static void assertIntervalizesTo(Definition def, Vector<Variable> vars, Definition result) {
    assertEquals(result, Simplifications.simplify(Intervals.intervalize(def, vars)));
  }
  
  @Test
  public void testInterval() {
    Variable x = var("x");
    
    assertIntervalizesTo(def(args(x), div(num(1), x)), vector(x), null);
  }

}
