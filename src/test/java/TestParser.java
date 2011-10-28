import static mathray.Expressions.*;
import static mathray.Functions.*;
import static org.junit.Assert.*;
import mathray.Variable;
import mathray.eval.text.ParseInfo;
import mathray.eval.text.ParseInfo.Associativity;

import org.junit.Test;


public class TestParser {
  

  Variable x = var("x");
  Variable y = var("y");
  ParseInfo parser = ParseInfo.builder()
    .group("(", ")")
    .infix("+", 1, Associativity.LEFT, ADD.select(0))
    .infix("-", 1, Associativity.LEFT, SUB.select(0))
    .infix("*", 2, Associativity.LEFT, MUL.select(0))
    .infix("/", 2, Associativity.LEFT, DIV.select(0))
    .infix("^", 3, Associativity.RIGHT, POW.select(0))
    .function("sin", SIN.select(0))
    .var(x)
    .var(y)
    .build();
  
  @Test
  public void testSimple() {
    assertEquals(num(0), parser.parse("0"));
    assertEquals(x, parser.parse("x"));
  }

  @Test
  public void testOperators() {
    assertEquals(add(num(1), num(2)), parser.parse("1+2"));
    assertEquals(mul(num(1), num(2)), parser.parse("1*2"));
    assertEquals(sub(num(1), num(2)), parser.parse("1-2"));
    assertEquals(div(num(1), num(2)), parser.parse("1/2"));
    assertEquals(pow(num(1), num(2)), parser.parse("1^2"));
  }
  
  @Test
  public void testOrderOfOps() {
    assertEquals(add(num(1), mul(num(2), num(3))), parser.parse("1+2*3"));
    assertEquals(add(mul(num(1), num(2)), num(3)), parser.parse("1*2+3"));
  }
  
  @Test
  public void testParentheses() {
    assertEquals(mul(add(num(1), num(2)), num(3)), parser.parse("(1+2)*3"));
    assertEquals(mul(num(1), add(num(2), num(3))), parser.parse("1*(2+3)"));
  }
  
  @Test
  public void testAssociativity() {
    assertEquals(add(add(num(1), num(2)), num(3)), parser.parse("1+2+3"));
    assertEquals(mul(mul(num(1), num(2)), num(3)), parser.parse("1*2*3"));
    assertEquals(pow(num(1), pow(num(2), num(3))), parser.parse("1^2^3"));
  }
  
  // @Test
  // public void testFunctions() {
  //   assertEquals(sin(x), parser.parse("sin(x)"));
  //   assertEquals(min(x, y), parser.parse("min(x, y)"));
  // }
  
  // @Test
  // public void testSimplePrefixOperators() {
  //   assertEquals(neg(num(1)), parser.parse("-1"));
  //   assertEquals(neg(x), parser.parse("-x"));
  // }
  
  // @Test
  // public void testPrefixOrderOfOps() {
  //   assertEquals(neg(pow(num(1), pow(num(2), num(3)))), parser.parse("-1^2^3"));
  //   assertEquals(pow(num(1), neg(pow(num(2), num(3)))), parser.parse("1^-2^3"));
  //   assertEquals(pow(num(1), pow(num(2), neg(num(3)))), parser.parse("1^2^-3"));
  // }

}
