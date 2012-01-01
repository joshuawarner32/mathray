package mathray.eval.split;

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
  
  public static Computation split(Computation comp, final FunctionRegistrar<Computation> env, final Computation splitter, final Args args, SymbolSplitter replacements) {
    if(!args.isSubsetOf(comp.args)) {
      throw new IllegalArgumentException();
    }
    final int newSize = splitter.values.size();
    Symbol[] nargsarr = new Symbol[args.size() * (newSize - 1) + comp.args.size()];
    final Vector<Value>[] bindings = new Vector[comp.args.size()];
    int i = 0;
    int j = 0;
    for(Symbol var : comp.args) {
      if(args.contains(var)) {
        Vector<Symbol> reps = replacements.split(var);
        for(Symbol v : reps) {
          nargsarr[i++] = v;
        }
        bindings[j++] = (Vector)reps;
      } else {
        nargsarr[i++] = var;
        bindings[j++] = splitter.call(vector((Value)var));
      }
    }
    EvaluatingVisitor<Vector<Value>> v = new EvaluatingVisitor<Vector<Value>>() {
      @Override
      public Vector<Value> symbol(Symbol sym) {
        return bindings[args.getIndex(sym)];
      }
      
      @Override
      public Vector<Value> constant(Rational rat) {
        return splitter.call(vector((Value)rat));
      }
      
      @Override
      public Vector<Value> call(Call call, Vector<Vector<Value>> args) {
        return env.lookup(call.func).call(Vector.flatten(args));
      }
    };
    Vector<Vector<Value>> ret = comp.accept(v);
    return new Computation(args(nargsarr), Vector.flatten(ret));
  }
}
