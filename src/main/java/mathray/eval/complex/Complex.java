package mathray.eval.complex;

import mathray.Args;
import mathray.Computation;
import mathray.Definition;
import mathray.Value;
import mathray.Symbol;
import mathray.Vector;
import mathray.eval.ComputeData;
import mathray.eval.Environment;

import static mathray.Expressions.*;
import static mathray.Functions.*;

public final class Complex {
  
  private Complex() {}

  private static final Symbol x = sym("x");
  private static final Symbol xr = sym("xr");
  private static final Symbol xi = sym("xi");
  private static final Symbol yr = sym("yr");
  private static final Symbol yi = sym("yi");
  
  private static final Value DIV_DENOM = add(pow(yr, num(2)), pow(yi, num(2)));
  
  private static final ComputeData env = ComputeData.builder()
    .register(ADD, compute(args(xr, xi, yr, yi), add(xr, yr), add(xi, yi)))
    .register(SUB, compute(args(xr, xi, yr, yi), sub(xr, yr), sub(xi, yi)))
    .register(MUL, compute(args(xr, xi, yr, yi), sub(mul(xr, yr), mul(xi, yi)), add(mul(xr, yi), mul(xi, yr))))
    .register(DIV, compute(args(xr, xi, yr, yi), div(add(mul(xr, yr), mul(xi, yi)), DIV_DENOM), div(sub(mul(xi, yr), mul(xr, yi)), DIV_DENOM)))
    .register(NEG, compute(args(xr, xi), neg(xr), neg(xi)))
    .register(SIN, compute(args(xr, xi), mul(cosh(xi), sin(xr)), mul(sinh(xi), cos(xr))))
    .register(COS, compute(args(xr, xi), mul(cosh(xi), cos(xr)), neg(mul(sinh(xi), sin(xr)))))
    .build();
  
  public static Computation complexify(Computation comp, Args args, Vector<Vector<Symbol>> replacements) {
    return Splitter.split(comp, env, compute(args(x), x, num(0)), args, replacements);
  }

}
