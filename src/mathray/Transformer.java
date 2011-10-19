package mathray;

public interface Transformer<I, O> {
  
  public O transform(I in);

}
