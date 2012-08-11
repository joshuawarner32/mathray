package mathray.plot;

import java.awt.image.BufferedImage;

import mathray.Definition;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaDevice;
import mathray.eval.split.IntervalTransform;
import mathray.util.ImageUtil;

public class Equation2DPlotter implements Plotter {
  
  private void pickFormat(Format format) {
    if(format.width == null) {
      format.width = 512;
    }
    if(format.height == null) {
      format.height = 512;
    }
    if(format.xRange == null) {
      format.xRange = new Range(-10, 10);
    }
    if(format.yRange == null) {
      format.yRange = new Range(-10, 10);
    }
  }

  @Override
  public Output plot(Definition def, Format format) {
    pickFormat(format);
    
    Rectangle rect = new Rectangle(format.xRange, format.yRange);
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
    int[] data = new int[w * h];
    rf.repeat(in, out);
    
    for(int y = 0; y < h; y++) {
      for(int x = 0; x < w; x++) {
        int i = 2 * (y * w + x);
        double a = out[i + 0];
        double b = out[i + 1];
        if(a * b <= 0) {
          data[x + (h - y - 1) * w] = 0x000000;
        } else {
          data[x + (h - y - 1) * w] = 0xffffff;
        }
      }
    }
    BufferedImage ret = ImageUtil.imageFromArray(data, w, h);
    return new ImageOutput(def.value.toString() + " = 0", ret);
  }

}
