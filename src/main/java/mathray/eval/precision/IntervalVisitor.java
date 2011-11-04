package mathray.eval.precision;

import static mathray.Functions.*;

import java.util.HashMap;
import java.util.Map;

import mathray.Call;
import mathray.Rational;
import mathray.Function;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Environment;
import mathray.eval.Visitor;
import mathray.eval.Impl;
import static mathray.Expressions.*;
import static mathray.NamedConstants.*;

public class IntervalVisitor implements Visitor<Interval> {
  
  Environment<Interval> env = Environment.<Interval>builder() 
    .register(ADD, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        return vector(new Interval(add(r.a, o.a), add(r.b, o.b)));
      }
    })
    .register(SUB, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        return vector(new Interval(sub(r.a, o.b), sub(r.b, o.a)));
      }
    })
    .register(MUL, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        Value ac = mul(r.a, o.a);
        Value ad = mul(r.a, o.b);
        Value bc = mul(r.b, o.a);
        Value bd = mul(r.b, o.b);
        Call m1 = minMax(ac, ad);
        Call m2 = minMax(bc, bd);
        Interval ret = new Interval(
            min(m1.select(0), m2.select(0)),
            max(m1.select(1), m2.select(1)));
        return vector(ret);
      }
    })
    .register(DIV, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        Value ac = div(r.a, o.a);
        Value ad = div(r.a, o.b);
        Value bc = div(r.b, o.a);
        Value bd = div(r.b, o.b);
        Call m1 = minMax(ac, ad);
        Call m2 = minMax(bc, bd);
        Interval ret = new Interval(
            min(m1.select(0), m2.select(0)),
            max(m1.select(1), m2.select(1)));
        return vector(intervalSelectContains(args.get(1), Interval.INFINITE, ret));
      }
    })
    .register(SIN, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        Interval x = args.get(0);
        Value diff = sub(x.b, x.a);
        Interval full = new Interval(num(-1), num(1));
        Value va = sin(x.a);
        Value vb = sin(x.b);
        Value da = cos(x.a);
        Value db = cos(x.b);
        Interval els = new Interval(num(-1), max(va, vb));
        Interval dbltz = intervalSelectSign(db, new Interval(min(va, vb), num(1)), els, els);
        Call minMax = minMax(va, vb);
        Interval els2 = new Interval(minMax.select(0), minMax.select(1));
        Interval fallback = intervalSelectSign(mul(da, db), dbltz, els2, els2);
        return vector(intervalSelectSign(sub(diff, mul(num(2), PI)), full, full, fallback));
      }
    })
    .register(MIN_MAX, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        Call a = minMax(args.get(0).a, args.get(1).a);
        Call b = minMax(args.get(0).b, args.get(1).b);
        return vector(new Interval(a.select(0), b.select(0)), new Interval(a.select(1), b.select(1)));
      }
    })
    .register(ABS, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        Value aa = abs(args.get(0).a);
        Value ab = abs(args.get(0).b);
        Call minMax = minMax(aa, ab);
        return vector(new Interval(minMax.select(0), minMax.select(1)));
      }
    })
    .register(SQRT, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        Interval r = args.get(0);
        return vector(new Interval(sqrt(r.a), sqrt(r.b)));
      }
    })
    .build();
  
  private static Value selectContains(Interval test, Value t, Value f) {
    return selectSign(mul(test.a, test.b), t, t, f);
  }
  
  private static Interval intervalSelectContains(Interval test, Interval t, Interval f) {
    return new Interval(selectContains(test, t.a, f.a), selectContains(test, t.b, f.b));
  }
  
  private static Interval intervalSelectSign(Value test, Interval n, Interval z, Interval p) {
    return new Interval(selectSign(test, n.a, z.a, p.a), selectSign(test, n.b, z.b, p.b));
  }

  private Impl<Interval> implement(Function func) {
    return env.implement(func);
  }
  
  @Override
  public Vector<Interval> call(Visitor<Interval> v, Call call) {
    return implement(call.func).call(call.visitArgs(v));
  }
  
  @Override
  public Interval variable(Variable var) {
    return new Interval(var, var);
  }

  @Override
  public Interval constant(Rational cst) {
    return new Interval(cst, cst);
  }

}
