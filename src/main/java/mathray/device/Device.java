package mathray.device;

public interface Device {
  
  public static final int NO_SUPPORT = Integer.MAX_VALUE;
  
  public <T, Clos> T compile(FunctionType<T, Clos> type, Clos def);
  
  public int cost(FunctionType<?, ?> type);
  
  public int compileCost(FunctionType<?, ?> type);

}
