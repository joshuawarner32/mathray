package mathray.plot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import mathray.Definition;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaCompiler;

public class BasicFunctionPlotter implements Plotter {

  @Override
  public Output plot(Definition def) {
    BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    g.setBackground(Color.WHITE);
    g.clearRect(0, 0, 512, 512);
    FunctionTypes.D1_1 f = JavaCompiler.compile(JavaCompiler.D1_1, def.toMultidef());
    Plots.simplePlot(f, Frame.frameFor(def, -10, 10), image, Color.BLACK);
    return new ImageOutput(def.toString(), image);
  }

}
