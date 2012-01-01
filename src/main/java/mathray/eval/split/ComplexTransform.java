package mathray.eval.split;

import mathray.Args;
import mathray.Computation;
import mathray.FunctionRegistrar;
import mathray.Value;
import mathray.Symbol;
import mathray.util.Vector;

import static mathray.Expressions.*;
import static mathray.Functions.*;

public final class ComplexTransform extends FunctionRegistrar<Computation> {

  private static final Symbol x = sym("x");
  private static final Symbol xr = sym("xr");
  private static final Symbol xi = sym("xi");
  private static final Symbol yr = sym("yr");
  private static final Symbol yi = sym("yi");
  
  private static final Value DIV_DENOM = add(pow(yr, num(2)), pow(yi, num(2)));
  
  {
    register(ADD, compute(args(xr, xi, yr, yi), add(xr, yr), add(xi, yi)));
    register(SUB, compute(args(xr, xi, yr, yi), sub(xr, yr), sub(xi, yi)));
    register(MUL, compute(args(xr, xi, yr, yi), sub(mul(xr, yr), mul(xi, yi)), add(mul(xr, yi), mul(xi, yr))));
    register(DIV, compute(args(xr, xi, yr, yi), div(add(mul(xr, yr), mul(xi, yi)), DIV_DENOM), div(sub(mul(xi, yr), mul(xr, yi)), DIV_DENOM)));
    register(NEG, compute(args(xr, xi), neg(xr), neg(xi)));
    register(SIN, compute(args(xr, xi), mul(cosh(xi), sin(xr)), mul(sinh(xi), cos(xr))));
    register(COS, compute(args(xr, xi), mul(cosh(xi), cos(xr)), neg(mul(sinh(xi), sin(xr)))));
  }
  
  public Computation complexify(Computation comp, Args args) {
    return Splitter.split(comp, this, compute(args(x), x, num(0)), args, new SymbolSplitter() {
      @Override
      public Vector<Symbol> split(Symbol symbol) {
        return vector(sym(symbol.name + "_r"), sym(symbol.name + "_i"));
      }
    });
  }

}
