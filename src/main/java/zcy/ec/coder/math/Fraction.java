package zcy.ec.coder.math;

public class Fraction {
  // 分子
  int numerator;
  // 分母
  int denominator;

  public Fraction(int numerator, int denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
    this.cancelling();
  }

  public static Fraction create(int numerator, int denominator) {
    return new Fraction(numerator, denominator);
  }

  public static Fraction create(int numerator) {
    return new Fraction(numerator, 1);
  }

  public boolean isNan() {
    return denominator == 0;
  }

  public boolean isZero() {
    return numerator == 0;
  }

  private void cancelling() {
    if (isNan()) {
      return;
    } else if (numerator == 0) {
      this.denominator = 1;
      return;
    }
    int g = gcd(this.denominator, this.numerator);
    this.denominator = this.denominator / g;
    this.numerator = this.numerator / g;
    if (this.denominator < 0) {
      this.denominator = -this.denominator;
      this.numerator = -this.numerator;
    }
  }

  public static int gcd(int a, int b) {
    int min = Math.min(Math.abs(a), Math.abs(b));
    int max = Math.max(Math.abs(a), Math.abs(b));
    if (min == 0) {
      return max;
    }
    return gcd(min, max % min);
  }

  public static Fraction add(Fraction left, Fraction right) {
    return new Fraction(left.numerator * right.denominator + left.denominator * right.numerator,
        left.denominator * right.denominator);
  }

  public static Fraction sub(Fraction left, Fraction right) {
    return new Fraction(left.numerator * right.denominator - left.denominator * right.numerator,
        left.denominator * right.denominator);
  }

  public static Fraction mul(Fraction left, Fraction right) {
    return new Fraction(left.numerator * right.numerator, left.denominator * right.denominator);
  }

  public static Fraction div(Fraction left, Fraction right) {
    return new Fraction(left.numerator * right.denominator, left.denominator * right.numerator);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Fraction fraction = (Fraction) o;
    return numerator == fraction.numerator && denominator == fraction.denominator;
  }

  @Override
  public String toString() {
    return String.format("%d/%d", numerator, denominator);
  }
}
