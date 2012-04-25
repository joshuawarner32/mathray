package mathray.plot;

import mathray.Definition;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaDevice;

public class Function1DPlotter implements Plotter {
  
  private void pickFormat(Format format) {
    if(format.width == null) {
      format.width = 512;
    }
    if(format.height == null) {
      format.height = 512;
    }
  }

  @Override
  public Output plot(Definition def, Format format) {
    pickFormat(format);
    
    FunctionTypes.D1_1 f = JavaDevice.compile(JavaDevice.D1_1, def.toMultidef());
    Graph2D graph = Plots.graphPlot(f, Frame.frameFor(def, -10, 10), format.width / 2);
    return new GraphOutput(def.toString(), graph, format);
  }

}
