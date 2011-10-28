package mathray.eval.complex;

import mathray.Definition;
import mathray.Function;
import mathray.Variable;
import mathray.Vector;

import static mathray.Expressions.*;
import static mathray.Functions.*;

public final class Complex {
  
  private Complex() {}
  
  static {
    Variable xr = var("xr");
    Variable xi = var("xi");
    Variable yr = var("yr");
    Variable yi = var("yi");
    
    register(ADD, def(args(xr, xi, yr, yi), add(xr, yr), add(xi, yi)));
    register(MUL, def(args(xr, xi, yr, yi), sub(mul(xr, yr), mul(xi, yi)), add(mul(xr, yi), mul(xi, yr))));
    register(SIN, def(args(xr, xi), mul(cosh(xi), sin(xr)), mul(sinh(xi), cos(xr))));
  }
  
  private static void register(Function func, Definition def) {
    
  }
  
  public static Definition complexify(Definition def, Vector<Variable> vars) {
    return def;
  }

}
