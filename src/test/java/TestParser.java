import static mathray.Expressions.*;
import static mathray.Functions.*;
import static org.junit.Assert.*;
import mathray.Symbol;
import mathray.eval.text.InfixOperator.Associativity;
import mathray.eval.text.ParseInfo;
import org.junit.Test;


public class TestParser {
  

  Symbol x = sym("x");
  Symbol y = sym("y");
  ParseInfo parser = ParseInfo.builder()
    .group("(", ")")
    .infix("+", 10, Associativity.LEFT, ADD)
    .infix("-", 10, Associativity.LEFT, SUB)
    .infix("*", 20, Associativity.LEFT, MUL)
    .infix("/", 20, Associativity.LEFT, DIV)
    .infix("^", 30, Associativity.RIGHT, POW)
    .prefix("-", 25, NEG)
    .function("sin", SIN)
    .sym(x)
    .sym(y)
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
  
//  @Test
//  public void testFunctions() {
//    assertEquals(sin(x), parser.parse("sin(x)"));
//    assertEquals(min(x, y), parser.parse("min(x, y)"));
//  }
  
  @Test
  public void testSimplePrefixOperators() {
    assertEquals(neg(num(1)), parser.parse("-1"));
    assertEquals(neg(x), parser.parse("-x"));
  }
  
  @Test
  public void testPrefixOrderOfOps() {
    assertEquals(neg(pow(num(1), pow(num(2), num(3)))), parser.parse("-1^2^3"));
    assertEquals(pow(num(1), neg(pow(num(2), num(3)))), parser.parse("1^-2^3"));
    assertEquals(pow(num(1), pow(num(2), neg(num(3)))), parser.parse("1^2^-3"));
    
    assertEquals(mul(neg(num(1)), num(2)), parser.parse("-1*2"));
    assertEquals(mul(pow(num(1), neg(num(2))), num(3)), parser.parse("1^-2*3"));
    assertEquals(add(pow(num(1), neg(num(2))), num(3)), parser.parse("1^-2+3"));
    assertEquals(add(num(1), neg(num(2))), parser.parse("1+-2"));
  }

}
