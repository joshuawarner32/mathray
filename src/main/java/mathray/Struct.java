package mathray;

import mathray.util.Vector;

public interface Struct {
  
  public int size();
  
  public Value get(int index);
  
  public Vector<Value> toVector();
  
}
