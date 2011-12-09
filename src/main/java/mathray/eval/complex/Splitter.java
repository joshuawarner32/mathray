package mathray.eval.complex;

import mathray.Args;
import mathray.Call;
import mathray.Computation;
import mathray.FunctionRegistrar;
import mathray.Rational;
import mathray.Transformer;
import mathray.Value;
import mathray.Symbol;
import mathray.Vector;
import mathray.eval.Visitor;
import static mathray.Expressions.*;

public class Splitter {
  
  public static Computation split(Computation comp, final FunctionRegistrar<Computation> env, final Computation splitter, final Args args, final Vector<Vector<Symbol>> replacements) {
    if(!args.isSubsetOf(comp.args)) {
      throw new IllegalArgumentException();
    }
    final int newSize = splitter.values.size();
    for(Vector<Symbol> repl : replacements) {
      if(newSize != repl.size()) {
        throw new IllegalArgumentException();
      }
    }
    Symbol[] nargsarr = new Symbol[args.size() * (newSize - 1) + comp.args.size()];
    int i = 0;
    for(Symbol var : comp.args) {
      if(args.contains(var)) {
        Vector<Symbol> reps = replacements.get(args.getIndex(var));
        for(Symbol v : reps) {
          nargsarr[i++] = v;
        }
      } else {
        nargsarr[i++] = var;
      }
    }
    final Vector<Vector<Value>> bindings = comp.args.toVector().transform(new Transformer<Symbol, Vector<Value>>() {
      @SuppressWarnings({ "unchecked", "rawtypes" })
      @Override
      public Vector<Value> transform(Symbol in) {
        if(args.contains(in)) {
          return (Vector) replacements.get(args.getIndex(in));
        } else {
          return splitter.call(vector((Value)in));
        }
      }
    });
    Visitor<Vector<Value>> v = new Visitor<Vector<Value>>() {
      @Override
      public Vector<Value> symbol(Symbol sym) {
        return bindings.get(args.getIndex(sym));
      }
      
      @Override
      public Vector<Value> constant(Rational rat) {
        return splitter.call(vector((Value)rat));
      }
      
      @Override
      public Vector<Value> call(Call call) {
        return null; // TODO
      }
    };
    Value[] ret = new Value[newSize * comp.values.size()];
    i = 0;
    for(Value val : comp.values) {
      for(Value v2 : val.accept(v)) {
        ret[i++] = v2;
      }
    }
    return new Computation(args(nargsarr), vector(ret));
  }
}
