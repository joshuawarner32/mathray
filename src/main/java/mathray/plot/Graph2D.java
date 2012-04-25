package mathray.plot;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public final class Graph2D {
  
  private final float[] verts; // two elements per vertex
  private final int[][] curves;
  private final Rectangle rect;
  
  private Graph2D(Rectangle validRect, float[] verts, int[][] curves) {
    this.rect = validRect;
    this.verts = verts;
    this.curves = curves;
  }
  
  public static final class Builder {
    private List<Float> verts = new ArrayList<Float>();
    private List<int[]> curves = new ArrayList<int[]>();
    private List<Integer> currentCurve = new ArrayList<Integer>();
    
    private Builder() {}
    
    public void point(double x, double y) {
      int index = verts.size();
      verts.add((float)x);
      verts.add((float)y);
      currentCurve.add(index / 2);
    }
    
    public void split() {
      if(currentCurve.size() > 0) {
        int[] arr = new int[currentCurve.size()];
        for(int i = 0; i < arr.length; i++) {
          arr[i] = currentCurve.get(i);
        }
        curves.add(arr);
        currentCurve = new ArrayList<Integer>();
      }
    }
    
    public Graph2D build(Rectangle validRect) {
      float[] realVerts = new float[verts.size()];
      for(int i = 0; i < realVerts.length; i++) {
        realVerts[i] = verts.get(i);
      }
      split();
      int[][] realCurves = new int[curves.size()][];
      for(int i = 0; i < realCurves.length; i++) {
        realCurves[i] = curves.get(i);
      }
      return new Graph2D(validRect, realVerts, realCurves);
    }
  }
  
  public static Builder builder() {
    return new Builder();
  }
  
  public void draw(Graphics2D g, float imagX, float imagY, float width, float height) {
    for(int[] curve : curves) {
      double xl = (verts[2 * curve[0]] - rect.xa) / rect.width() * width + imagX;
      double yl = (verts[2 * curve[0] + 1] - rect.ya) / rect.height() * height + imagY;
      for(int i = 1; i < curve.length; i++) {
        double x = (verts[2 * curve[i]] - rect.xa) / rect.width() * width + imagX;
        double y = (verts[2 * curve[i] + 1] - rect.ya) / rect.height() * height + imagY;
        g.draw(new Line2D.Double(xl, yl, x, y));
        xl = x;
        yl = y;
      }
    }
  }

}
