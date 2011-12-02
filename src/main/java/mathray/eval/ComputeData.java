package mathray.eval;

import java.util.HashMap;
import java.util.Map;

import mathray.Computation;
import mathray.Function;

public class ComputeData {
  private final Map<Function, Computation> impls = new HashMap<Function, Computation>();
  
  private ComputeData() {}

  public Computation implement(Function func) {
    Computation impl = impls.get(func);
    if(impl == null) {
      throw new RuntimeException("function " + func.name + " not implemented");
    }
    return impl;
  }
  
  public static class Builder<T> {
    private Builder() {}
    
    private ComputeData env = new ComputeData();
    
    public Builder<T> register(Function func, Computation impl) {
      env.impls.put(func, impl);
      return this;
    }
    
    public ComputeData build() {
      ComputeData ret = env;
      env = null;
      return ret;
    }

  }
  
  public static <T> Builder<T> builder() {
    return new Builder<T>();
  }
}
