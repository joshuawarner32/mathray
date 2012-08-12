package mathray;

import java.util.Set;

@SuppressWarnings("serial")
public class UndefinedSymbolException extends RuntimeException {
  
  public final Symbol sym;

  public UndefinedSymbolException(Symbol sym, Set<Symbol> keys) {
    super("undefined symbol " + sym + " choices: " + keys);
    this.sym = sym;
  }

}
