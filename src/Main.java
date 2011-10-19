
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mathray.Definition;
import mathray.Variable;
import mathray.eval.precision.Intervals;
import mathray.plot.Plotter;

import static mathray.Expressions.*;

public class Main {
  
  public static void plot(Definition def, double min, double max, int width, int height, String file) throws IOException {
    
    BufferedImage image = Plotter.simplePlot(def, min, max, width, height);
    File outputfile = new File(file);
    ImageIO.write(image, "png", outputfile);
  }
  
  public static void intervalPlot(Definition def, double min, double max, int intervals, int width, int height, String file) throws IOException {
    
    BufferedImage image = Plotter.intervalPlot(def, min, max, intervals, width, height);
    File outputfile = new File(file);
    ImageIO.write(image, "png", outputfile);
  }

  public static void main(String[] args) {
    Variable x = var("x");
    try {
      plot(def(args(x), div(sin(x), x)), -4 * Math.PI, 4 * Math.PI, 512, 128, "sin_x___over_x.png");
      plot(def(args(x), sin(div(num(1), x))), -4 * Math.PI, 4 * Math.PI, 512, 128, "sin_1_over_x.png");
      plot(def(args(x), pow(x, num(2))), -2, 2, 512, 512, "x_squared.png");
      intervalPlot(Intervals.intervalize(def(args(x), mul(x, x)), vector(x)), -2, 2, 10, 512, 512, "x_times_x_interval.png");
      intervalPlot(Intervals.intervalize(def(args(x), div(num(1), x)), vector(x)), -2, 2, 10, 512, 512, "1_over_x_interval.png");
    } catch (IOException e) {
      e.printStackTrace(System.err);
    }

  }

}
