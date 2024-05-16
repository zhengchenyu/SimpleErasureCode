package zcy.ec.coder;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Random;
import org.junit.jupiter.api.Test;

public class TestErasureCoder {

  private static final int CHUNCK = 1024;

  @Test
  public void test() {
    Random random = new Random();
    ErasureCoder coder = new ErasureCoder();
    byte[][] data = new byte[6][CHUNCK];
    byte[] tmpBytes = new byte[CHUNCK * 6];
    random.nextBytes(tmpBytes);
    for (int i = 0; i < 6; i++) {
      System.arraycopy(tmpBytes, i * CHUNCK, data[i], 0, CHUNCK);
    }

    // 1 Encode
    byte[][] parity = new byte[3][CHUNCK];
    coder.encode(data, parity);

    // 2 Compose the complete data
    byte[][] all = new byte[9][CHUNCK];
    for (int i = 0; i < 6; i++) {
      System.arraycopy(data[i], 0, all[i], 0, CHUNCK);
    }
    for (int i = 0; i < 3; i++) {
      System.arraycopy(parity[i], 0, all[i + 6], 0, CHUNCK);
    }

    // 3 Decode
    byte[][] backup = new byte[6][CHUNCK];
    for (int i = 0; i < 6; i++) {
      for (int j = i; j < 6; j++) {
        for (int k = j; k < 6; k++) {
          int[] erasedIndexes;
          if (i == j && j == k) {
            erasedIndexes = new int[]{i};
            backup[0] = all[i];
            all[i] = null;
          } else if (i == j) {
            erasedIndexes = new int[]{i, k};
            backup[0] = all[i];
            backup[1] = all[k];
            all[i] = null;
            all[k] = null;
          } else if ((i == k) || ((j == k))) {
            erasedIndexes = new int[]{i, j};
            backup[0] = all[i];
            backup[1] = all[j];
            all[i] = null;
            all[j] = null;
          } else {
            erasedIndexes = new int[]{i, j, k};
            backup[0] = all[i];
            backup[1] = all[j];
            backup[2] = all[k];
            all[i] = null;
            all[j] = null;
            all[k] = null;
          }
          byte[][] decoded = new byte[erasedIndexes.length][CHUNCK];
          coder.decode(all, erasedIndexes, decoded);
          for (int l = 0; l < erasedIndexes.length; l++) {
            assertArrayEquals(backup[l], decoded[l]);
            all[erasedIndexes[l]] = backup[l];
          }
        }
      }
    }
  }
}
