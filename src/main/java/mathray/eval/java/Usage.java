package mathray.eval.java;

import java.util.HashMap;
import java.util.Map;

import mathray.Call;
import mathray.Computation;
import mathray.Rational;
import mathray.Symbol;
import mathray.Value;
import mathray.visitor.SimpleVisitor;

public class Usage {

  private final Map<Value, Integer> useCounts = new HashMap<Value, Integer>();
  
  private Usage() {}
  
  private void inc(Value value) {
    Integer count = useCounts.get(value);
    if(count == null) {
      count = 0;
    }
    useCounts.put(value, count + 1);
  }
  
  public static Usage generate(Computation comp) {
    final Usage ret = new Usage();
    
    SimpleVisitor<Void> v = new SimpleVisitor<Void>() {

      @Override
      public Void symbol(Symbol sym) {
        return null;
      }

      @Override
      public Void constant(Rational rat) {
        return null;
      }

      @Override
      public Void call(Call call) {
        for(Value arg : call.args) {
          ret.inc(arg);
          arg.accept(this);
        }
        return null;
      }
      
    };
    
    for(Value val : comp.values) {
      val.accept(v);
    }
    
    return ret;
  }

}
