package mathray.eval.text;
    
public class PrecedenceString {
  public final String text;
  public final int precedence;
  
  public PrecedenceString(String text, int precedence) {
    this.text = text;
    this.precedence = precedence;
  }
  
  public String toString(int outerPrec) {
    if(outerPrec > precedence) {
      return "(" + text + ")";
    }
    return text;
  }
  
  public String toString() {
    return text;
  }
}
