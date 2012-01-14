package mathray.util;

public class Stopwatch {
  
  public final long start;
  
  public Stopwatch() {
    start = System.currentTimeMillis(); 
  }
  
  public long time() {
    return System.currentTimeMillis() - start;
  }
  
}
