package mathray;

import mathray.util.Vector;
import mathray.visitor.Processor;
import mathray.visitor.Visitable;

public interface Closable extends Visitable {
  
  public <T> Vector<T> acceptVector(Processor<T> p);
  
  // This should return the Self-type, but the recursive relationship with Lambda makes this impossible
  public Closable wrap(Vector<Value> results);
  
}
