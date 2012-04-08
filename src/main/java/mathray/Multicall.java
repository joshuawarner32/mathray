package mathray;

import static mathray.Expressions.*;

import mathray.util.Generator;
import mathray.util.Transformer;
import mathray.util.Vector;

public class Multicall extends Struct {
  
  public final Function func;
  
  public final int size;
  
  public final Vector<Struct> args;
  
  public Multicall(Function func, Vector<Struct> args) {
    this.func = func;
    this.args = args;
    if(args.size() != func.arity) {
      throw new IllegalArgumentException("function arity does not match");
    }
    int size = -1;
    for(Struct a : args) {
      if(size == -1) {
        size = a.size();
      } else if(a.size() != size) {
        throw new IllegalArgumentException("argument width does not match");
      }
    }
    this.size = size;
  }
  
  @Override
  public int size() {
    return size;
  }
  
  @Override
  public Value get(final int index) {
    return func.call(struct(args.transform(new Transformer<Struct, Value>() {
      @Override
      public Value transform(Struct in) {
        return in.get(index);
      }
    })));
  }
  
  @Override
  public Vector<Value> toVector() {
    return Vector.<Value>generate(size(), new Generator<Value>() {
      @Override
      public Value generate(int index) {
        return get(index);
      }
    });
  }
  
  @Override
  public String toString() {
    return toVector().toString();
  }

}
