package mathray.eval.java;

import java.util.HashMap;
import java.util.Map;

import mathray.Computation;
import mathray.InternalVisitor;
import mathray.Value;

public class Usage {

  private final Map<Value, Integer> useCounts = new HashMap<Value, Integer>();
  
  private Usage() {}
  
  public static Usage generate(Computation comp) {
    Usage ret = new Usage();
    
    return ret;
  }

}
