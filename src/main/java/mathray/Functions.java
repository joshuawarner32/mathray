package mathray;

public class Functions {

  private Functions() {}
  
  public static final Function ADD = new Function("add", 2);
  public static final Function SUB = new Function("sub", 2);
  public static final Function MUL = new Function("mul", 2);
  public static final Function DIV = new Function("div", 2);
  
  public static final Function POW = new Function("pow", 2);
  
  public static final Function NEG = new Function("neg", 1);
  
  public static final Function SIN = new Function("sin", 1);
  public static final Function SINH = new Function("sinh", 1);
  public static final Function ASIN = new Function("asin", 1);
  public static final Function COS = new Function("cos", 1);
  public static final Function COSH = new Function("cosh", 1);
  public static final Function ACOS = new Function("acos", 1);
  public static final Function TAN = new Function("tan", 1);
  public static final Function TANH = new Function("tanh", 1);
  public static final Function ATAN = new Function("atan", 1);
  public static final Function ATAN2 = new Function("atan2", 2);

  public static final Function SQRT = new Function("sqrt", 1);

  // The natural logarithm.  What other base could you possibly care about?
  public static final Function LOG = new Function("log", 1);
  
  public static final Function MIN = new Function("min", 2);
  public static final Function MAX = new Function("max", 2);
  
  public static final Function ABS = new Function("abs", 1);
  
  // selectSign(test, ifNegative, ifZero, ifPositive)
  public static final Function SELECT_SIGN = new Function("selectSign", 4);
  
  // return the next representable number in the target number system 
  public static final Function UP = new Function("up", 1);

  // return the previous representable number in the target number system 
  public static final Function DOWN = new Function("down", 1);
}
