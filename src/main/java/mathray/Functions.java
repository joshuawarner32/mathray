package mathray;

import static mathray.Expressions.*;

public class Functions {

  private Functions() {}
  
  public static final Function ADD = new Function("add", 2, 1);
  public static final Function SUB = new Function("sub", 2, 1);
  public static final Function MUL = new Function("mul", 2, 1);
  public static final Function DIV = new Function("div", 2, 1);
  
  public static final Function POW = new Function("pow", 2, 1);
  
  public static final Function NEG = new Function("neg", 1, 1);
  
  public static final Function SIN = new Function("sin", 1, 1);
  public static final Function SINH = new Function("sinh", 1, 1);
  public static final Function COS = new Function("cos", 1, 1);
  public static final Function COSH = new Function("cosh", 1, 1);
  public static final Function TAN = new Function("tan", 1, 1);

  public static final Function SQRT = new Function("sqrt", 1, 1);

  public static final Function LN = new Function("ln", 1, 1);
  
  public static final Function MIN_MAX = new Function("minMax", 2, 2, vector("min", "max"));
  
  public static final Function ABS = new Function("abs", 1, 1);
  
  public static final Function SELECT_SIGN = new Function("selectSign", 4, 1);
}
