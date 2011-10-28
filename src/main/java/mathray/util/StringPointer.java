package mathray.util;

public class StringPointer {
  private String text;
  private int pos;
  
  public int mark() {
    return pos;
  }
  
  public String substring(int mark) {
    return text.substring(mark, pos);
  }
  
  public StringPointer(String text) {
    this.text = text;
    this.pos = 0;
  }
  
  public void next() {
    if(pos < text.length()) {
      pos++;
    }
  }
  
  public char cur() {
    return text.charAt(pos);
  }
  
  public void ws() {
    while(Character.isWhitespace(cur())) {
      next();
    }
  }
  
  public boolean match(String t) {
    int p = 0;
    int po = pos;
    while(t.charAt(p) == text.charAt(po)) {
      p++;
      po++;
    }
    if(p == t.length()) {
      pos = po;
      return true;
    }
    return false;
  }

  public void expect(String t) {
    if(!match(t)) {
      throw new RuntimeException("parse error");
    }
  }

  public void expectEOS() {
    if(pos != text.length()) {
      throw new RuntimeException("parse error");
    }
  }
}
