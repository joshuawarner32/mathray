package mathray;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mathray.eval.Binder;

public class Args implements Iterable<Variable> {
  
  private Map<Variable, Integer> vars = new HashMap<Variable, Integer>();
  
  public int getIndex(Variable var) {
    return vars.get(var);
  }

  public Variable get(int index) {
    for(Map.Entry<Variable, Integer> entry : vars.entrySet()) {
      if(entry.getValue() == index) {
        return entry.getKey();
      }
    }
    throw new IndexOutOfBoundsException();
  }
  
  public Args(Variable... args) {
    for(int i = 0; i < args.length; i++) {
      vars.put(args[i], i);
    }
  }

  public int size() {
    return vars.size();
  }

  public boolean contains(Variable var) {
    return vars.get(var) != null;
  }

  @Override
  public Iterator<Variable> iterator() {
    return vars.keySet().iterator();
  }
  
  public <T> Binder<T> bind(final Vector<T> params) {
    return new Binder<T>() {
      @Override
      public T bind(Variable var) {
        return params.get(getIndex(var));
      }
    };
  }
  
  @Override
  public int hashCode() {
    return vars.hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    return obj instanceof Args && vars.equals(((Args)obj).vars);
  }

  @SuppressWarnings("unchecked")
  public Vector<Variable> toVector() {
    return new Vector(vars.keySet().toArray());
  }

}
