package mathray;

import java.util.ArrayList;
import java.util.List;

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

  public final <K> void collectArgs(Vector<K>... args) {
    List<K> actualArguments = new ArrayList<K>();
  	collectArgs(0, actualArguments, args);
  }
  
  private final <K> void collectArgs(int index, List<K> actualArguments, Vector<K>... args) {
    if(args[index].size() != this.args.size()) {
      throw new IllegalArgumentException("closure arguments at " + index + " don't match up");
    }
    actualArguments.addAll(args[index]);
    if(index < args.length) {
      try {
        ((Closure<?>)value).collectArgs(index + 1, actualArguments, args);
      } catch(ClassCastException e) {
        throw new IllegalArgumentException("closure argument group count (got " + args.length + ") doesn't match up!", e);
      }
    }
  }
  
}
