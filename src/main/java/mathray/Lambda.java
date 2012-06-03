package mathray;

import static mathray.Expressions.*;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import mathray.util.Vector;
import mathray.visitor.Processor;
import mathray.visitor.Visitor;

public class Lambda<T extends Closable> implements Closable {
  
  public final Args args;
  
  public final T value;
  
  public Lambda(Args args, T value) {
    this.args = args;
    
    if(value == null) {
      throw new NullPointerException();
    }
    this.value = value;
  }

  public Lambda<T> close(T value) {
    return new Lambda<T>(args, value);
  }

  @Override
  public void accept(Visitor v) {
    value.accept(v);
  }
  
  @Override
  public <K> Vector<K> acceptVector(Processor<K> p) {
    return value.acceptVector(p);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Closable wrap(Vector<Value> results) {
    return new Lambda<T>(args, (T) value.wrap(results));
  }
 
  @Override
  public String toString() {
    return args + " -> " + value.toString();
  }
  
  @Override
  public int hashCode() {
    return args.hashCode() + value.hashCode();
  }
  
  @SuppressWarnings("rawtypes")
  @Override
  public boolean equals(Object other) {
    if(!(other instanceof Lambda)) {
      return false;
    }
    Lambda<?> def = (Lambda)other;
    if(args.size() != def.args.size()) {
      return false;
    }
    if(args.equals(def.args)) {
      return value.equals(def.value);
    }
    // TODO: argument invariance
    return value.equals(def.value);
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
        ((Lambda<?>)value).collectArgs(index + 1, actualArguments, args);
      } catch(ClassCastException e) {
        throw new IllegalArgumentException("closure argument group count (got " + args.length + ") doesn't match up!", e);
      }
    }
  }
  
  public T call(Value... argValues) {
    return call(vector(argValues));
  }

  @SuppressWarnings("unchecked")
  public T call(final Vector<Value> argValues) {
    Preconditions.checkArgument(argValues.size() == args.size());
    return (T)value.wrap(value.acceptVector(new Processor<Value>() {

      @Override
      public Value process(Call call, Vector<Value> args) {
        return call.func.call(args);
      }

      @Override
      public Value process(Symbol sym) {
        int i = args.indexOf(sym);
        if(i >= 0) {
          return argValues.get(i);
        }
        return sym;
      }

      @Override
      public Value process(Rational rat) {
        return rat;
      }
      
    }));
  }
  
}
