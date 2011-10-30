package mathray.domain;

public interface Node {
  // negative axis for the negative direction
  public void split(int axis);
  
  public Node extend(int axis);
}