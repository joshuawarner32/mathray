package mathray.visitor;

import java.util.HashMap;
import java.util.Map;

import mathray.Struct;
import mathray.Value;
import mathray.util.Transformer;
import mathray.util.Vector;

public class Context<T> {
  
  private final Map<Value, T> map = new HashMap<Value, T>();
  
  public void put(Value v, T t) {
    map.put(v, t);
  }
  
  public T get(Value v) {
    return map.get(v);
  }
  
  public Vector<T> getStruct(Struct args) {
    return args.toVector().transform(new Transformer<Value, T>() {
      @Override
      public T transform(Value in) {
        return get(in);
      }
    });
  }

}
