package mathray;

import mathray.visitor.Visitor;

public class Rational extends Value {
  public final long num;
  public final long denom;
  
  
  private Rational(long num, long denom) {
    this.num = num;
    this.denom = denom;
    if(denom == 0) {
      throw new ArithmeticException("constructing Rational with 0 denominator");
    }
  }
  
  public static Rational get(long value) {
    return get(value, 1);
  }
  
  public static Rational get(long num, long denom) {
    if(denom < 0) {
      num = -num;
      denom = -denom;
    }
    long fact = gcd(Math.abs(num), denom);
    return new Rational(num / fact, denom / fact);
  }
  
  private static long gcd(long a, long b) {
    while(b != 0) {
      long tmp = a;
      a = b;
      b = tmp % b;
    }
    return a;
  } 


  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }
  
  @Override
  public int hashCode() {
    return ((int)num) + ((int)denom * 3);
  }
  
  @Override
  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    }
    if(obj instanceof Rational) {
      Rational r = (Rational)obj;
      return num == r.num && denom == r.denom;
    }
    return false;
  }
  
  @Override
  public int compareTo(Value v) {
    if(this == v) {
      return 0;
    }
    if(v instanceof Rational) {
      Rational r = (Rational)v;
      long p1 = num * r.denom;
      long p2 = denom * r.num;
      if(p1 > p2) {
        return 1;
      } else if(p1 < p2) {
        return -1;
      } else {
        return 0;
      }
    } else { // Variable or Call
      return -1;
    }
  }
  
  public Rational add(Rational r) {
    return get(num * r.denom + r.num * denom, denom * r.denom);
  }

  public Rational mul(Rational r) {
    return get(num * r.num, denom * r.denom);
  }
  
  public Rational div(Rational r) {
    return get(num * r.denom, denom * r.num);
  }
  
  public Rational negative() {
    // no point re-reducing it...
    return new Rational(-num, denom);
  }

  public Rational reciprocal() {
    // no point re-reducing it...
    if(num >= 0) {
      return new Rational(denom, num);
    } else {
      return new Rational(-denom, -num);
    }
  }

  public boolean isNegative() {
    return num < 0;
  }

  public boolean isEven() {
    return denom == 1 && ((num & 1) == 0);
  }
  
  public boolean isOdd() {
    return !isEven();
  }
  
  public boolean isInteger() {
    return denom == 1;
  }
  
  public Rational numerator() {
    return new Rational(num, 1);
  }
  
  public Rational denominator() {
    return new Rational(denom, 1);
  }

  public double toDouble() {
    return num / (double)denom;
  }

}
