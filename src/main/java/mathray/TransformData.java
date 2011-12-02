package mathray;

import java.util.HashMap;
import java.util.Map;

public class TransformData<FuncData, SymData> {
  
  private Map<Function, FuncData> funcs = new HashMap<Function, FuncData>();
  private Map<Symbol, SymData> syms = new HashMap<Symbol, SymData>();
  
  public FuncData defineFunction(Function func) {
    return funcs.get(func);
  }
  
  public SymData defineSymbol(Symbol sym) {
    return syms.get(sym);
  }
  
}
