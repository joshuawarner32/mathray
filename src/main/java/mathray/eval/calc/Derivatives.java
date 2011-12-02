package mathray.eval.calc;

import java.util.HashMap;
import java.util.Map;

import mathray.Call;
import mathray.Computation;
import mathray.Rational;
import mathray.Definition;
import mathray.Function;
import mathray.Generator;
import mathray.Transformer;
import mathray.Value;
import mathray.Symbol;
import mathray.Vector;
import mathray.eval.Visitor;

import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Derivatives {
  
  private static final Map<Function, Computation> derivs = new HashMap<Function, Computation>();
  
  static {
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
  
  private static void register(Function func, Computation d) {
    derivs.put(func, d);
  }
  
  private static Vector<Value> paralellScalarMul(Vector<Value> values, final Value value) {
    return values.transform(new Transformer<Value, Value>() {
      @Override
      public Value transform(Value in) {
        return mul(in, value);
      }
    });
  }

  public static Definition derive(Definition def, final Symbol diffVar) {
    final Visitor<Value> deriver = new Visitor<Value>() {
      @Override
      public Value call(final Call call) {
        final Vector<Value> args = call.visitArgs(this);
        final Computation comp = derivs.get(call.func);
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
    
    return new Definition(def.args, def.value.accept(deriver));
    
    /*return new Definition(def.args, def.values.transform(new Transformer<Value, Value>() {
      @Override
      public Value transform(Value in) {
        return in.accept(deriver);
      }
    }));*/
  }
}
