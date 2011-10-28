package mathray.eval.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Token {
  
  public enum Type {
    Identifier,
    Number,
    Symbol
  }
  
  public final Type type;
  public final String text;
  
  public Token(Type type, String text) {
    this.type = type;
    this.text = text;
  }
  
  public static boolean isSymbol(int ch) {
    switch(ch) {
    case '!':
    case '#':
    case '$':
    case '(':
    case ')':
    case '*':
    case '+':
    case '-':
    case '/':
    case ':':
    case ';':
    case '<':
    case '=':
    case '>':
    case '?':
    case '[':
    case ']':
    case '^':
    case '{':
    case '|':
    case '}':
    case '~':
      return true;
    default:
      return false;
    }
  }
  
  @Override
  public String toString() {
    return text;
  }
  
  public static List<Token> tokens(Set<String> symbols, String text) {
    List<Token> ret = new ArrayList<Token>();
    int pos = 0;
    while(pos < text.length()) {
      char ch = text.charAt(pos);
      if(Character.isWhitespace(ch)) {
        do {
          pos++;
        } while(pos < text.length() && Character.isWhitespace(text.charAt(pos)));
      } else if(Character.isLetter(ch)) {
        int start = pos;
        do {
          pos++;
        } while(pos < text.length() && Character.isLetter(text.charAt(pos)));
        ret.add(new Token(Type.Identifier, text.substring(start, pos)));
      } else if(Character.isDigit(ch)) {
        int start = pos;
        do {
          pos++;
        } while(pos < text.length() && Character.isDigit(text.charAt(pos)));
        if(pos < text.length() && text.charAt(pos) == '.') {
          pos++;
          while(pos < text.length() && Character.isDigit(text.charAt(pos))) {
            pos++;
          }
        }
        ret.add(new Token(Type.Number, text.substring(start, pos)));
      } else if(isSymbol(ch)) {
        int start = pos;
        do {
          pos++;
        } while(pos < text.length() && isSymbol(text.charAt(pos)));
        splitSymbols(symbols, ret, text.substring(start, pos));
      } else {
        throw new RuntimeException("parse error");
      }
    }
    return ret;
  }

  private static void splitSymbols(Set<String> symbols, List<Token> ret, String text) {
    int i;
    for(i = text.length() - 1; i > 0; i--) {
      String s = text.substring(0, i);
      if(symbols.contains(s)) {
        ret.add(new Token(Type.Symbol, s));
        break;
      }
    }
    String s = text.substring(i);
    if(text.length() - i == 1) {
      if(symbols.contains(s)) {
        ret.add(new Token(Type.Symbol, s));
        return;
      } else {
        throw new RuntimeException("unknown symbol '" + s + "'");
      }
    } else {
      splitSymbols(symbols, ret, s);
    }
  }

}
