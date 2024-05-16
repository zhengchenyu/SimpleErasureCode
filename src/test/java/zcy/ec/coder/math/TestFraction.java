package zcy.ec.coder.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import zcy.ec.coder.math.Fraction;

public class TestFraction {

  @Test
  public void testGCD() {
    assertEquals(2, Fraction.gcd(2, 2));
    assertEquals(1, Fraction.gcd(1, 2));
    assertEquals(6, Fraction.gcd(12, 18));
    assertEquals(18, Fraction.gcd(0, 18));
    assertEquals(6, Fraction.gcd(-12, -18));
    assertEquals(7, Fraction.gcd(-21, 56));
  }

  @Test
  public void testFraction() {
    assertEquals("1/3", new Fraction(3, 9).toString());
    assertEquals("29/35", Fraction.add(new Fraction(2, 5), new Fraction(3, 7)).toString());
    assertEquals("-1/35", Fraction.add(new Fraction(2, 5), new Fraction(3, -7)).toString());
    assertEquals("9/25", Fraction.sub(new Fraction(3, 5), new Fraction(24, 100)).toString());
    assertEquals("21/25", Fraction.sub(new Fraction(3, 5), new Fraction(24, -100)).toString());
    assertEquals("11/14", Fraction.mul(new Fraction(4, 7), new Fraction(11, 8)).toString());
    assertEquals("10/27", Fraction.div(new Fraction(4, 9), new Fraction(18, 15)).toString());
    assertEquals("-11/14", Fraction.mul(new Fraction(4, -7), new Fraction(11, 8)).toString());
    assertEquals("-10/27", Fraction.div(new Fraction(4, 9), new Fraction(18, -15)).toString());
  }
}
