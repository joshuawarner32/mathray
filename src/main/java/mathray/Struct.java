package mathray;

import mathray.util.Vector;
import mathray.visitor.Context;
import mathray.visitor.Processor;
import mathray.visitor.Visitor;
import mathray.visitor.Visitors;

public abstract class Struct implements Closable {
  
  private int hashCode = -1;
  
  public abstract int size();
  
  public abstract Value get(int index);
  
  public abstract Vector<Value> toVector();

  @Override
  public final void accept(Visitor v) {
    for(Value value : toVector()) {
      value.accept(v);
    }
  }
  
  @Override
  public <T> Vector<T> acceptVector(Processor<T> v) {
    Context<T> ctx = new Context<T>();
    accept(Visitors.toVisitor(v, ctx));
    return ctx.getStruct(this);
  }
  
  @Override
  public final boolean equals(Object obj) {
    return obj instanceof Struct &&
      toVector().equals(((Struct)obj).toVector());
  }
  
  @Override
  public final int hashCode() {
    if(hashCode == -1) {
      hashCode = toVector().hashCode();
    }
    return hashCode;
  }
  
}
