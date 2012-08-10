package mathray;

@SuppressWarnings("serial")
public class UndefinedSymbolException extends RuntimeException {
  
  public final Symbol sym;

  public UndefinedSymbolException(Symbol sym) {
    super("undefined symbol " + sym);
    this.sym = sym;
  }

}
