package mathray;

import java.util.HashMap;
import java.util.Map;

public class FunctionSymbolRegistrar<FuncData, SymData> extends FunctionRegistrar<FuncData> {

  private Map<Symbol, SymData> syms = new HashMap<Symbol, SymData>();
  
  public void register(Symbol sym, SymData data) {
    syms.put(sym, data);
  }
  
  public SymData lookup(Symbol sym) {
    return syms.get(sym);
  }
  
}
