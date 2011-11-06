package mathray.eval.precision;

import mathray.Args;
import mathray.Definition;
import mathray.Expressions;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Visitor;
import mathray.eval.Visitors;

public class Intervals {
  
  private Intervals() {}
  
  public static Definition intervalize(Definition def, Vector<Variable> vars) {
    for(Variable var : vars) {
      if(!def.args.contains(var)) {
        throw new IllegalArgumentException();
      }
    }
    Variable[] nargsarr = new Variable[vars.size() + def.args.size()];
    Interval[] bindings = new Interval[def.args.size()];
    int i = 0;
    int j = 0;
    for(Variable v : def.args) {
      if(vars.contains(v)) {
        Variable vl = new Variable(v.name + "_a");
        Variable vh = new Variable(v.name + "_b");
        bindings[i++] = new Interval(vl, vh);
        nargsarr[j++] = vl;
        nargsarr[j++] = vh;
      } else {
        bindings[i++] = new Interval(v, v);
        nargsarr[j++] = v;
      }
    }
    Args nargs = Expressions.args(nargsarr);
    final Visitor<Interval> v = Visitors.bind(new IntervalVisitor(), def.args, Expressions.vector(bindings));
    Value[] values = new Value[def.values.size() * 2];
    for(i = 0; i < def.values.size(); i++) {
      Interval iv = def.values.get(i).accept(v);
      values[i * 2] = iv.a;
      values[i * 2 + 1] = iv.b;
    }
    return new Definition(nargs, Expressions.vector(values));
  }
  
  public Definition precisionGuarantee(Definition def, Vector<Variable> vars) {
    return def;
  }

}
