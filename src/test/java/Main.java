import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import mathray.Args;
import mathray.Closure;
import mathray.Definition;
import mathray.Multidef;
import mathray.Symbol;
import mathray.concrete.CameraD3;
import mathray.concrete.VectorD3;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaDevice;
import mathray.eval.simplify.Simplifications;
import mathray.eval.split.IntervalTransform;
import mathray.eval.text.DefaultPrinter;
import mathray.eval.text.ParseException;
import mathray.eval.text.ParseInfo;
import mathray.eval.transform.Project;
import mathray.plot.Frame;
import mathray.plot.Plot3D;
import mathray.plot.Plots;
import mathray.random.ValueRandom;
import mathray.util.UI;
import mathray.util.Vector;
import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Main {

  public static void main(String[] args) {
    //printRandomExpressionForever();
    plot3DStuff();
  }
  
  private static Symbol x = sym("x");
  private static Symbol y = sym("y");
  private static Symbol z = sym("z");
  
  private static void plot3DStuff() {
    ParseInfo info = DefaultPrinter.BASIC_FUNCTIONS.toBuilder()
      .values(x, y, z)
      .build();
    //plot3D(def(args(x, y, z), add(mul(x, x), mul(y, y), mul(z, z), num(-1))), 512, 512);
    //plot3D(def(args(x, y, z), add(pow(x, 2), pow(y, 2), pow(z, 2), num(-1))), 512, 512);
    try {
      plot3D(def(args(x, y, z), info.parse("x^2+z^2-(1-y)*y^4")), 512, 512);
    } catch(ParseException e) {
      throw new RuntimeException(e);
    }
    //plot3D(def(args(x, y, z), info.parse("81*(x^3 + y^3 + z^3) - 189*(x^2*y + x^2*z + y^2*x + y^2*z + z^2*x + z^2*y) + 54*x*y*z + 126*(x*y + x*z + y*z) - 9*(x^2 + y^2 + z^2) - 9*(x + y + z) + 1")), 512, 512);
  }
  
  private static void plot3D(Definition def, int width, int height) {
    Closure<Multidef> proj = Project.project(def.toMultidef(), def.args); 
    Closure<Multidef> inter = proj.close(Simplifications.simplify(IntervalTransform.intervalize(proj.value, proj.value.args)));
    CameraD3 cam = CameraD3.lookAt(new VectorD3(3, 3, 3), new VectorD3(0, 0, 0), 1, width / (double)height, new VectorD3(0, 0, 1));
    FunctionTypes.ClosureD<FunctionTypes.ZeroInBlockD3> func = JavaDevice.compile(JavaDevice.closureD(JavaDevice.MAYBE_ZERO_IN_BLOCKD3), inter);
    BufferedImage image = Plot3D.plotBlockDepth(func.close(cam.args()), width, height, 0.001, 100);
    UI.show(def.toString(), image);
  }
  
  private static void process(Definition def) {
    System.out.println(def.toString());
    BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    g.setBackground(Color.WHITE);
    g.clearRect(0, 0, 512, 512);
    FunctionTypes.D1_1 f = JavaDevice.compile(JavaDevice.D1_1, def.toMultidef());
    g.setPaint(Color.BLACK);
    Plots.graphPlot(f, Frame.frameFor(def, -10, 10), 512).draw(g, 0, 0, 512, 512);
    UI.show(def.toString(), image);
  }

  public static void printRandomExpressionForever() {
    ValueRandom random = new ValueRandom(Vector.<Symbol>empty(), vector(ADD, SUB, MUL, DIV, SIN, SQRT, NEG));
    Symbol x = sym("x");
    Args a = args(x);
    for(Definition def : random.randomDefinitions(a, 0.99, 0.5, 0.2)) {
      process(def);
    }
  }
}
