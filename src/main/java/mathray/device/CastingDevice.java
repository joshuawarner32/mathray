package mathray.device;

import java.util.HashMap;
import java.util.Map;

import mathray.Multidef;

public class CastingDevice implements Device {
  
  private interface Compiler<T> {
    public T compile(Multidef def);
  }
  
  private final Map<FunctionType<?>, Compiler<?>> compilers = new HashMap<FunctionType<?>, Compiler<?>>();
  private final Device device;
  
  private <T> void register(FunctionType<T> type, Compiler<T> c) {
    compilers.put(type, c);
  }
  
  private <T> void passThrough(final FunctionType<T> type) {
    register(type, new Compiler<T>() {
      public T compile(Multidef def) {
        return device.compile(type, def);
      }
    });
  }

  public CastingDevice(final Device device) {
    this.device = device;
    
    if(device.supports(FunctionTypes.FUNCTION_F)) {
      passThrough(FunctionTypes.FUNCTION_F);
      if(!device.supports(FunctionTypes.FUNCTION_D)) {
        register(FunctionTypes.FUNCTION_D, new Compiler<FunctionTypes.D>() {
          @Override
          public FunctionTypes.D compile(Multidef def) {
            return FunctionTypes.toD(device.compile(FunctionTypes.FUNCTION_F, def));
          }
        });
      }
    }
    
    if(device.supports(FunctionTypes.FUNCTION_D)) {
      passThrough(FunctionTypes.FUNCTION_D);
      if(!device.supports(FunctionTypes.FUNCTION_F)) {
        register(FunctionTypes.FUNCTION_F, new Compiler<FunctionTypes.F>() {
          @Override
          public FunctionTypes.F compile(Multidef def) {
            return FunctionTypes.toF(device.compile(FunctionTypes.FUNCTION_D, def));
          }
        });
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  private <T> Compiler<T> lookup(FunctionType<T> type) {
    Compiler<T> ret = (Compiler<T>)compilers.get(type);
    if(ret == null) {
      throw new IllegalArgumentException("unsupported function type");
    }
    return ret;
  }

  @Override
  public <T> T compile(FunctionType<T> type, Multidef def) {
    return lookup(type).compile(def);
  }

  @Override
  public boolean supports(FunctionType<?> type) {
    return compilers.containsKey(type);
  }
}
