package mathray;

import mathray.util.Vector;

public class Multivalue extends Struct {
  
  public final Vector<Value> values;
  
  public Multivalue(Vector<Value> values) {
    this.values = values;
  }

  @Override
  public int size() {
    return values.size();
  }

  @Override
  public Value get(int index) {
    return values.get(index);
  }

  @Override
  public Vector<Value> toVector() {
    return values;
  }
  
  @Override
  public String toString() {
    return values.toString();
  }

}
