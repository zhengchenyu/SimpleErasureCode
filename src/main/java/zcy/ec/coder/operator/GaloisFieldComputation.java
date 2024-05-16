package zcy.ec.coder.operator;

import java.util.HashSet;
import java.util.Set;

// Use GF(2^n)
// 不可约多项式(Irreducible polynomial): x^8+x^4+x^3+x+1
public class GaloisFieldComputation extends Computation<Byte> {

  // Irreducible polynomial coefficient for _mul
  private final static byte IPC = 0b00011011;

  // multiply table
  static byte[][] mulTable = new byte[256][256];
  // multiplicative inverse table
  static byte[] mulInverse = new byte[256];

  static {
    // Construct multiply table to save computing resource
    // See 《密码编码学与网络安全--原理与实现(第六版)》 Chapter 4.7.4 for more information
    for (int i = 0; i < 256; i++) {
      mulTable[i] = new byte[256];
    }
    for (int i = 0; i < 256; i++) {
      for (int j = 0; j <= i; j++) {
        mulTable[i][j] = _mul(intToByte(i), intToByte(j));
        mulTable[j][i] = mulTable[i][j];
        if (mulTable[i][j] == 1) {
          mulInverse[i] = intToByte(j);
          mulInverse[j] = intToByte(i);
        }
      }
    }
    // Verify that all multiplicative inverses exist except 0.
    Set<Byte> set = new HashSet<>();
    for (int i = 0; i < 256; i++) {
      if (i != 0 && mulInverse[i] == 0) {
        throw new RuntimeException("The multiplicative inverse table is not set for " + i);
      }
      if (set.contains(i)) {
        throw new RuntimeException("Duplicate " + i);
      }
      set.add(intToByte(i));
    }
//    List<Byte> list = new ArrayList<>();
//    list.addAll(set);
//    Collections.sort(list);
//    for (int i = 0; i < list.size(); i++) {
//      System.out.println("list[" + i + "] = " + list.get(i));
//    }
  }

  // _mul is the real multiply operation
  public static byte _mul(Byte left, Byte right) {
    // 1 Count the '1' in right
    byte[] bits = new byte[8];
    for (int i = 0; i < 8; i++) {
      if (((1 << i) & right) != 0) {
        bits[i] = 1;
      }
    }
    byte ret = 0;
    for (int i = 0; i < 8; i ++) {
      if (bits[i] == 1) {
        byte tmp = fxxn(left, i);
        ret = intToByte(ret ^ tmp);
      }
    }
    return ret;
  }

  // _fxx is a function that computes x^n*f(x). Here value is the coefficient of f(x)
  public static byte fxxn(byte value, int n) {
    if (n == 0) {
      return value;
    } else if (n == 1) {
      return _fxx(value);
    }
    return _fxx(fxxn(value, n - 1));
  }

  // _fxx is a function that computes x*f(x). Here value is the coefficient of f(x)
  public static byte _fxx(byte value) {
    byte tmp = intToByte(value << 1);
    if (((1 << 7) & value) == 0) {
      return tmp;
    } else {
      return (byte)(tmp ^ IPC);
    }
  }

  public static byte intToByte(int value) {
    return (byte) (value & 0xFF);
  }

  public static int byteToInt(byte value) {
    return value & 0xFF;
  }

  @Override
  public Byte add(Byte left, Byte right) {
    return intToByte(left ^ right);
  }

  @Override
  public Byte addInv(Byte value) {
    return value;
  }

  @Override
  public Byte mul(Byte left, Byte right) {
    return mulTable[byteToInt(left)][byteToInt(right)];
  }

  @Override
  public Byte mulInv(Byte value) {
    return mulInverse[byteToInt(value)];
  }

  @Override
  public Byte zero() {
    return 0;
  }

  @Override
  public Byte unit() {
    return 1;
  }

  @Override
  public boolean isZero(Byte value) {
    return value == 0;
  }

  @Override
  public boolean isUnit(Byte value) {
    return value == 1;
  }

  @Override
  public boolean isNan(Byte value) {
    return false;
  }
}
