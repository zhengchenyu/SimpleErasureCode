package zcy.ec.coder.operator;

public class DefaultComputation extends Computation<Double> {

  @Override
  public Double add(Double left, Double right) {
    return left + right;
  }

  public Double addInv(Double value) {
    return - value;
  }

  @Override
  public Double mul(Double left, Double right) {
    return left * right;
  }

  public Double mulInv(Double value) {
    if (isZero(value)) {
      return Double.NaN;
    }
    return 1 / value;
  }

  @Override
  public Double zero() {
    return 0.0d;
  }

  @Override
  public Double unit() {
    return 1.0d;
  }

  @Override
  public boolean isZero(Double d) {
    return Double.compare(d, 0.0) == 0;
  }

  @Override
  public boolean isUnit(Double d) {
    return Double.compare(d, 1.0) == 0;
  }

  @Override
  public boolean isNan(Double value) {
    return value.isNaN();
  }
}
