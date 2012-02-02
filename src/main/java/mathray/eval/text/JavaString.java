package mathray.eval.text;

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
        StringBuilder b = new StringBuilder();
        b.append(call.func.name);
        b.append("(");
        if(args.size() > 0) {
          b.append(call.args.get(0).toJavaString());
        }
        for(int i = 1; i < args.size(); i++) {
          b.append(", ");
          b.append(call.args.get(i).toJavaString());
        }
        b.append(")");
        return b.toString();
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
