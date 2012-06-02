package mathray.flow;

import mathray.Multidef;

public class Kernel extends Flow {

  public Kernel(int dimensions, Multidef kernel) {
    super(new Pins(kernel.args.size(), dimensions), new Pins(kernel.value.size(), dimensions));
  }

}
