package mathray;
import java.util.HashMap;
import java.util.Map;


public class SymbolSource {
  
  private Map<String, Symbol> symbols = new HashMap<String, Symbol>();
  
  public Symbol getSymbol(String name) {
    Symbol sym = symbols.get(name);
    if(sym == null) {
      symbols.put(name, sym = new Symbol(name));
    }
    return sym;
  }

}
