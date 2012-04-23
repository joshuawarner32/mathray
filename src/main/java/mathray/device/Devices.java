package mathray.device;

public class Devices {
  
  private Devices() {}
  
  public static <T, Clos> Device toDevice(final FunctionType<T, Clos> myType, final Compiler<T, Clos> compiler) {
    return new Device() {
      @SuppressWarnings("unchecked")
      @Override
      public <T2, Clos2> T2 compile(FunctionType<T2, Clos2> type, Clos2 def) {
        if(!myType.equals(type)) {
          throw new IllegalArgumentException();
        }
        return (T2)compiler.compile((Clos)def);
      }
    
      @Override
      public int cost(FunctionType<?, ?> type) {
        if(!myType.equals(type)) {
          return Device.NO_SUPPORT;
        }
        return compiler.cost();
      }
    
      @Override
      public int compileCost(FunctionType<?, ?> type) {
        if(!myType.equals(type)) {
          return Device.NO_SUPPORT;
        }
        return compiler.compileCost();
      }
    };
  }

}
