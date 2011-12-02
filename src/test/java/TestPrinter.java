import static mathray.Expressions.*;
import static mathray.Functions.*;
import static org.junit.Assert.*;
import mathray.NamedConstants;
import mathray.Symbol;
import mathray.eval.text.InfixOperator.Associativity;
import mathray.eval.text.ParseInfo;
import org.junit.Test;

public class TestPrinter {
  
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
    .build();
  
  @Test
  public void testVariables() {
    assertEquals("x", parser.unparse(x));
    assertEquals("tau", parser.unparse(NamedConstants.TAU));
  }
  
  @Test
  public void testRationals() {
    assertEquals("0", parser.unparse(num(0)));
    assertEquals("1", parser.unparse(num(1)));
    assertEquals("0", parser.unparse(num(0, 1)));
    assertEquals("1/2", parser.unparse(num(1, 2)));
  }
  
  @Test
  public void testOperators() {
    assertEquals("x+y", parser.unparse(add(x, y)));
    assertEquals("x-y", parser.unparse(sub(x, y)));
    assertEquals("x*y", parser.unparse(mul(x, y)));
    assertEquals("x/y", parser.unparse(div(x, y)));
    assertEquals("x^y", parser.unparse(pow(x, y)));
  }
  
  @Test
  public void testOrderOfOps() {
    assertEquals("1+2*3", parser.unparse(add(num(1), mul(num(2), num(3)))));
    assertEquals("1*(2+3)", parser.unparse(mul(num(1), add(num(2), num(3)))));
  }
  
  @Test
  public void testAssociativity() {
    assertEquals("1+2+3", parser.unparse(add(add(num(1), num(2)), num(3))));
    assertEquals("1+(2+3)", parser.unparse(add(num(1), add(num(2), num(3)))));
    assertEquals("(1^2)^3", parser.unparse(pow(pow(num(1), num(2)), num(3))));
    assertEquals("1^2^3", parser.unparse(pow(num(1), pow(num(2), num(3)))));
  }
  
  @Test
  public void testPrefix() {
    assertEquals("-x", parser.unparse(neg(x)));
    assertEquals("-1", parser.unparse(neg(num(1))));
  }

}
