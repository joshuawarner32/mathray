package mathray;

import mathray.eval.Visitor;

public abstract class Value implements Comparable<Value> {
  
  public abstract <T> T accept(Visitor<T> v);
  
  public final Vector<Value> toVector() {
    return new Vector<Value>(this);
  }

  public abstract String toJavaString();
  
}
