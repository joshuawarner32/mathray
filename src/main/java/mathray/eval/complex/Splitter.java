package mathray.eval.complex;

import mathray.Args;
import mathray.Call;
import mathray.Computation;
import mathray.FunctionRegistrar;
import mathray.Rational;
import mathray.Value;
import mathray.Symbol;
import mathray.util.Transformer;
import mathray.util.Vector;
import mathray.visitor.EvaluatingVisitor;
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
    EvaluatingVisitor<Vector<Value>> v = new EvaluatingVisitor<Vector<Value>>() {
      @Override
      public Vector<Value> symbol(Symbol sym) {
        return bindings.get(args.getIndex(sym));
      }
      
      @Override
      public Vector<Value> constant(Rational rat) {
        return splitter.call(vector((Value)rat));
      }
      
      @Override
      public Vector<Value> call(Call call, Vector<Vector<Value>> args) {
        return null; // TODO
      }
    };
    Vector<Vector<Value>> ret = comp.accept(v);
    return new Computation(args(nargsarr), Vector.flatten(ret));
  }
}
