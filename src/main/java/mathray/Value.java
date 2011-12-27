package mathray;

import mathray.visitor.EvaluatingVisitor;
import mathray.visitor.SimpleVisitor;
import mathray.visitor.Visitors;


public abstract class Value implements Comparable<Value> {
  
  abstract <T> T accept(SimpleVisitor<T> v);
  
  public abstract String toJavaString();

  public <T> T accept(EvaluatingVisitor<T> v) {
    return accept(Visitors.cache(Visitors.simple(v)));
  }
  
}
