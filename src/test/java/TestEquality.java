import static mathray.Expressions.*;
import static org.junit.Assert.*;
import mathray.Symbol;

import org.junit.Test;


public class TestEquality {
  
  private Symbol x = sym("x");
  
  @Test
  public void testCallEquality() {
    assertEquals(cos(x), cos(x));
  }
  
  @Test
  public void testCallHashCode() {
    assertEquals(cos(x).hashCode(), cos(x).hashCode());
  }

  @Test
  public void testStructEquality() {
    assertEquals(struct(cos(x)), struct(cos(x)));
  }
  
  @Test
  public void testStructHashCode() {
    assertEquals(struct(cos(x)).hashCode(), struct(cos(x)).hashCode());
  }
  
  @Test
  public void testDefinitionEquality() {
    assertEquals(def(args(x), x), def(args(x), x));
  }
  
  @Test
  public void testMultidefEquality() {
    assertEquals(multidef(args(x), x), multidef(args(x), x));
  }

}
