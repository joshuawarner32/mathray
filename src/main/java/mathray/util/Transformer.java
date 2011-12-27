package mathray.util;

public interface Transformer<I, O> {
  
  public O transform(I in);

}
