package mathray.plot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import mathray.Definition;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaDevice;
import mathray.eval.split.IntervalTransform;

public class Equation2DPlotter implements Plotter {
  
  private void pickFormat(Format format) {
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
    if(format.ya == null) {
      if(format.yb == null) {
        format.ya = -10d;
        format.yb = 10d;
      } else {
        format.ya = format.yb - 10;
      }
    } else {
      if(format.yb == null) {
        format.yb = format.ya + 10;
      }
    }
  }

  @Override
  public Output plot(Definition def, Format format) {
    pickFormat(format);
    
    Rectangle rect = new Rectangle(format.xa, format.xb, format.ya, format.yb);
    FunctionTypes.D f = JavaDevice.compile(IntervalTransform.intervalize(def.toMultidef(), def.args));
    FunctionTypes.RepeatD rf = FunctionTypes.toRepeatD(f);
    int w = format.width;
    int h = format.height;
    
    double[] in = new double[4 * w * h];
    
    for(int y = 0; y < h; y++) {
      for(int x = 0; x < w; x++) {
        int i = 4 * (y * w + x);
        in[i + 0] = x * rect.width() / (double)w + rect.xa;
        in[i + 1] = (x + 1) * rect.width() / (double)w + rect.xa;
        in[i + 2] = y * rect.height() / (double)h + rect.ya;
        in[i + 3] = (y + 1) * rect.height() / (double)h + rect.ya;
      }
    }
    
    double[] out = new double[2 * w * h];
    rf.repeat(in, out);
    
    BufferedImage ret = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D)ret.getGraphics();
    g.setBackground(Color.WHITE);
    g.clearRect(0, 0, w, h);
    
    g.setPaint(Color.BLACK);
    
    for(int y = 0; y < h; y++) {
      for(int x = 0; x < w; x++) {
        int i = 2 * (y * w + x);
        double a = out[i + 0];
        double b = out[i + 1];
        if(a * b <= 0) {
          g.fillRect(x, h - y, 1, 1);
        }
      }
    }
    
    return new ImageOutput(def.value.toString() + " = 0", ret);
  }

}
