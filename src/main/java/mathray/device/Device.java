package mathray.device;

import mathray.Multidef;

public interface Device {
  
  public <T> T compile(FunctionType<T> type, Multidef def);
  
  public boolean supports(FunctionType<?> type);

}
