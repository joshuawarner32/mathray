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
import mathray.eval.Visitor;
import mathray.eval.Impl;
import static mathray.Expressions.*;

public class IntervalVisitor implements Visitor<Interval> {
  
  private Map<Function, Impl<Interval>> impls = new HashMap<Function, Impl<Interval>>();
  
  {
    register(ADD, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        return vector(new Interval(add(r.a, o.a), add(r.b, o.b)));
      }
    });
    
    register(SUB, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        return vector(new Interval(sub(r.a, o.b), sub(r.b, o.a)));
      }
    });
    
    register(MUL, new Impl<Interval>() {
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
    });
    
    register(DIV, new Impl<Interval>() {
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
        return vector(ret);
      }
    });
    
    register(SIN, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        throw new RuntimeException("not implemented");
      }
    });

    register(SQRT, new Impl<Interval>() {
      public Vector<Interval> call(Vector<Interval> args) {
        Interval r = args.get(0);
        return vector(new Interval(sqrt(r.a), sqrt(r.b)));
      }
    });
  }
  
  public void register(Function func, Impl<Interval> impl) {
    impls.put(func, impl);
  }

  private Impl<Interval> implement(Function func) {
    Impl<Interval> ret = impls.get(func);
    if(ret == null) {
      throw new RuntimeException("couldn't find impl");
    }
    return ret;
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
