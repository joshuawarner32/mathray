package mathray;


public interface Visitor<T> extends TerminalVisitor<T> {

  public T call(Function func, Vector<T> args);
  
}
