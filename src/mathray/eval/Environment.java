package mathray.eval;

import java.util.HashMap;
import java.util.Map;

import mathray.Function;

public class Environment<T> implements Implementer<T> {
  private final Map<Function, Impl<T>> impls = new HashMap<Function, Impl<T>>();
  private Impl<T> default_; 
  
  private Environment() {}

  public Impl<T> implement(Function func) {
    Impl<T> impl = impls.get(func);
    if(impl == null) {
      if(default_ == null) {
        throw new RuntimeException("function " + func.name + " not implemented");
      } else {
        return default_;
      }
    }
    return impl;
  }
  
  public static class Builder<T> {
    private Builder() {}
    
    private Environment<T> env = new Environment<T>();
    
    public Builder<T> register(Function func, Impl<T> impl) {
      env.impls.put(func, impl);
      return this;
    }
    
    public Environment<T> build() {
      Environment<T> ret = env;
      env = null;
      return ret;
    }

    public Builder<T> registerDefault(Impl<T> impl) {
      env.default_ = impl;
      return this;
    }
  }
  
  public static <T> Builder<T> builder() {
    return new Builder<T>();
  }
}
