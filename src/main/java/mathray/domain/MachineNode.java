package mathray.domain;

class MachineNode extends Node {

  @Override
  protected Node child() {
    return new MachineNode();
  }

}
