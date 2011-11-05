package mathray.domain;

class MachineNode extends Node {
  
  private double[] lowLimits;
  private double[] highLimits;

  @Override
  protected Node child() {
    return new MachineNode();
  }

}
