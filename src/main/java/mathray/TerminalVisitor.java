package mathray;


public interface TerminalVisitor<T> {
  
  public T symbol(Symbol sym);

  public T constant(Rational rat);

}
