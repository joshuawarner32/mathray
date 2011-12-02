package mathray.eval.complex;

import mathray.Args;
import mathray.Computation;
import mathray.Definition;
import mathray.Function;
import mathray.Rational;
import mathray.Transformer;
import mathray.Value;
import mathray.Symbol;
import mathray.Vector;
import mathray.eval.ComputeData;
import mathray.eval.Context;
import mathray.eval.Environment;
import mathray.eval.Impl;
import mathray.eval.Implementer;
import mathray.eval.Translator;
import static mathray.Expressions.*;

public class Splitter {
  
  public static Computation split(Computation comp, final ComputeData env, final Computation splitter, final Args args, final Vector<Vector<Symbol>> replacements) {
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
    Vector<Vector<Value>> bindings = comp.args.toVector().transform(new Transformer<Symbol, Vector<Value>>() {
      @Override
      public Vector<Value> transform(Symbol in) {
        if(args.contains(in)) {
          return (Vector) replacements.get(args.getIndex(in));
        } else {
          return splitter.call(vector((Value)in));
        }
      }
    });
    Vector<Vector<Value>> res = new Context<Vector<Value>>(args.bind(bindings), new Implementer<Vector<Value>>() {
      @Override
      public Impl<Vector<Value>> implement(final Function func) {
        return new Impl<Vector<Value>>() {
          @Override
          public Vector<Value> call(Vector<Vector<Value>> args) {
            return null; //Vector.group(env.implement(func).call(Vector.flatten(args)), newSize);
          }
        };
      }
    }, new Translator<Vector<Value>>() {
      @Override
      public Vector<Value> translate(Rational r) {
        return splitter.call(vector((Value)r));
      }
    }).run(comp.values);
    Value[] ret = new Value[newSize * comp.values.size()];
    i = 0;
    for(Vector<Value> vec : res) {
      for(Value v : vec) {
        ret[i++] = v;
      }
    }
    return new Computation(args(nargsarr), vector(ret));
  }
}
