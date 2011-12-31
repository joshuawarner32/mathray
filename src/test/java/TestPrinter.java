import static mathray.Expressions.*;
import static org.junit.Assert.*;
import mathray.NamedConstants;
import mathray.Symbol;
import org.junit.Test;

public class TestPrinter {
  
  Symbol x = sym("x");
  Symbol y = sym("y");
  
  private static void assertPrintsTo(Object val, String str) {
    assertEquals(str, val.toString());
  }
  
  @Test
  public void testVariables() {
    assertPrintsTo(x, "x");
    assertPrintsTo(NamedConstants.TAU, "tau");
  }
  
  @Test
  public void testRationals() {
    assertPrintsTo(num(0), "0");
    assertPrintsTo(num(1), "1");
    assertPrintsTo(num(0, 1), "0");
    assertPrintsTo(num(1, 2), "1/2");
  }
  
  @Test
  public void testOperators() {
    assertPrintsTo(add(x, y), "x+y");
    assertPrintsTo(sub(x, y), "x-y");
    assertPrintsTo(mul(x, y), "x*y");
    assertPrintsTo(div(x, y), "x/y");
    assertPrintsTo(pow(x, y), "x^y");
  }
  
  @Test
  public void testOrderOfOps() {
    assertPrintsTo(add(num(1), mul(num(2), num(3))), "1+2*3");
    assertPrintsTo(mul(num(1), add(num(2), num(3))), "1*(2+3)");
  }
  
  @Test
  public void testAssociativity() {
    assertPrintsTo(add(add(num(1), num(2)), num(3)), "1+2+3");
    assertPrintsTo(add(num(1), add(num(2), num(3))), "1+(2+3)");
    assertPrintsTo(pow(pow(num(1), num(2)), num(3)), "(1^2)^3");
    assertPrintsTo(pow(num(1), pow(num(2), num(3))), "1^2^3");
  }
  
  @Test
  public void testPrefix() {
    assertPrintsTo(neg(x), "-x");
    assertPrintsTo(neg(num(1)), "-1");
  }
  
  @Test
  public void testFunctions() {
    assertPrintsTo(sin(x), "sin(x)");
    assertPrintsTo(max(x, y), "max(x, y)");
  }
  
  @Test
  public void testDefinition() {
    assertPrintsTo(def(args(x), sin(x)), "f(x) = sin(x)");
    assertPrintsTo(def(args(x, y), max(x, y)), "f(x, y) = max(x, y)");
  }

}
