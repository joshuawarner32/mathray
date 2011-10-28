package mathray.eval.simplify;

import java.util.Map;
import java.util.TreeMap;

public final class Bag<E> {
  private final Map<E, Integer> counts;
  
  public Bag() {
    counts = new TreeMap<E, Integer>();
  }
  
  public Bag(Bag<E> copy) {
    counts = new TreeMap<E, Integer>(copy.counts);
  }
  
  private void addMutate(E item, int count) {
    Integer i = counts.get(item);
    if(i == null) {
      i = 0;
    }
    counts.put(item, i + count);
  }
  
  public Bag<E> add(E item) {
    Bag<E> ret = new Bag<E>(this);
    ret.addMutate(item, 1);
    return ret;
  }
  
  public Bag<E> addAll(Bag<E> items) {
    Bag<E> ret = new Bag<E>(this);
    for(Map.Entry<E, Integer> entry : items.counts.entrySet()) {
      ret.addMutate(entry.getKey(), entry.getValue());
    }
    return ret;
  }
  
}
