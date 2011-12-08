package mathray.eval;

import java.util.HashMap;
import java.util.Map;

import mathray.Function;
import mathray.Symbol;

public class EvalData<FuncData, SymData> {
  private Map<Function, FuncData> funcs = new HashMap<Function, FuncData>();
  private Map<Symbol, SymData> syms = new HashMap<Symbol, SymData>();
  
  private EvalData() {}
  
  public FuncData defineFunction(Function func) {
    FuncData ret = funcs.get(func);
    if(ret == null) {
      throw new RuntimeException("function not found");
    }
    return ret;
  }
  
  public SymData defineSymbol(Symbol sym) {
    SymData ret = syms.get(sym);
    if(ret == null) {
      throw new RuntimeException("symbol not found");
    }
    return ret;
  }
  
  public static class Builder<FuncData, SymData> {
    private Builder() {}
    
    private EvalData<FuncData, SymData> env = new EvalData<FuncData, SymData>();
    
    public Builder<FuncData, SymData> register(Function func, FuncData impl) {
      env.funcs.put(func, impl);
      return this;
    }

    public Builder<FuncData, SymData> register(Symbol sym, SymData data) {
      env.syms.put(sym, data);
      return this;
    }
    
    public EvalData<FuncData, SymData> build() {
      EvalData<FuncData, SymData> ret = env;
      env = null;
      return ret;
    }
  }
  
  public static <FuncData, SymData> Builder<FuncData, SymData> builder() {
    return new Builder<FuncData, SymData>();
  }
}
