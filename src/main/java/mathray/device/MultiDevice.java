package mathray.device;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mathray.Multidef;

public class MultiDevice implements Device {
  
  private static class Support<T, Clos> {
    public final Compiler<T, Clos> compiler;
    public final int cost;
    public final int compileCost;
    
    public Support(Compiler<T, Clos> compiler, int cost, int compileCost) {
      this.compiler = compiler;
      this.cost = cost;
      this.compileCost = compileCost;
    }
  }
  
  private final Map<FunctionType<?, ?>, Support<?, ?>> support = new HashMap<FunctionType<?, ?>, Support<?, ?>>();
  
  private final Set<Device> devices;
  
  private <T, Clos> Support<T, Clos> register(FunctionType<T, Clos> type, int cost, int compileCost, Compiler<T, Clos> c) {
    Support<T, Clos> s = new Support<T, Clos>(c, cost, compileCost);
    support.put(type, s);
    return s;
  }
  
  private <T, Clos> Support<T, Clos> passThrough(final FunctionType<T, Clos> type, final Device device) {
    return register(type, device.cost(type), device.compileCost(type), new Compiler<T, Clos>() {
      public T compile(Clos def) {
        return device.compile(type, def);
      }
    });
  }
  
  private <T, Clos> Device findDevice(FunctionType<T, Clos> type) {
    Device minD = null;
    int min = Device.NO_SUPPORT;
    
    for(Device d : devices) {
      int c = d.cost(type);
      if(c < min) {
        min = c;
        minD = d;
      }
    }
    
    return minD;
  }
  
  @SuppressWarnings("unchecked")
  private <T, Clos> Support<T, Clos> findSupport(FunctionType<T, Clos> type) {
    Support<?, ?> s = support.get(type);
    
    if(s == null) {
      Device d = findDevice(type);
      if(d == null) {
        return null;
      }
      s = passThrough(type, d);
    }
    
    return (Support<T, Clos>) s;
  }
  
  private static final int BASE_CASTING_COST = 5;
  private static final int BASE_CASTING_COMPILE_COST = 1;
  
  private <T, Clos> void registerCast(Device device, FunctionType<?, ?> from, FunctionType<T, Clos> to) {
  }

  public MultiDevice(Set<Device> devices) {
    this.devices = new HashSet<Device>(devices);
    
    final Device deviceF = findDevice(FunctionTypes.FUNCTION_F);
    final Device deviceD = findDevice(FunctionTypes.FUNCTION_D);
    
    if(deviceF != null && deviceD == null) {
      register(FunctionTypes.FUNCTION_D, BASE_CASTING_COST + deviceF.cost(FunctionTypes.FUNCTION_D), BASE_CASTING_COMPILE_COST + deviceF.compileCost(FunctionTypes.FUNCTION_D), new Compiler<FunctionTypes.D, Multidef>() {
        @Override
        public FunctionTypes.D compile(Multidef def) {
          return FunctionTypes.toD(deviceF.compile(FunctionTypes.FUNCTION_F, def));
        }
      });
    }
    
    if(deviceF == null && deviceD != null) {
      register(FunctionTypes.FUNCTION_F, BASE_CASTING_COST + deviceF.cost(FunctionTypes.FUNCTION_D), BASE_CASTING_COMPILE_COST + deviceF.compileCost(FunctionTypes.FUNCTION_D), new Compiler<FunctionTypes.F, Multidef>() {
        @Override
        public FunctionTypes.F compile(Multidef def) {
          return FunctionTypes.toF(deviceD.compile(FunctionTypes.FUNCTION_D, def));
        }
      });
    }
  }
  
  @SuppressWarnings("unchecked")
  private <T, Clos> Compiler<T, Clos> lookup(FunctionType<T, Clos> type) {
    Compiler<T, Clos> ret = (Compiler<T, Clos>)support.get(type).compiler;
    if(ret == null) {
      throw new IllegalArgumentException("unsupported function type");
    }
    return ret;
  }

  @Override
  public <T, Clos> T compile(FunctionType<T, Clos> type, Clos def) {
    return lookup(type).compile(def);
  }

  @Override
  public int cost(FunctionType<?, ?> type) {
    Support<?, ?> s = findSupport(type);
    
    if(s == null) {
      return Device.NO_SUPPORT;
    }
    
    return s.cost;
  }

  @Override
  public int compileCost(FunctionType<?, ?> type) {
    Support<?, ?> s = findSupport(type);
    
    if(s == null) {
      return Device.NO_SUPPORT;
    }
    
    return s.compileCost;
  }
}
