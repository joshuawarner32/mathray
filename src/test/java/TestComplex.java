
import static mathray.Expressions.*;
import org.junit.Test;

import mathray.Args;
import mathray.Definition;
import mathray.Symbol;
import mathray.util.Vector;

public class TestComplex {
  
  private static void assertComplexifiesTo(Definition def, Args args, Vector<Vector<Symbol>> replacements, Definition result) {
    //assertEquals(Complex.complexify(def, args, replacements), result);
  }
  
  @Test
  public void testComplex() {
    Symbol x = sym("x");
    Symbol y = sym("y");
    Symbol xr = sym("xr");
    Symbol xi = sym("xi");
    Symbol yr = sym("yr");
    Symbol yi = sym("yi");
    Args args2 = args(x, y);
    
    // I f***ing HATE java generics.  Compiler type erasure is the worst god-damned decision in language design history.
    //assertComplexifiesTo(def(args2, add(x, y)), args2, vector(vector(xr, xi), vector(yr, yi)), def(args(xr, xi, yr, yi), add(xr, yr), add(xi, yi)));
  }

}
