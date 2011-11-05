package mathray;

import mathray.eval.Impl;
import mathray.eval.Visitor;
import mathray.eval.text.DefaultPrinter;
import static mathray.Expressions.*;

public class Definition implements Impl<Value> {
  
  public final Args args;
  
  public final Vector<Value> values;
  
  public Definition(Args args, Vector<Value> values) {
    this.args = args;
    this.values = values;
  }

  @Override
  public final Vector<Value> call(final Vector<Value> a) {
    final Visitor<Value> v = new Visitor<Value>() {
      @Override
      public Vector<Value> call(Visitor<Value> v, Call call) {
        return call.func.call(call.visitArgs(v)).selectAll();
      }
      @Override
      public Value variable(Variable var) {
        return a.get(args.getIndex(var));
      }
      @Override
      public Value constant(Rational cst) {
        return cst;
      }
    };
    return values.transform(new Transformer<Value, Value>() {
      @Override
      public Value transform(Value in) {
        return in.accept(v);
      }
    });
  }
  
  public final Vector<Value> call(Value... values) {
    return call(new Vector<Value>(values));
  }
  
  @Override
  public String toString() {
    return DefaultPrinter.toString(this);
  }
  
  @Override
  public int hashCode() {
    return args.hashCode() + values.hashCode();
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
      return values.equals(def.values);
    }
    // TODO
    return values.equals(def.values);
  }

  public static Definition identity(int count) {
    Args args = args(count);
    Vector<Value> values = args.toValueVector();
    return new Definition(args, values);
  }

  public String toJavaString() {
    StringBuilder b = new StringBuilder();
    b.append("def(");
    b.append(args.toJavaString());
    for(Value v : values) {
      b.append(", ");
      b.append(v.toJavaString());
    }
    b.append(")");
    return b.toString();
  }

}
