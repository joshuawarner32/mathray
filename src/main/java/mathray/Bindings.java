package mathray;

import mathray.util.Vector;

public class Bindings<T> {
  
  public final Args args;
  
  public final Vector<T> values;
  
  public Bindings(Args args, Vector<T> values) {
    if(args.size() != values.size()) {
      throw new IllegalArgumentException();
    }
    this.args = args;
    this.values = values;
  }
  
  public T get(Symbol sym) {
    return values.get(args.indexOf(sym));
  }

}
