package mathray.eval.calc;

import mathray.Call;
import mathray.Multidef;
import mathray.FunctionRegistrar;
import mathray.Rational;
import mathray.Definition;
import mathray.Value;
import mathray.Symbol;
import mathray.util.Vector;
import mathray.visitor.EvaluatingVisitor;

import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Derivatives extends FunctionRegistrar<Multidef> {
  
  {
    Symbol x = sym("x");
    Symbol y = sym("y");
    register(ADD, multidef(args(x, y), num(1), num(1)));
    register(SUB, multidef(args(x, y), num(1), num(-1)));
    register(MUL, multidef(args(x, y), y, x));
    register(DIV, multidef(args(x, y), div(num(1), y), div(neg(x), pow(y, num(2)))));

    register(POW, multidef(args(x, y), mul(y, pow(x, sub(y, num(1)))), mul(log(x), pow(x, y))));
    
    register(NEG, multidef(args(x), num(-1)));
    
    register(SIN, multidef(args(x), neg(cos(x))));
    register(COS, multidef(args(x), sin(x)));
    register(SQRT, multidef(args(x), div(num(1), mul(num(2), sqrt(x)))));
  }
  
  public static final Derivatives DEFAULT = new Derivatives();
  
  public static Definition derive(Definition def, Symbol diffVar) {
    return DEFAULT.transform(def.toMultidef(), diffVar).get(0);
  }
  
  public Multidef transform(Multidef def, final Symbol diffVar) {
    final EvaluatingVisitor<Value> deriver = new EvaluatingVisitor<Value>() {
      @Override
      public Value call(final Call call, final Vector<Value> args) {
        return add(zip(MUL.define(), lookup(call.func).call(call.args), args));
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
    
    return def.transform(deriver);
  }
}
