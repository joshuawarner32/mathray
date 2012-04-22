package mathray.visitor;

import java.util.HashSet;
import java.util.Set;

import mathray.Call;
import mathray.Rational;
import mathray.Symbol;
import mathray.Value;

public class Visitors {
  
  private Visitors() {}
  
  public static <T> Visitor toVisitor(final Processor<T> v, final Context<T> ctx) {
    return new Visitor() {
      @Override
      public void visit(Symbol sym) {
        T ret = ctx.get(sym);
        if(ret == null) {
          ctx.put(sym, ret = v.process(sym));
        }
      }

      @Override
      public void visit(Rational rat) {
        T ret = ctx.get(rat);
        if(ret == null) {
          ctx.put(rat, ret = v.process(rat));
        }
      }

      @Override
      public void visit(Call call) {
        T ret = ctx.get(call);
        if(ret == null) {
          call.args.accept(this);
          ctx.put(call, ret = v.process(call, ctx.getStruct(call.args)));
        }
      }
      
    };
  }
  
  public static Set<Symbol> getSymbols(Value... values) {
    final Set<Symbol> ret = new HashSet<Symbol>();
    Visitor visitor = new Visitor() {
      @Override
      public void visit(Rational rat) {}
      
      @Override
      public void visit(Symbol sym) {
        ret.add(sym);
      }
      
      @Override
      public void visit(Call call) {}
    };
    for(Value v : values) {
      v.accept(visitor);
    }
    return ret;
  }
  
}
