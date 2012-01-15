package mathray.eval.java;

import java.util.HashMap;
import java.util.Map;

import mathray.Call;
import mathray.Multidef;
import mathray.Rational;
import mathray.Symbol;
import mathray.Value;
import mathray.visitor.SimpleVisitor;

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
  
  public static Usage generate(Multidef def) {
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
    
    for(Value val : def.value.toVector()) {
      val.accept(v);
    }
    
    return ret;
  }

}
