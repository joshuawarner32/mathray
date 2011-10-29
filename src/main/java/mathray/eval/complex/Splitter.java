package mathray.eval.complex;

import mathray.Args;
import mathray.Definition;
import mathray.Function;
import mathray.Rational;
import mathray.Transformer;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Context;
import mathray.eval.Environment;
import mathray.eval.Impl;
import mathray.eval.Implementer;
import mathray.eval.Translator;
import static mathray.Expressions.*;

public class Splitter {
  
  public static Definition split(Definition def, Environment<Value> env, final Definition splitter, final Args args, final Vector<Vector<Variable>> replacements) {
    if(!args.isSubsetOf(def.args)) {
      throw new IllegalArgumentException();
    }
    int newSize = splitter.values.size();
    for(Vector<Variable> repl : replacements) {
      if(newSize != repl.size()) {
        throw new IllegalArgumentException();
      }
    }
    Variable[] nargsarr = new Variable[args.size() * (newSize - 1) + def.args.size()];
    int i = 0;
    for(Variable var : def.args) {
      if(args.contains(var)) {
        Vector<Variable> reps = replacements.get(args.getIndex(var));
        for(Variable v : reps) {
          nargsarr[i++] = v;
        }
      } else {
        nargsarr[i++] = var;
      }
    }
    Vector<Vector<Value>> bindings = def.args.toVector().transform(new Transformer<Variable, Vector<Value>>() {
      @Override
      public Vector<Value> transform(Variable in) {
        if(args.contains(in)) {
          return (Vector) replacements.get(args.getIndex(in));
        } else {
          return splitter.call(vector((Value)in));
        }
      }
    });
    Vector<Vector<Value>> res = new Context<Vector<Value>>(args.bind(bindings), new Implementer<Vector<Value>>() {
      @Override
      public Impl<Vector<Value>> implement(Function func) {
        return new Impl<Vector<Value>>() {
          @Override
          public Vector<Vector<Value>> call(Vector<Vector<Value>> args) {
            // TODO
            return null;
          }
        };
      }
    }, new Translator<Vector<Value>>() {
      @Override
      public Vector<Value> translate(Rational r) {
        return splitter.call(vector((Value)r));
      }
    }).run(def.values);
    Value[] ret = new Value[newSize * def.values.size()];
    i = 0;
    for(Vector<Value> vec : res) {
      for(Value v : vec) {
        ret[i++] = v;
      }
    }
    return new Definition(args(nargsarr), vector(ret));
  }
}
