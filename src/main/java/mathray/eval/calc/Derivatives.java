package mathray.eval.calc;

import mathray.Call;
import mathray.Computation;
import mathray.FunctionRegistrar;
import mathray.Rational;
import mathray.Definition;
import mathray.Value;
import mathray.Symbol;
import mathray.util.Generator;
import mathray.util.Vector;
import mathray.visitor.EvaluatingVisitor;

import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Derivatives extends FunctionRegistrar<Computation> {
  
  {
    Symbol x = sym("x");
    Symbol y = sym("y");
    register(ADD, compute(args(x, y), num(1), num(1)));
    register(SUB, compute(args(x, y), num(1), num(-1)));
    register(MUL, compute(args(x, y), y, x));
    register(DIV, compute(args(x, y), div(num(1), y), div(neg(x), pow(y, num(2)))));

    register(POW, compute(args(x, y), mul(y, pow(x, sub(y, num(1)))), mul(log(x), pow(x, y))));
    
    register(NEG, compute(args(x), num(-1)));
    
    register(SIN, compute(args(x), neg(cos(x))));
    register(COS, compute(args(x), sin(x)));
    register(SQRT, compute(args(x), div(num(1), mul(num(2), sqrt(x)))));
  }
  
  public static final Derivatives DEFAULT = new Derivatives();
  
  public static Definition derive(Definition def, Symbol diffVar) {
    return DEFAULT.transform(def.toComputation(), diffVar).get(0);
  }
  
  public Computation transform(Computation comp, final Symbol diffVar) {
    final EvaluatingVisitor<Value> deriver = new EvaluatingVisitor<Value>() {
      @Override
      public Value call(final Call call, final Vector<Value> args) {
        final Computation comp = lookup(call.func);
        final Vector<Value> vals = comp.call(call.args);
        return add(Vector.generate(comp.values.size(), new Generator<Value>() {
          @Override
          public Value generate(int index) {
            return mul(vals.get(index), args.get(index));
          }
        }));
      }
      
      @Override
      public Value constant(Rational cst) {
        return num(0);
      }
      
      @Override
      public Value symbol(Symbol var) {
        return var == diffVar ? num(1) : num(0);
      }
    };
    
    return comp.transform(deriver);
  }
}
