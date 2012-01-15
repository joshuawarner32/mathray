package mathray.eval.split;

import mathray.Args;
import mathray.Call;
import mathray.Multidef;
import mathray.FunctionRegistrar;
import mathray.Rational;
import mathray.Value;
import mathray.Symbol;
import mathray.util.Vector;
import mathray.visitor.EvaluatingVisitor;
import static mathray.Expressions.*;

public class Splitter {
  
  public static Multidef split(Multidef def, final FunctionRegistrar<Multidef> env, final Multidef splitter, final Args args, SymbolSplitter replacements) {
    if(!args.isSubsetOf(def.args)) {
      throw new IllegalArgumentException();
    }
    final int newSize = splitter.value.size();
    Symbol[] nargsarr = new Symbol[args.size() * (newSize - 1) + def.args.size()];
    final Vector<Value>[] bindings = new Vector[def.args.size()];
    int i = 0;
    int j = 0;
    for(Symbol var : def.args) {
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
        int index = args.indexOf(sym);
        if(index >= 0) {
          return bindings[index];
        } else {
          return vector((Value)sym, sym);
        }
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
    Vector<Vector<Value>> ret = def.accept(v);
    return new Multidef(args(nargsarr), struct(Vector.flatten(ret)));
  }
}
