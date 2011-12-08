package mathray;

import java.util.HashMap;
import java.util.Map;

public class FunctionRegistrar<FuncData> {

  private Map<Function, FuncData> funcs = new HashMap<Function, FuncData>();
  
  public void register(Function func, FuncData data) {
    funcs.put(func, data);
  }
  
  public FuncData lookup(Function func) {
    return funcs.get(func);
  }
  
}
