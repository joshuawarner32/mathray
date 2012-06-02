package mathray.flow;

import mathray.Args;
import mathray.annotate.ConstExpr;

public class FlowExpressions {
  
  private FlowExpressions() {}
  
  @ConstExpr
  public static Index index(String name) {
    return new Index(name);
  }
  
  public static FlowBuilder input(int dimensions, Args args) {
    return new FlowBuilder();
  }

}
