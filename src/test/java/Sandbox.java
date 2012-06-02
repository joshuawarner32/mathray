import static mathray.Expressions.*;
import static mathray.flow.FlowExpressions.*;

import mathray.Symbol;
import mathray.flow.Flow;
import mathray.flow.Index;

import org.junit.Ignore;
import org.junit.Test;

public class Sandbox {
  
  @Test
  public void testEmpty() {
    
  }
  
  @Ignore
  void testFlow() {
    Symbol x = sym("x");
    Symbol y = sym("y");
    
    Index i = index("i");
    
    
    Flow f = 
      input(1, args(x))
        .output();
//        .select(i, i.get(x), i.next().get(x))
//        .kernel(args(x, y), add(sqrt(x), y), x)
//        .select(i, i.get(x), i.next().get(x))
//        .output();
  }

}




