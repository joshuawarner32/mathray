package mathray.eval.precision;

import static mathray.Expressions.*;
import static mathray.Functions.*;
import static mathray.NamedConstants.*;
import mathray.Args;
import mathray.Call;
import mathray.Computation;
import mathray.Definition;
import mathray.Expressions;
import mathray.Rational;
import mathray.Value;
import mathray.Symbol;
import mathray.eval.Environment;
import mathray.eval.Impl;
import mathray.util.Vector;
import mathray.visitor.EvaluatingVisitor;

public class Intervals {
  
  private static boolean useApprox = false;
  
  private static Value maybeUp(Value a) {
    if(useApprox) {
      return up(a);
    } else {
      return a;
    }
  }
  
  private static Interval approx(Value a, Value b) {
    if(useApprox) {
      return Interval.approx(a, b);
    } else {
      return Interval.exact(a, b);
    }
  }

  private static Environment<Interval> env = Environment.<Interval>builder() 
    .register(ADD, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        return approx(add(r.a, o.a), add(r.b, o.b));
      }
    })
    .register(SUB, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        return approx(sub(r.a, o.b), sub(r.b, o.a));
      }
    })
    .register(MUL, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        Value ac = mul(r.a, o.a);
        Value ad = mul(r.a, o.b);
        Value bc = mul(r.b, o.a);
        Value bd = mul(r.b, o.b);
        Value m1Min = min(ac, ad);
        Value m1Max = max(ac, ad);
        Value m2Min = min(bc, bd);
        Value m2Max = max(bc, bd);
        Interval ret = Interval.exact(
            min(m1Min, m2Min),
            max(m1Max, m2Max));
        return ret;
      }
    })
    .register(DIV, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        Value ac = div(r.a, o.a);
        Value ad = div(r.a, o.b);
        Value bc = div(r.b, o.a);
        Value bd = div(r.b, o.b);
        Value m1Min = min(ac, ad);
        Value m1Max = max(ac, ad);
        Value m2Min = min(bc, bd);
        Value m2Max = max(bc, bd);
        Interval ret = Interval.exact(
            min(m1Min, m2Min),
            max(m1Max, m2Max));
        return intervalSelectContains(args.get(1), Interval.INFINITE, ret);
      }
    })
    .register(SIN, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval x = args.get(0);
        Value diff = sub(x.b, x.a);
        Interval full = Interval.exact(num(-1), num(1));
        Value va = sin(x.a);
        Value vb = sin(x.b);
        Value da = cos(x.a);
        Value db = cos(x.b);
        Interval dbltz = intervalSelectSign(db, Interval.exact(min(va, vb), num(1)), Interval.exact(num(-1), max(va, vb)));
        Value min = min(va, vb);
        Value max = max(va, vb);
        Interval els = intervalSelectSign(sub(diff, div(TAU, num(2))), Interval.exact(min, max), full);
        Interval fallback = intervalSelectSign(mul(da, db), dbltz, els);
        return intervalSelectSign(sub(diff, TAU), fallback, full);
      }
    })
    .register(MIN, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval a = args.get(0);
        Interval b = args.get(1);
        return Interval.exact(max(a.a, b.a), max(a.b, b.b));
      }
    })
    .register(MAX, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval a = args.get(0);
        Interval b = args.get(1);
        return Interval.exact(min(a.a, b.a), min(a.b, b.b));
      }
    })
    .register(ABS, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Value aa = abs(args.get(0).a);
        Value ab = abs(args.get(0).b);
        return Interval.exact(min(aa, ab), max(aa, ab));
      }
    })
    .register(SQRT, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval r = args.get(0);
        return approx(sqrt(r.a), sqrt(r.b));
      }
    })
    .register(POW, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval a = args.get(0);
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
        }
        throw new RuntimeException("not implemented");
      }
    })
    .build();
  
  private static Value selectContains(Interval test, Value t, Value f) {
    return selectSign(neg(mul(test.a, test.b)), f, t);
  }
  
  private static Interval intervalSelectContains(Interval test, Interval t, Interval f) {
    return Interval.exact(selectContains(test, t.a, f.a), selectContains(test, t.b, f.b));
  }
  
  private static Interval intervalSelectSign(Value test, Interval n, Interval zp) {
    return Interval.exact(selectSign(test, n.a, zp.a), selectSign(test, n.b, zp.b));
  }
  
  private Intervals() {}
  
  public static Computation intervalize(final Computation comp, Vector<Symbol> vars) {
    for(Symbol var : vars) {
      if(!comp.args.contains(var)) {
        throw new IllegalArgumentException();
      }
    }
    Symbol[] nargsarr = new Symbol[vars.size() + comp.args.size()];
    final Interval[] bindings = new Interval[comp.args.size()];
    int i = 0;
    int j = 0;
    for(Symbol v : comp.args) {
      if(vars.contains(v)) {
        Symbol vl = new Symbol(v.name + "_a");
        Symbol vh = new Symbol(v.name + "_b");
        bindings[i++] = Interval.exact(vl, vh);
        nargsarr[j++] = vl;
        nargsarr[j++] = vh;
      } else {
        bindings[i++] = approx(v, v);
        nargsarr[j++] = v;
      }
    }
    Args nargs = Expressions.args(nargsarr);
    final EvaluatingVisitor<Interval> v = new EvaluatingVisitor<Interval>() {

      @Override
      public Interval call(Call call, Vector<Interval> args) {
        return env.implement(call.func).call(args);
      }
      
      @Override
      public Interval symbol(Symbol sym) {
        Integer index = comp.args.getIndex(sym);
        if(index != null) {
          return bindings[index];
        }
        return approx(sym, sym);
      }

      @Override
      public Interval constant(Rational cst) {
        return Interval.exact(cst, cst);
      }

    };
    Value[] values = new Value[2];
    for(i = 0; i < comp.values.size(); i++) {
      Interval iv = comp.values.get(i).accept(v);
      values[i * 2] = iv.a;
      values[i * 2 + 1] = iv.b;
    }
    return new Computation(nargs, vector(values));
  }
  
  public Definition precisionGuarantee(Definition def, Vector<Symbol> vars) {
    return def;
  }

}
