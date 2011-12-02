package mathray.eval;

import java.util.HashMap;
import java.util.Map;

import mathray.Call;
import mathray.Rational;
import mathray.Transformer;
import mathray.Value;
import mathray.Symbol;
import mathray.Vector;

public class Context<T> implements Visitor<T> {
  
  private final Binder<T> binder;
  private final Implementer<T> implementer;
  private final Translator<T> translator;
  
  private final Map<Value, T> cache = new HashMap<Value, T>();
  
  public Context(Binder<T> binder, Implementer<T> implementer, Translator<T> translator) {
    this.binder = binder;
    this.implementer = implementer;
    this.translator = translator;
  }

  @Override
  public T call(Call call) {
    T ret = cache.get(call);
    if(ret == null) {
      cache.put(call, ret = implementer.implement(call.func).call(call.visitArgs(this)));
    }
    return ret;
  }

  @Override
  public T symbol(Symbol var) {
    T ret = cache.get(var);
    if(ret == null) {
      cache.put(var, ret = binder.bind(var));
    }
    return ret;
  }

  @Override
  public T constant(Rational cst) {
    T ret = cache.get(cst);
    if(ret == null) {
      cache.put(cst, ret = translator.translate(cst));
    }
    return ret;
  }

  public Vector<T> run(Vector<Value> values) {
    return values.transform(new Transformer<Value, T>() {
      @Override
      public T transform(Value in) {
        return in.accept(Context.this);
      }
    });
  }  
}
