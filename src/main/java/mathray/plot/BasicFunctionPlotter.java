package mathray.plot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import mathray.Definition;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaDevice;

public class BasicFunctionPlotter implements Plotter {
  
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
    
    BufferedImage image = new BufferedImage(format.width, format.height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    g.setBackground(Color.WHITE);
    g.clearRect(0, 0, 512, 512);
    FunctionTypes.D1_1 f = JavaDevice.compile(JavaDevice.D1_1, def.toMultidef());
    Plots.simplePlot(f, Frame.frameFor(def, -10, 10), image, Color.BLACK);
    return new ImageOutput(def.toString(), image);
  }

}
