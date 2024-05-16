package zcy.ec.coder.operator;

public abstract class Computation<T> {

  public abstract T add(T left, T right);

  // additive inverse
  public abstract T addInv(T value);

  public abstract T mul(T left, T right);

  // multiplicative inverse
  public abstract T mulInv(T value);

  public abstract T zero();

  // unit element
  public abstract T unit();

  public abstract boolean isZero(T value);

  public abstract boolean isUnit(T value);

  public abstract boolean isNan(T value);

}
