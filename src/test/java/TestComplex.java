
import static mathray.Expressions.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import mathray.Args;
import mathray.Definition;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.complex.Complex;

public class TestComplex {
  
  private static void assertComplexifiesTo(Definition def, Args args, Vector<Vector<Variable>> replacements, Definition result) {
    assertEquals(Complex.complexify(def, args, replacements), result);
  }
  
  @Test
  public void testComplex() {
    Variable x = var("x");
    Variable y = var("y");
    Variable xr = var("xr");
    Variable xi = var("xi");
    Variable yr = var("yr");
    Variable yi = var("yi");
    Args args2 = args(x, y);
    
    // I f***ing HATE java generics.  Compiler type erasure is the worst god-damned decision in language design history.
    //assertComplexifiesTo(def(args2, add(x, y)), args2, vector(vector(xr, xi), vector(yr, yi)), def(args(xr, xi, yr, yi), add(xr, yr), add(xi, yi)));
  }

}
