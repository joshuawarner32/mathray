import static mathray.Expressions.*;
import static mathray.Functions.*;
import static org.junit.Assert.*;

import mathray.Definition;
import mathray.Symbol;
import mathray.Vector;
import mathray.eval.precision.Intervals;
import mathray.eval.simplify.Simplifications;

import org.junit.Test;

public class Sandbox {
  
  private static void assertIntervalizesTo(Definition def, Vector<Symbol> vars, Definition result) {
    assertEquals(result, Simplifications.simplify(Intervals.intervalize(def.toComputation(), vars)));
  }
  
  @Test
  public void testInterval() {
    Symbol x = sym("x");
    
    assertIntervalizesTo(def(args(x), div(num(1), x)), vector(x), null);
  }

}
