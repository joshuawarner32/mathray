import static mathray.Expressions.*;
import static org.junit.Assert.*;
import mathray.Symbol;
import mathray.eval.text.DefaultPrinter;
import mathray.eval.text.ParseInfo;
import org.junit.Test;


public class TestParser {
  

  private Symbol x = sym("x");
  private Symbol y = sym("y");
  
  private ParseInfo parser = DefaultPrinter.BASIC_FUNCTIONS.toBuilder()
    .values(x, y)
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
  
  @Test
  public void testFunctions() {
    assertEquals(sin(x), parser.parse("sin(x)"));
    assertEquals(min(x, y), parser.parse("min(x, y)"));
    
    assertEquals(add(num(1), sin(x)), parser.parse("1+sin(x)"));
    assertEquals(add(num(1), min(x, y)), parser.parse("1+min(x, y)"));
    assertEquals(add(sin(x), num(1)), parser.parse("sin(x)+1"));
    assertEquals(add(min(x, y), num(1)), parser.parse("min(x, y)+1"));
    
    assertEquals(sin(add(num(1), x)), parser.parse("sin(1+x)"));
    assertEquals(sin(add(x, num(1))), parser.parse("sin(x+1)"));
    assertEquals(min(add(num(1), x), y), parser.parse("min(1+x, y)"));
    assertEquals(min(x, add(num(1), y)), parser.parse("min(x, 1+y)"));
  }
  
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
