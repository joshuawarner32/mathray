package mathray;

import mathray.util.Vector;
import mathray.visitor.Visitor;

public abstract class Struct implements Closable {
  
  public abstract int size();
  
  public abstract Value get(int index);
  
  public abstract Vector<Value> toVector();

  @Override
  public final void accept(Visitor v) {
    for(Value value : toVector()) {
      value.accept(v);
    }
  }
  
}
