package mathray.device;

public interface Compiler<T, Clos> {
  
  public T compile(Clos def);
  
  public int cost();
  
  public int compileCost();

}
