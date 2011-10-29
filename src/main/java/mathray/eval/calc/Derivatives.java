package mathray.eval.calc;

import java.util.HashMap;
import java.util.Map;

import mathray.Call;
import mathray.Rational;
import mathray.Definition;
import mathray.Function;
import mathray.Generator;
import mathray.Transformer;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Visitor;

import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Derivatives {
  
  private static final Map<Function, Vector<Definition>> derivs = new HashMap<Function, Vector<Definition>>();
  
  static {
    Variable x = var("x");
    Variable y = var("y");
    register(ADD, def(args(x, y), num(1)), def(args(x, y), num(1)));
    register(SUB, def(args(x, y), num(1)), def(args(x, y), num(-1)));
    register(MUL, def(args(x, y), y), def(args(x, y), x));
    register(DIV, def(args(x, y), div(num(1), y)), def(args(x, y), div(num(1), pow(x, num(2)))));

    register(POW, def(args(x, y), mul(y, pow(x, sub(y, num(1))))), def(args(x, y), mul(ln(x), pow(x, y))));
    
    register(NEG, def(args(x), num(-1)));
    
    register(SIN, def(args(x), neg(cos(x))));
    register(COS, def(args(x), sin(x)));
    register(SQRT, def(args(x), div(num(1), mul(num(2), sqrt(x)))));
  }
  
  private static void register(Function func, Definition... derivDefs) {
    derivs.put(func, vector(derivDefs));
  }
  
  private static Vector<Value> paralellSum(Vector<Vector<Value>> values) {
    Value[] ret = null;
    for(Vector<Value> vals : values) {
      if(ret == null) {
        ret = new Value[vals.size()];
      }
      if(vals.size() != ret.length) {
        throw new IllegalArgumentException();
      }
      for(int i = 0; i < ret.length; i++) {
        if(ret[i] == null) {
          ret[i] = vals.get(i);
        } else {
          ret[i] = add(ret[i], vals.get(i));
        }
      }
    }
    return vector(ret);
  }
  
  private static Vector<Value> paralellScalarMul(Vector<Value> values, final Value value) {
    return values.transform(new Transformer<Value, Value>() {
      @Override
      public Value transform(Value in) {
        return mul(in, value);
      }
    });
  }

  public static Definition derive(Definition def, final Variable diffVar) {
    final Visitor<Value> deriver = new Visitor<Value>() {
      @Override
      public Vector<Value> call(Visitor<Value> v, final Call call) {
        final Vector<Value> args = call.visitArgs(v);
        final Vector<Definition> der = derivs.get(call.func);
        return paralellSum(Vector.generate(call.args.size(), new Generator<Vector<Value>>() {
          @Override
          public Vector<Value> generate(int index) {
            return paralellScalarMul(der.get(index).call(call.args), args.get(index));
          }
        }));
      }
      
      @Override
      public Value constant(Rational cst) {
        return num(0);
      }
      
      @Override
      public Value variable(Variable var) {
        return var == diffVar ? num(1) : num(0);
      }
    };
    
    return new Definition(def.args, def.values.transform(new Transformer<Value, Value>() {
      @Override
      public Value transform(Value in) {
        return in.accept(deriver);
      }
    }));
  }
}
