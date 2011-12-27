package mathray;

import mathray.eval.Impl;
import mathray.eval.text.DefaultPrinter;
import mathray.util.Vector;
import mathray.visitor.SimpleVisitor;
import static mathray.Expressions.*;

public class Definition implements Impl<Value> {
  
  public final Args args;
  
  public final Value value;
  
  public Definition(Args args, Value value) {
    this.args = args;
    this.value = value;
  }

  public final Value call(final Vector<Value> a) {
    final SimpleVisitor<Value> v = new SimpleVisitor<Value>() {
      @Override
      public Value call(Call call) {
        return call.func.call(call.visitArgs(this));
      }
      @Override
      public Value symbol(Symbol sym) {
        Integer index = args.getIndex(sym);
        if(index != null) {
          return a.get(index);
        }
        return sym;
      }
      @Override
      public Value constant(Rational cst) {
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

  public String toJavaString() {
    StringBuilder b = new StringBuilder();
    b.append("def(");
    b.append(args.toJavaString());
    b.append(", ");
    b.append(value.toJavaString());
    b.append(")");
    return b.toString();
  }

  public Computation toComputation() {
    return new Computation(args, vector(value));
  }

}
