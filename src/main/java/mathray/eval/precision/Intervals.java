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
import mathray.Vector;
import mathray.eval.Environment;
import mathray.eval.Impl;
import mathray.eval.Visitor;

public class Intervals {

  private static Environment<Interval> env = Environment.<Interval>builder() 
    .register(ADD, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        return new Interval(add(r.a, o.a), add(r.b, o.b));
      }
    })
    .register(SUB, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval r = args.get(0);
        Interval o = args.get(1);
        return new Interval(sub(r.a, o.b), sub(r.b, o.a));
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
        Interval ret = new Interval(
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
        Interval ret = new Interval(
            min(m1Min, m2Min),
            max(m1Max, m2Max));
        return intervalSelectContains(args.get(1), Interval.INFINITE, ret);
      }
    })
    .register(SIN, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval x = args.get(0);
        Value diff = sub(x.b, x.a);
        Interval full = new Interval(num(-1), num(1));
        Value va = sin(x.a);
        Value vb = sin(x.b);
        Value da = cos(x.a);
        Value db = cos(x.b);
        Interval els = new Interval(num(-1), max(va, vb));
        Interval dbltz = intervalSelectSign(db, new Interval(min(va, vb), num(1)), els, els);
        Value min = min(va, vb);
        Value max = max(va, vb);
        Interval els2 = new Interval(min, max);
        Interval fallback = intervalSelectSign(mul(da, db), dbltz, els2, els2);
        return intervalSelectSign(sub(diff, TAU), full, full, fallback);
      }
    })
    .register(MIN, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval a = args.get(0);
        Interval b = args.get(1);
        return new Interval(max(a.a, b.a), max(a.b, b.b));
      }
    })
    .register(MAX, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval a = args.get(0);
        Interval b = args.get(1);
        return new Interval(min(a.a, b.a), min(a.b, b.b));
      }
    })
    .register(ABS, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Value aa = abs(args.get(0).a);
        Value ab = abs(args.get(0).b);
        return new Interval(min(aa, ab), max(aa, ab));
      }
    })
    .register(SQRT, new Impl<Interval>() {
      public Interval call(Vector<Interval> args) {
        Interval r = args.get(0);
        return new Interval(sqrt(r.a), sqrt(r.b));
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
        bindings[i++] = new Interval(vl, vh);
        nargsarr[j++] = vl;
        nargsarr[j++] = vh;
      } else {
        bindings[i++] = new Interval(v, v);
        nargsarr[j++] = v;
      }
    }
    Args nargs = Expressions.args(nargsarr);
    final Visitor<Interval> v = new Visitor<Interval>() {

      @Override
      public Interval call(Call call) {
        return env.implement(call.func).call(call.visitArgs(this));
      }
      
      @Override
      public Interval symbol(Symbol sym) {
        Integer index = comp.args.getIndex(sym);
        if(index != null) {
          return bindings[index];
        }
        return new Interval(sym, sym);
      }

      @Override
      public Interval constant(Rational cst) {
        return new Interval(cst, cst);
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
