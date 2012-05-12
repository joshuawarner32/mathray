package mathray.search;

import java.util.List;

import mathray.device.FunctionTypes;

public class SummaryNode {
  
  private SummaryNode[] children;
  
  private final FunctionTypes.D func;
  
  double[] mins;
  double[] maxs;
  
  double minValue;
  double maxValue;
  
  public SummaryNode(FunctionTypes.D func) {
    this.func = func;
    // TODO: set minValue, maxValue
  }
  
  private boolean shouldSplit(double granularity) {
    return false;
  }
  
  private void split() {
    if(children == null) {
      children = new SummaryNode[1 << func.getInputArity()];
      // TODO: recompute accurate mins and maxs up the chain
      for(int i = 0; i < 1 << func.getInputArity(); i++) {
        children[i] = new SummaryNode(func);
      }
    }
  }
  
  public void find(double value, double granularity, List<SummaryNode> results) {
    if(minValue <= value && maxValue >= value) {
      results.add(this);
      if(shouldSplit(granularity)) {
        split();
        for(SummaryNode node : children) {
          node.find(value, granularity, results);
        }
      }
    }
  }

}
