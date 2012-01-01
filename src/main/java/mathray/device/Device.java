package mathray.device;

import mathray.Computation;

public interface Device {
  
  public <T> T compile(FunctionType<T> type, Computation comp);
  
  public boolean supports(FunctionType<?> type);

}
