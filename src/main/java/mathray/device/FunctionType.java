package mathray.device;

public class FunctionType<T, Clos> {
  
  public final Class<T> interfaceClass;
  
  public final Class<Clos> closureClass;
  
  public FunctionType(Class<T> interfaceClass, Class<Clos> closureClass) {
    this.interfaceClass = interfaceClass;
    this.closureClass = closureClass;
  }

}
