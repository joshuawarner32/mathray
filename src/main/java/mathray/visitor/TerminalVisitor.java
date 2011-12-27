package mathray.visitor;

import mathray.Rational;
import mathray.Symbol;


public interface TerminalVisitor<T> {
  
  public T symbol(Symbol sym);

  public T constant(Rational rat);

}
