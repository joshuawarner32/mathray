package mathray;

import mathray.visitor.Visitor;

public class Closure<T extends Closable> implements Closable {
  
  public final Args args;
  
  public final T value;
  
  public Closure(Args args, T value) {
    this.args = args;
    this.value = value;
  }

  public Closure<T> close(T value) {
    return new Closure<T>(args, value);
  }

  @Override
  public void accept(Visitor v) {
    value.accept(v);
  }
  
}
