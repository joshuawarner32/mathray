package mathray;

import mathray.util.Vector;
import mathray.visitor.Processor;
import mathray.visitor.Visitable;

public interface Closable extends Visitable {
  
  public <T> Vector<T> acceptVector(Processor<T> v);
  
}
