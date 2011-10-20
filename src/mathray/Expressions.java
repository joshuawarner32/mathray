package mathray;

import static mathray.Functions.*;

public class Expressions {
  
  private Expressions() {}
  
  public static Variable var(String name) {
    return new Variable(name);
  }
  
  public static Args args(Variable... args) {
    return new Args(args);
  }
  
  public static <T> Vector<T> vector(T... values) {
    return new Vector<T>(values);
  }
  
  public static Definition def(Args args, Value... values) {
    return new Definition(args, vector(values));
  }
  
  public static Value fold(Function func, Value... values) {
    if(values.length < 2) {
      throw new IllegalArgumentException("length of values must be > 1");
    }
    if(func.outputArity != 1 || func.inputArity != 2) {
      throw new IllegalArgumentException("arity of func");
    }
    Value res = func.call(vector(values[0], values[1])).select(0);
    for(int i = 2; i < values.length; i++) {
      res = func.call(vector(res, values[i])).select(0);
    }
    return res;
  }
  
  public static Value fold(Function func, Vector<Value> values) {
    if(values.size() < 1) {
      throw new IllegalArgumentException("length of values must be > 0");
    } else if(values.size() < 2) {
      return values.get(0);
    }
    if(func.outputArity != 1 || func.inputArity != 2) {
      throw new IllegalArgumentException("arity of func");
    }
    Value res = func.call(vector(values.get(0), values.get(1))).select(0);
    for(int i = 2; i < values.size(); i++) {
      res = func.call(vector(res, values.get(i))).select(0);
    }
    return res;
  }
  
  public static Value fold(Function func, Value start, Vector<Value> values) {
    if(values.size() < 1) {
      return start;
    }
    if(func.outputArity != 1 || func.inputArity != 2) {
      throw new IllegalArgumentException("arity of func");
    }
    Value res = start;
    for(int i = 1; i < values.size(); i++) {
      res = func.call(vector(res, values.get(i))).select(0);
    }
    return res;
  }
  
  public static Value add(Value... values) {
    return fold(ADD, values);
  }
  
  public static Value sub(Value... values) {
    return fold(SUB, values);
  }
  
  public static Value mul(Value... values) {
    return fold(MUL, values);
  }
  
  public static Value div(Value... values) {
    return fold(DIV, values);
  }
  
  public static Value pow(Value a, Value b) {
    return POW.call(a, b).select(0);
  }
  
  public static Value neg(Value value) {
    return NEG.call(value).select(0);
  }
  
  public static Call minMax(Value... values) {
    return MIN_MAX.call(values);
  }
  
  public static Value min(Value... values) {
    return MIN_MAX.call(values).select(0);
  }
  
  public static Value max(Value... values) {
    return MIN_MAX.call(values).select(1);
  }
  
  public static Value sin(Value... values) {
    return SIN.call(values).select(0);
  }
  
  public static Value cos(Value... values) {
    return COS.call(values).select(0);
  }
  
  public static Value tan(Value... values) {
    return TAN.call(values).select(0);
  }
  
  public static Value sqrt(Value... values) {
    return SQRT.call(values).select(0);
  }
  
  public static Value num(long value) {
    return Constant.get(value);
  }

}
