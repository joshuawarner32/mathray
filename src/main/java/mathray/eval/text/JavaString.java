package mathray.eval.text;

import com.google.common.base.Joiner;

import mathray.Call;
import mathray.Rational;
import mathray.Symbol;
import mathray.Value;
import mathray.util.Vector;
import mathray.visitor.Processor;

public class JavaString {
  
  private JavaString() {}
  
  public static String toJavaString(Value v) {
    return v.accept(new Processor<String>() {
      @Override
      public String process(Call call, Vector<String> args) {
        return call.func.name + '(' + Joiner.on(", ").join(args) + ')';
      }
      
      @Override
      public String process(Rational rat) {
        if(rat.denom == 1) {
          return "num(" + rat.num + ")";
        } else {
          return "num(" + rat.num + ", " + rat.denom + ")";
        }
      }
      
      @Override
      public String process(Symbol sym) {
        return sym.name;
      }
    });
  }

}
