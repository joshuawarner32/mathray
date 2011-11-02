package mathray.plot;

import java.util.ArrayList;
import java.util.List;

public final class Graph2D {
  
  private final float[] verts; // two elements per vertex
  //private final int[] segments;
  private final int[][] curves; 
  //private final int[] linearCurves; // three elements per segment: start, step, length - 1
  
  private Graph2D(float[] verts, int[][] curves) {
    this.verts = verts;
    this.curves = curves;
  }
  
  public static final class Builder {
    private List<Float> verts = new ArrayList<Float>();
    private List<int[]> curves = new ArrayList<int[]>();
    private List<Integer> currentCurve = new ArrayList<Integer>();
    
    private Builder() {}
    
    public void point(float x, float y) {
      int index = verts.size();
      verts.add(x);
      verts.add(y);
      currentCurve.add(index / 2);
    }
    
    public void split() {
      if(currentCurve.size() > 0) {
        int[] arr = new int[currentCurve.size()];
        for(int i = 0; i < arr.length; i++) {
          arr[i] = currentCurve.get(i);
        }
        curves.add(arr);
      }
      currentCurve = new ArrayList<Integer>();
    }
    
    public Graph2D build() {
      float[] realVerts = new float[verts.size()];
      for(int i = 0; i < realVerts.length; i++) {
        realVerts[i] = verts.get(i);
      }
      split();
      int[][] realCurves = new int[curves.size()][];
      for(int i = 0; i < realCurves.length; i++) {
        realCurves[i] = curves.get(i);
      }
      return new Graph2D(realVerts, realCurves);
    }
  }
  
  public static Builder builder() {
    return new Builder();
  }

}
