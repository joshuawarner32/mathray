package mathray.eval.split;

import mathray.Args;
import mathray.Multidef;
import mathray.FunctionRegistrar;
import mathray.Value;
import mathray.Symbol;
import mathray.util.Vector;

import static mathray.Expressions.*;
import static mathray.Functions.*;

public final class ComplexTransform extends FunctionRegistrar<Multidef> {

  private static final Symbol x = sym("x");
  private static final Symbol xr = sym("xr");
  private static final Symbol xi = sym("xi");
  private static final Symbol yr = sym("yr");
  private static final Symbol yi = sym("yi");
  
  private static final Value DIV_DENOM = add(pow(yr, num(2)), pow(yi, num(2)));
  
  {
    Args args2 = args(xr, xi, yr, yi);
    Args args1 = args(xr, xi);
    
    register(ADD, multidef(args2, add(xr, yr), add(xi, yi)));
    register(SUB, multidef(args2, sub(xr, yr), sub(xi, yi)));
    register(MUL, multidef(args2, sub(mul(xr, yr), mul(xi, yi)), add(mul(xr, yi), mul(xi, yr))));
    register(DIV, multidef(args2, div(add(mul(xr, yr), mul(xi, yi)), DIV_DENOM), div(sub(mul(xi, yr), mul(xr, yi)), DIV_DENOM)));
    register(NEG, multidef(args1, neg(xr), neg(xi)));
    register(SIN, multidef(args1, mul(cosh(xi), sin(xr)), mul(sinh(xi), cos(xr))));
    register(COS, multidef(args1, mul(cosh(xi), cos(xr)), neg(mul(sinh(xi), sin(xr)))));
    register(SQRT, multidef(args1, sqrt(add(pow(xr, 2), pow(xi, 2))), num(0)));
    
    Value sqrtFirst = div(atan2(xi, xr), num(2));
    Value sqrtSecond = pow(add(pow(xi, 2), pow(xr, 2)), num(1, 4));
    register(SQRT, multidef(args1, mul(cos(sqrtFirst), sqrtSecond), mul(sin(sqrtFirst), sqrtSecond)));
  }
  
  private static final ComplexTransform INSTANCE = new ComplexTransform();
  
  public static Multidef complexify(Multidef def, Args args) {
    return INSTANCE.transform(def, args);
  }
  
  public Multidef transform(Multidef def, Args args) {
    return Splitter.split(def, this, multidef(args(x), x, num(0)), args, new SymbolSplitter() {
      @Override
      public Vector<Symbol> split(Symbol symbol) {
        return vector(sym(symbol.name + "_r"), sym(symbol.name + "_i"));
      }
    });
  }

}
