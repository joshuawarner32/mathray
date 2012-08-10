package mathray.util;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ImageUtil {
  
  private ImageUtil() {}

  private static final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
  private static final ColorModel RGB_OPAQUE =
    new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
  
  public static BufferedImage imageFromArray(int[] data, int width, int height) {
    DataBuffer buf = new DataBufferInt(data, data.length);
    WritableRaster raster = Raster.createPackedRaster(buf, width, height, width, RGB_MASKS, null);
    BufferedImage outImage = new BufferedImage(RGB_OPAQUE, raster, false, null);
    return outImage;
  }

}
