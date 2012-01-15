package mathray;

public class Closure<T> {
  
  public final Args args;
  
  public final T def;
  
  public Closure(Args args, T def) {
    this.args = args;
    this.def = def;
  }

}
