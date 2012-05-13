import mathray.Symbol;
import mathray.search.Summary;

import static mathray.Expressions.*;
import org.junit.Test;


public class TestSummary {
  
  private Symbol x = sym("x");
  
  @Test
  public void testCircleSummary() {
    Summary summary = Summary.of(def(args(x), pow(x, 2)));
    
    summary.find(0);
  }

}
