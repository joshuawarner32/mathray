package mathray;

public class Closure<T> {
  
  public final Args args;
  
  public final T value;
  
  public Closure(Args args, T value) {
    this.args = args;
    this.value = value;
  }

}
