package mathray;

import mathray.eval.Impl;
import mathray.eval.text.DefaultPrinter;
import mathray.util.Vector;
import mathray.visitor.Processor;
import static mathray.Expressions.*;

public class Definition extends Closure<Value> implements Impl<Value> {
  
  public Definition(Args args, Value value) {
    super(args, value);
  }

  public final Value call(final Vector<Value> a) {
    final Processor<Value> v = new Processor<Value>() {
      @Override
      public Value process(Call call, Vector<Value> args) {
        return call.func.call(args);
      }
      @Override
      public Value process(Symbol sym) {
        int index = args.indexOf(sym);
        if(index != -1) {
          return a.get(index);
        }
        return sym;
      }
      @Override
      public Value process(Rational cst) {
        return cst;
      }
    };
    return value.accept(v);
  }
  
  public final Value call(Value... values) {
    return call(vector(values));
  }
  
  @Override
  public String toString() {
    return DefaultPrinter.toString(this);
  }
  
  @Override
  public int hashCode() {
    return args.hashCode() + value.hashCode();
  }
  
  @Override
  public boolean equals(Object other) {
    if(!(other instanceof Definition)) {
      return false;
    }
    Definition def = (Definition)other;
    if(args.size() != def.args.size()) {
      return false;
    }
    if(args.equals(def.args)) {
      return value.equals(def.value);
    }
    // TODO: argument invariance
    return value.equals(def.value);
  }

  public static Definition identity(int count) {
    Args args = args(1);
    return new Definition(args, args.get(0));
  }

  public Multidef toMultidef() {
    return new Multidef(args, struct(value));
  }

}
