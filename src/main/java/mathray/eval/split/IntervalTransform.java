package mathray.eval.split;

import static mathray.Expressions.*;
import static mathray.Functions.*;
import static mathray.NamedConstants.*;
import mathray.Args;
import mathray.Multidef;
import mathray.Function;
import mathray.FunctionRegistrar;
import mathray.Value;
import mathray.Symbol;
import mathray.util.Vector;

public class IntervalTransform extends FunctionRegistrar<Multidef> {

  private static final Symbol x = sym("x");
  private static final Symbol xa = sym("xa");
  private static final Symbol xb = sym("xb");
  private static final Symbol ya = sym("ya");
  private static final Symbol yb = sym("yb");
  
  private static final Args args1 = args(xa, xb);
  private static final Args args2 = args(xa, xb, ya, yb);
  
  private static Multidef funcMinMax(Function func) {
    Value ac = func.call(xa, ya);
    Value ad = func.call(xa, yb);
    Value bc = func.call(xb, ya);
    Value bd = func.call(xb, yb);

    Value m1Min = min(ac, ad);
    Value m1Max = max(ac, ad);
    Value m2Min = min(bc, bd);
    Value m2Max = max(bc, bd);
    
    return multidef(args2, min(m1Min, m2Min), max(m1Max, m2Max));
  }
  
  private static Multidef makeSinCompute() {
    Value diff = sub(xb, xa);
    Vector<Value> full = vector((Value)num(-1), num(1));
    Value va = sin(xa);
    Value vb = sin(xb);
    Value da = cos(xa);
    Value db = cos(xb);
    Vector<Value> dbltz = vector(intervalSelectSign(db, vector(min(va, vb), num(1)), vector(num(-1), max(va, vb))));
    Value min = min(va, vb);
    Value max = max(va, vb);
    Vector<Value> els = vector(intervalSelectSign(sub(diff, div(TAU, num(2))), vector(min, max), full));
    Vector<Value> fallback = vector(intervalSelectSign(mul(da, db), dbltz, els));
    return multidef(args1, intervalSelectSign(sub(diff, TAU), fallback, full));
  }
  
  private static Multidef makePowCompute() {
    // TODO
    /*Interval a = args.get(0);
    Interval b = args.get(1);
    if(b.a.equals(b.b)) {
      if(b.a instanceof Rational) {
        Rational r = (Rational)b.a;
        if(r.isInteger()) {
          long v = r.num;
          if(v >= 0) {
            if(v % 2 == 0) {
              Value pa = pow(a.a, r);
              Value pb = pow(a.b, r);
              Value max = max(pa, pb);
              Value min = min(pa, pb);
              return intervalSelectContains(a, Interval.exact(num(0), maybeUp(max)), approx(min, max));
            } else {
              return approx(pow(a.a, r), pow(a.b, r));
            }
          }
        }
      }
    }*/
    return null;
  }
  
  private static Multidef makeAbsCompute() {
    Value aa = abs(xa);
    Value ab = abs(xb);
    return multidef(args1, intervalSelectContains(vector((Value)xa, xb), vector(num(0), max(aa, ab)), vector(min(aa, ab), max(aa, ab))));
  }
  
  {
    
    register(ADD, multidef(args2, add(xa, ya), add(xb, yb)));
    register(SUB, multidef(args2, sub(xa, yb), sub(xb, ya)));
    register(MUL, funcMinMax(MUL));
    register(DIV, funcMinMax(DIV));
    register(SIN, makeSinCompute());
    register(MIN, multidef(args2, max(xa, ya), max(xb, yb)));
    register(MAX, multidef(args2, min(xa, ya), min(xb, yb)));
    register(ABS, makeAbsCompute());
    register(SQRT, multidef(args1, sqrt(xa), sqrt(xb)));
    register(POW, makePowCompute());
  }
  
  private static Value selectContains(Vector<Value> test, Value t, Value f) {
    return selectSign(neg(mul(test.get(0), test.get(1))), f, t);
  }
  
  private static Value[] intervalSelectContains(Vector<Value> test, Vector<Value> t, Vector<Value> f) {
    return new Value[] {selectContains(test, t.get(0), f.get(0)), selectContains(test, t.get(1), f.get(1))};
  }
  
  private static Value[] intervalSelectSign(Value test, Vector<Value> n, Vector<Value> zp) {
    return new Value[] {selectSign(test, n.get(0), zp.get(0)), selectSign(test, n.get(1), zp.get(1))};
  }
  
  private static final IntervalTransform INSTANCE = new IntervalTransform();
  
  public static Multidef intervalize(final Multidef def, Args args) {
    return Splitter.split(def, INSTANCE, multidef(args(x), x, x), args, new SymbolSplitter() {
      @Override
      public Vector<Symbol> split(Symbol symbol) {
        return vector(sym(symbol.name + "_a"), sym(symbol.name + "_b"));
      }
    });
  }

}
