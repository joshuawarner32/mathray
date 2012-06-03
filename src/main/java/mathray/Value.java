package mathray;

import com.google.common.base.Preconditions;

import mathray.eval.text.DefaultPrinter;
import mathray.eval.text.JavaStringizer;
import mathray.util.Vector;
import mathray.visitor.Context;
import mathray.visitor.Processor;
import mathray.visitor.Visitor;
import mathray.visitor.Visitors;
import static mathray.Expressions.*;


public abstract class Value implements Comparable<Value>, Closable {
  
  public abstract void accept(Visitor v);
  
  @Override
  public final String toString() {
    return DefaultPrinter.toString(this);
  }
  
  public final String toJavaString() {
    return JavaStringizer.toJavaString(this);
  }

  public <T> T accept(Processor<T> v) {
    Context<T> ctx = new Context<T>();
    accept(Visitors.toVisitor(v, ctx));
    return ctx.get(this);
  }
  
  @Override
  public <T> Vector<T> acceptVector(Processor<T> v) {
    return vector(accept(v));
  }
  
  @Override
  public Closable wrap(Vector<Value> results) {
    Preconditions.checkArgument(results.size() == 1);
    return results.get(0);
  }
  
}
