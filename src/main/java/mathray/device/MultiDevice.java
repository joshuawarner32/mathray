package mathray.device;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mathray.Multidef;

public class MultiDevice implements Device {
  
  private final Map<FunctionType<?, ?>, Device> support = new HashMap<FunctionType<?, ?>, Device>();
  
  private final Set<Device> devices;
  
  private <T, Clos> void register(FunctionType<T, Clos> type, Device device) {
    support.put(type, device);
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
  
  private <T, Clos> Device findSupport(FunctionType<T, Clos> type) {
    Device d = support.get(type);
    
    if(d == null) {
      d = findDevice(type);
      if(d == null) {
        return null;
      }
      register(type, d);
    }
    
    return d;
  }
  
  private static final int BASE_CASTING_COST = 5;
  private static final int BASE_CASTING_COMPILE_COST = 1;
  
  private interface Caster<T, Clos> {
    public T compile(Device fromDevice, Clos def);
  }
  
  private <T, Clos> void maybeRegisterCast(FunctionType<?, ?> from, FunctionType<T, Clos> to, final Caster<T, Clos> compiler) {
    Device toDevice = findDevice(to);
    if(toDevice == null) {
      final Device device = findDevice(from);
      if(device != null) {
        register(to, new Device() {
          @SuppressWarnings("unchecked")
          @Override
          public <T2, Clos2> T2 compile(FunctionType<T2, Clos2> type, Clos2 def) {
            return (T2)compiler.compile(device, (Clos)def);
          }
    
          @Override
          public int cost(FunctionType<?, ?> type) {
            return BASE_CASTING_COST + device.cost(type);
          }
    
          @Override
          public int compileCost(FunctionType<?, ?> type) {
            return BASE_CASTING_COMPILE_COST + device.compileCost(type);
          }
        });
      }
    }
  }

  public MultiDevice(Set<Device> devices) {
    this.devices = new HashSet<Device>(devices);
    
    maybeRegisterCast(FunctionTypes.FUNCTION_F, FunctionTypes.FUNCTION_D, new Caster<FunctionTypes.D, Multidef>() {
      @Override
      public FunctionTypes.D compile(Device device, Multidef def) {
        return FunctionTypes.toD(device.compile(FunctionTypes.FUNCTION_F, def));
      }
    });

    maybeRegisterCast(FunctionTypes.FUNCTION_D, FunctionTypes.FUNCTION_F, new Caster<FunctionTypes.F, Multidef>() {
      @Override
      public FunctionTypes.F compile(Device device, Multidef def) {
        return FunctionTypes.toF(device.compile(FunctionTypes.FUNCTION_D, def));
      }
    });
  }

  @Override
  public <T, Clos> T compile(FunctionType<T, Clos> type, Clos def) {
    return findSupport(type).compile(type, def);
  }

  @Override
  public int cost(FunctionType<?, ?> type) {
    Device d = findSupport(type);
    
    if(d == null) {
      return Device.NO_SUPPORT;
    }
    
    return d.cost(type);
  }

  @Override
  public int compileCost(FunctionType<?, ?> type) {
    Device d = findSupport(type);
    
    if(d == null) {
      return Device.NO_SUPPORT;
    }
    
    return d.compileCost(type);
  }
}
