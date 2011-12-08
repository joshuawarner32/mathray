package mathray.domain;

import mathray.Computation;

public class Domain {
  
  public final Computation comp;
  
  private Node rootNode;
  
  public Domain(Computation comp) {
    this.comp = comp;
  }

}
