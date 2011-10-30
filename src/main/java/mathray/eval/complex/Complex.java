package mathray.eval.complex;

import mathray.Args;
import mathray.Definition;
import mathray.Function;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Environment;

import static mathray.Expressions.*;
import static mathray.Functions.*;

public final class Complex {
  
  private Complex() {}

  private static final Variable x = var("x");
  private static final Variable xr = var("xr");
  private static final Variable xi = var("xi");
  private static final Variable yr = var("yr");
  private static final Variable yi = var("yi");
  
  private static final Value DIV_DENOM = add(pow(yr, num(2)), pow(yi, num(2)));
  
  private static final Environment<Value> env = Environment.<Value>builder()
    .register(ADD, def(args(xr, xi, yr, yi), add(xr, yr), add(xi, yi)))
    .register(ADD, def(args(xr, xi, yr, yi), sub(xr, yr), sub(xi, yi)))
    .register(MUL, def(args(xr, xi, yr, yi), sub(mul(xr, yr), mul(xi, yi)), add(mul(xr, yi), mul(xi, yr))))
    .register(DIV, def(args(xr, xi, yr, yi), div(add(mul(xr, yr), mul(xi, yi)), DIV_DENOM), div(sub(mul(xi, yr), mul(xr, yi)), DIV_DENOM)))
    .register(SIN, def(args(xr, xi), mul(cosh(xi), sin(xr)), mul(sinh(xi), cos(xr))))
    .build();
  
  public static Definition complexify(Definition def, Args args, Vector<Vector<Variable>> replacements) {
    return Splitter.split(def, env, def(args(x), x, num(0)), args, replacements);
  }

}
