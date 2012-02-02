package mathray.eval.java;

import java.util.HashMap;
import java.util.Map;

import mathray.Call;
import mathray.Rational;
import mathray.Symbol;
import mathray.Value;
import mathray.visitor.Visitable;
import mathray.visitor.Visitor;

public class Usage {

  private final Map<Value, Integer> useCounts = new HashMap<Value, Integer>();
  
  private Usage() {}
  
  public void inc(Value value) {
    useCounts.put(value, get(value) + 1);
  }
  
  public int get(Value value) {
    Integer count = useCounts.get(value);
    if(count == null) {
      return 0;
    }
    return count;
  }
  
  public static Usage generate(Visitable def) {
    final Usage ret = new Usage();
    
    Visitor v = new Visitor() {

      @Override
      public void visit(Call call) {
        for(Value arg : call.args.toVector()) {
          ret.inc(arg);
          arg.accept(this);
        }
      }

      @Override
      public void visit(Symbol sym) {}

      @Override
      public void visit(Rational rat) {}
      
    };
    
    def.accept(v);
    
    return ret;
  }

}
