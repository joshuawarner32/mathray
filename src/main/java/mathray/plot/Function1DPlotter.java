package mathray.plot;

import mathray.Definition;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaDevice;

public class Function1DPlotter implements Plotter {
  
  private static void pickFormat(Format format) {
    if(format.width == null) {
      format.width = 512;
    }
    if(format.height == null) {
      format.height = 512;
    }
    if(format.xRange == null) {
      format.xRange = new Range(-10, 10);
    }
  }
  
  public static Rectangle pickFrame(Definition def, Format format) {
    if(format.yRange != null) {
      return new Rectangle(format.xRange, format.yRange);
    } else {
      return Frame.frameFor(def, format.xRange);
    }
  }

  @Override
  public Output plot(Definition def, Format format) {
    pickFormat(format);
    
    Rectangle frame = pickFrame(def, format);
    
    FunctionTypes.D1_1 f = JavaDevice.compile(JavaDevice.D1_1, def.toMultidef());
    Graph2D graph = Plots.graphPlot(f, frame, format.width / 2);
    return new GraphOutput(def.toString(), graph, format);
  }

}
