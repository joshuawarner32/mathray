
public class Timer {
  
  private Timer() {}
  
  private static final Runnable TrivialTask = new Runnable() {
    public void run() {}
  };
  
  private static long simpleTime(Runnable r, int samples) {
    long start = System.currentTimeMillis();
    for(int i = 0; i < samples; i++) {
      r.run();
    }
    long end = System.currentTimeMillis();
    return end - start;
  }
  
  public static double time(Runnable r, int samples) {
    return (simpleTime(r, samples) - simpleTime(TrivialTask, samples)) / (1000.0 * samples);
  }

}
