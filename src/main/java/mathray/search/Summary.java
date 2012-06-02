package mathray.search;

import java.util.ArrayList;
import java.util.List;

import mathray.Definition;
import mathray.eval.java.JavaDevice;

public class Summary {
  
  private Definition def;
  private SummaryNode root;
  
  private Summary(Definition def) {
    this.def = def;
    root = new SummaryNode(JavaDevice.compile(def.toMultidef()));
  }

  public static Summary of(Definition def) {
    return new Summary(def);
  }

  public void find(double value, double granularity) {
    List<SummaryNode> results = new ArrayList<SummaryNode>();
    root.find(value, granularity, results);
  }

}
