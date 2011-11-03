package mathray.domain;

public abstract class Node {
  
  private Node parent;
  
  private double[] lowLimits;
  private double[] highLimits;
  
  private int splitAxis;
  
  private Node child1;
  private Node child2;
  
  private boolean maybeComplex;
  private boolean definitelyComplex;
  
  // negative axis for the negative direction
  public void split(int axis) {
    if(child1 != null) {
      throw new IllegalStateException("node already split");
    }
    splitAxis = axis;
    child1 = makeChild();
    child2 = makeChild();
  }
  
  private Node makeChild() {
    Node ret = child();
    ret.definitelyComplex = definitelyComplex;
    ret.maybeComplex = (definitelyComplex || maybeComplex) && !ret.definitelyNotComplex();
    ret.parent = this;
    return ret;
  }
  
  private boolean definitelyNotComplex() {
    // TODO: use inclusion arithmetic to compute whether complex values can ever crop up.
    return false;
  }
  
  protected abstract Node child();
  
  public Node extend(int axis) {
    return null;
  }
}