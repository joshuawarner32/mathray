package mathray.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public final class Vector<T> implements Iterable<T> {
  
  private T[] items;
  
  public T get(int index) {
    return items[index];
  }
  
  public int size() {
    return items.length;
  }
  
  public Vector(T... items) {
    this.items = items.clone();
    for(int i = 0; i < items.length; i++) {
      if(items[i] == null) {
        throw new NullPointerException();
      }
    }
  }
  
  private Vector() {}
  
  @SuppressWarnings("unchecked")
  public <O> Vector<O> transform(Transformer<T, O> t) {
    Vector<O> ret = new Vector<O>();
    ret.items = (O[])new Object[items.length];
    for(int i = 0; i < items.length; i++) {
      ret.items[i] = t.transform(items[i]);
    }
    return ret;
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      int index = 0;

      @Override
      public boolean hasNext() {
        return index < items.length;
      }

      @Override
      public T next() {
        return items[index++];
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  public boolean contains(T v) {
    for(T i : items) {
      if(i == v) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    b.append('{');
    if(items.length > 0) {
      b.append(items[0]);
    }
    for(int i = 1; i < items.length; i++) {
      b.append(", ");
      b.append(items[i]);
    }
    b.append('}');
    return b.toString();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object obj) {
    return obj instanceof Vector &&
        Arrays.equals(items, ((Vector<T>)obj).items);
  }
  
  @Override
  public int hashCode() {
    int hash = 0xe56de916;
    for(T t : items) {
      hash += t.hashCode();
      hash *= 17;
    }
    return hash;
  }

  @SuppressWarnings("unchecked")
  public static <T> Vector<T> generate(int count, Generator<T> generator) {
    Vector<T> ret = new Vector<T>();
    ret.items = (T[])new Object[count];
    for(int i = 0; i < count; i++) {
      ret.items[i] = generator.generate(i);
    }
    return ret;
  }

  public Vector<T> concat(Vector<T> other) {
    Vector<T> ret = new Vector<T>();
    ret.items = Arrays.copyOf(items, items.length + other.items.length);
    for(int i = 0; i < other.items.length; i++) {
      ret.items[i + items.length] = other.items[i];
    }
    return ret;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static final Vector EMPTY = new Vector(new Object[0]);

  @SuppressWarnings("unchecked")
  public static <T> Vector<T> empty() {
    return EMPTY;
  }

  @SuppressWarnings("unchecked")
  public static <T> Vector<T> flatten(Vector<Vector<T>> group) {
    Vector<T> res = new Vector<T>();
    ArrayList<T> list = new ArrayList<T>();
    for(Vector<T> vec : group) {
      for(T t : vec) {
        list.add(t);
      }
    }
    res.items = (T[])list.toArray();
    return res;
  }

  @SuppressWarnings("unchecked")
  public static <T> Vector<Vector<T>> group(Vector<T> flat, int size) {
    Vector<Vector<T>> res = new Vector<Vector<T>>();
    ArrayList<Vector<T>> list = new ArrayList<Vector<T>>();
    T[] items = (T[])new Object[size];
    int i = 0;
    while(i < flat.size()) {
      for(int j = 0; j < size; j++, i++) {
        items[j] = flat.get(i);
      }
      list.add(new Vector<T>(items)); // the constructor clones the array
    }
    res.items = (Vector<T>[])list.toArray();
    return res;
  }

  public Vector<T> append(T item) {
    Vector<T> ret = new Vector<T>();
    ret.items = Arrays.copyOf(items, items.length + 1);
    ret.items[items.length] = item;
    return ret;
  }

  public static <T> Vector<T> unsafe(T[] items) {
    Vector<T> ret = new Vector<T>();
    ret.items = items;
    return ret;
  }

}
