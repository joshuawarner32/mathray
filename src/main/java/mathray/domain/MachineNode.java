package mathray.domain;

class MachineNode implements Node {
  
  private Node parent;
  
  private double[] lowLimits;
  private double[] highLimits;
  
  private int splitAxis;
  
  private Node child1;
  private Node child2;

  @Override
  public void split(int axis) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Node extend(int axis) {
    // TODO Auto-generated method stub
    return null;
  }

}
