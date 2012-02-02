
import static mathray.Expressions.*;
import org.junit.Test;
import mathray.Args;
import mathray.Multidef;
import mathray.Definition;
import mathray.Symbol;
import mathray.eval.split.ComplexTransform;
import mathray.eval.split.IntervalTransform;

public class TestComplexAndIntervals {
  
  private static void assertEquivalent(Multidef a, Multidef b) {
    //TODO
  }
  
  private static void assertSymmetry(Definition def) {
    Multidef c = def.toMultidef();
    Multidef complex = ComplexTransform.complexify(c, c.args);
    Multidef interval = IntervalTransform.intervalize(c, c.args);
    
    Multidef intervalComplex = IntervalTransform.intervalize(complex, complex.args);
    Multidef complexInterval = ComplexTransform.complexify(interval, interval.args);
    
    assertEquivalent(intervalComplex, complexInterval);
  }

  Symbol x = sym("x");
  Symbol y = sym("y");
  Args args1 = args(x);
  Args args2 = args(x, y);
  
  @Test
  public void testComplex() {
    assertSymmetry(def(args(x), sqrt(x)));
  }

}
