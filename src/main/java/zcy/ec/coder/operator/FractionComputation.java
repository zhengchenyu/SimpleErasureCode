package zcy.ec.coder.operator;

import zcy.ec.coder.math.Fraction;

public class FractionComputation extends Computation<Fraction> {

  public static final Fraction ZERO = new Fraction(0, 1);
  public static final Fraction ONE = new Fraction(1, 1);

  @Override
  public Fraction add(Fraction left, Fraction right) {
    return Fraction.add(left, right);
  }

  public Fraction addInv(Fraction value) {
    return Fraction.sub(ZERO, value);
  }

  @Override
  public Fraction mul(Fraction left, Fraction right) {
    return Fraction.mul(left, right);
  }

  public Fraction mulInv(Fraction value) {
    return Fraction.div(ONE, value);
  }

  @Override
  public Fraction zero() {
    return ZERO;
  }

  @Override
  public Fraction unit() {
    return ONE;
  }

  @Override
  public boolean isZero(Fraction value) {
    return value.equals(ZERO);
  }

  @Override
  public boolean isUnit(Fraction value) {
    return value.equals(ONE);
  }

  @Override
  public boolean isNan(Fraction value) {
    return value.isNan();
  }
}
