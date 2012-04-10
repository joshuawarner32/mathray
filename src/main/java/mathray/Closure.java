package mathray;

import mathray.util.Vector;
import mathray.visitor.Processor;
import mathray.visitor.Visitor;

public class Closure<T extends Closable> implements Closable {
  
  public final Args args;
  
  public final T value;
  
  public Closure(Args args, T value) {
    this.args = args;
    
    if(value == null) {
      throw new NullPointerException();
    }
    this.value = value;
  }

  public Closure<T> close(T value) {
    return new Closure<T>(args, value);
  }

  @Override
  public void accept(Visitor v) {
    value.accept(v);
  }
  
  @Override
  public <K> Vector<K> acceptVector(Processor<K> v) {
    return value.acceptVector(v);
  }
 
  @Override
  public String toString() {
    return args + " = " + value.toString();
  }
  
}
