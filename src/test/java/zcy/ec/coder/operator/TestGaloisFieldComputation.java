package zcy.ec.coder.operator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import org.junit.jupiter.api.Test;
import zcy.ec.coder.operator.GaloisFieldComputation;

public class TestGaloisFieldComputation {
  @Test
  public void test() {
    Random random = new Random();
    GaloisFieldComputation computation = new GaloisFieldComputation();
    for (int i = 0; i < 100; i++) {
      byte a = GaloisFieldComputation.intToByte(random.nextInt());
      byte b = GaloisFieldComputation.intToByte(random.nextInt());
      assertEquals(a, computation.add(computation.add(a, b), computation.addInv(b)));
      if (b != 0) {
        assertEquals(a, computation.mul(computation.mul(a, b), computation.mulInv(b)));
      }
    }
    // Example in Chapter 4.7.4
    byte c = GaloisFieldComputation.intToByte(0b01010111);
    byte d = GaloisFieldComputation.intToByte(0b10000011);
    assertEquals(GaloisFieldComputation.intToByte(0b11000001), GaloisFieldComputation._mul(c, d));
    assertEquals(GaloisFieldComputation.intToByte(0b11000001), GaloisFieldComputation._mul(d, c));
  }
}
