package mathray;

@SuppressWarnings("serial")
public class UndefinedFunctionException extends RuntimeException {
  
  public final Function func;

  public UndefinedFunctionException(Function func) {
    super(func.toString());
    this.func = func;
  }

}
