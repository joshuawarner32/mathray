package mathray;

import mathray.eval.text.DefaultPrinter;
import mathray.eval.text.JavaString;
import mathray.visitor.Context;
import mathray.visitor.Processor;
import mathray.visitor.Visitor;
import mathray.visitor.Visitors;


public abstract class Value implements Comparable<Value>, Closable {
  
  public abstract void accept(Visitor v);
  
  @Override
  public final String toString() {
    return DefaultPrinter.toString(this);
  }
  
  public final String toJavaString() {
    return JavaString.toJavaString(this);
  }

  public <T> T accept(Processor<T> v) {
    Context<T> ctx = new Context<T>();
    accept(Visitors.toVisitor(v, ctx));
    return ctx.get(this);
  }
  
}
