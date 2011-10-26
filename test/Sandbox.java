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
  
  private static void assertIntervalizesTo(Definition def, Vector<Variable> vars, Definition result) {
    assertEquals(result, Simplifications.simplify(Intervals.intervalize(def, vars)));
  }
  
  @Test
  public void testInterval() {
    Variable x = var("x");
    
    assertIntervalizesTo(def(args(x), div(num(1), x)), vector(x), null);
  }

}
