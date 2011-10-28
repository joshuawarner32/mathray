
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mathray.Variable;
import mathray.eval.precision.Intervals;
import mathray.plot.Plotter;

import static mathray.Expressions.*;

public class Main {
  
  private static void saveImage(String file, BufferedImage image) throws IOException {
    ImageIO.write(image, "png", new File(file));
  }
  
  public static void main(String[] args) {
    Variable x = var("x");
    try {
      saveImage("sin_x___over_x.png", Plotter.simplePlot(def(args(x), div(sin(x), x)), (-4 * Math.PI), (4 * Math.PI), 512, 128));
      saveImage("sin_1_over_x.png", Plotter.simplePlot(def(args(x), sin(div(num(1), x))), (-4 * Math.PI), (4 * Math.PI), 512, 128));
      saveImage("x_squared.png", Plotter.simplePlot(def(args(x), pow(x, num(2))), (-2), 2, 512, 512));
      saveImage("x_times_x_interval.png", Plotter.intervalPlot(Intervals.intervalize(def(args(x), mul(x, x)), vector(x)), (-2), 2, 10, 512, 512));
      saveImage("1_over_x_interval.png", Plotter.intervalPlot(Intervals.intervalize(def(args(x), div(num(1), x)), vector(x)), (-2), 2, 11, 512, 512));
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }

  }

}
