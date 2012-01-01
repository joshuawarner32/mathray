package mathray.device;

public class FunctionType<T> {
  
  public final Class<T> interfaceClass;
  
  public FunctionType(Class<T> interfaceClass) {
    this.interfaceClass = interfaceClass;
  }

}
