package mathray.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.Preconditions;

public final class Vector<T> implements Collection<T> {
  
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
      ret.items[i] = Preconditions.checkNotNull(t.transform(items[i]));
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

  public boolean contains(Object o) {
    for(T i : items) {
      if(i == o) {
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
      if(ret.items[i] == null) {
        throw new NullPointerException();
      }
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

  @SuppressWarnings("unchecked")
  public static <T> Vector<T> fromIterable(Iterable<T> it) {
    ArrayList<T> ret = new ArrayList<T>();
    for(T t : it) {
      ret.add(t);
    }
    return Vector.unsafe(ret.toArray((T[])new Object[ret.size()]));
  }

  @Override
  public boolean isEmpty() {
    return items.length > 0;
  }

  @Override
  public Object[] toArray() {
    return Arrays.copyOf(items, items.length);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <K> K[] toArray(K[] a) {
    if(a.length < items.length) {
      a = Arrays.copyOf(a, items.length);
    } else if(a.length > items.length) {
      a[items.length] = null;
    }
    for(int i = 0; i < items.length; i++) {
      a[i] = (K)items[i];
    }
    return a;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    for(Object o : c) {
      if(!contains(o)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean add(T e) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

}
