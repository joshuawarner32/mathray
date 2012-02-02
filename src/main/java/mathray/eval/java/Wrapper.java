package mathray.eval.java;

import mathray.Symbol;

public abstract class Wrapper<T> {
  
  public final ClassGenerator cgen;
  public final MethodGenerator mgen;
  
  public Wrapper(ClassGenerator cgen, MethodGenerator mgen) {
    this.cgen = cgen;
    this.mgen = mgen;
  }
  
  public abstract JavaValue symbol(Symbol sym);
  
  public abstract void ret(int index, JavaValue value);
  
  public abstract T end();

}
