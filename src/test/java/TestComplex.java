
import static mathray.Expressions.*;
import org.junit.Test;
import static org.junit.Assert.*;

import mathray.Args;
import mathray.Multidef;
import mathray.Definition;
import mathray.Symbol;
import mathray.eval.machine.MachineEvaluator;
import mathray.eval.split.ComplexTransform;
import mathray.util.Vector;

public class TestComplex {
  
  private static void assertComplexEvalsTo(Definition def, Vector<Double> input, Vector<Double> output) {
    Multidef comp = ComplexTransform.complexify(def.toMultidef(), def.args);
    Vector<Double> res = MachineEvaluator.eval(comp, input);
    assertEquals(output, res);
  }
  
  Symbol x = sym("x");
  Symbol y = sym("y");
  Args args1 = args(x);
  Args args2 = args(x, y);
  
  @Test
  public void testComplex() {
    assertComplexEvalsTo(def(args1, sqrt(x)), vector(-4.0, 0.0), vector(1.2246467991473532E-16, 2.0));
  }

}
