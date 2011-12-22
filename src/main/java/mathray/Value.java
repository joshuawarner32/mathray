package mathray;


public abstract class Value implements Comparable<Value> {
  
  abstract <T> T accept(InternalVisitor<T> v);
  
  public final <T> T accept(Visitor<T> v) {
    
  }

  public abstract String toJavaString();
  
}
