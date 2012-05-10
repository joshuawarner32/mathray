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
    if(format.xa == null) {
      if(format.xb == null) {
        format.xa = -10d;
        format.xb = 10d;
      } else {
        format.xa = format.xb - 10;
      }
    } else {
      if(format.xb == null) {
        format.xb = format.xa + 10;
      }
    }
  }
  
  public static Rectangle pickFrame(Definition def, Format format) {
    if(format.ya != null && format.yb != null) {
      return new Rectangle(format.xa, format.xb, format.ya, format.yb);
    } else {
      return Frame.frameFor(def, format.xa, format.xb);
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
