package zcy.ec.coder;

import java.util.HashSet;
import java.util.Set;
import zcy.ec.coder.math.Matrix;
import zcy.ec.coder.operator.GaloisFieldComputation;

import static zcy.ec.coder.operator.GaloisFieldComputation.intToByte;

// In fact, there are a lot of unnecessary calculations in the code.
// Here in order to better understand the problem, do not do optimization.
public class ErasureCoder {

  private Matrix<Byte> encodeMatrix = new Matrix<Byte>(new Byte[][] {
      {intToByte(1), intToByte(0), intToByte(0), intToByte(0), intToByte(0), intToByte(0)},
      {intToByte(0), intToByte(1), intToByte(0), intToByte(0), intToByte(0), intToByte(0)},
      {intToByte(0), intToByte(0), intToByte(1), intToByte(0), intToByte(0), intToByte(0)},
      {intToByte(0), intToByte(0), intToByte(0), intToByte(1), intToByte(0), intToByte(0)},
      {intToByte(0), intToByte(0), intToByte(0), intToByte(0), intToByte(1), intToByte(0)},
      {intToByte(0), intToByte(0), intToByte(0), intToByte(0), intToByte(0), intToByte(1)},
      {intToByte(1), intToByte(1), intToByte(1), intToByte(1), intToByte(1), intToByte(1)},
      {intToByte(1), intToByte(2), intToByte(4), intToByte(8), intToByte(16), intToByte(32)},
      {intToByte(1), intToByte(3), intToByte(9), intToByte(27), intToByte(81), intToByte(243)},
  });

  private GaloisFieldComputation computation = new GaloisFieldComputation();

  // We fixed the data number to 6 and the number of parity to 3
  void encode(byte[][] inputs, byte[][] output) {
    // 0 Check
    if (inputs.length != 6) {
      throw new RuntimeException("The length of input data must be 6!");
    }
    if (output.length != 3) {
      throw new RuntimeException("The length of output data must be 3!");
    }
    int bytePerData = inputs[0].length;
    for (int i = 1; i < inputs.length; i++) {
      if (inputs[i].length != bytePerData) {
        throw new RuntimeException("The length of 6 data cannot be aligned!");
      }
    }
    for (int i = 0; i < 3; i++) {
      if (output[i] == null) {
        output[i] = new byte[bytePerData];
      }
    }

    // Step 1: dataMatrix multiply encodeMatrix, then get parity
    for (int i = 0; i < bytePerData; i++) {
      Byte[][] tmpData = new Byte[6][1];
      for (int j = 0; j < 6; j++) {
        tmpData[j] = new Byte[1];
        tmpData[j][0] = inputs[j][i];
      }
      Matrix<Byte> data = new Matrix<Byte>(tmpData);
      Matrix<Byte> out = Matrix.multiply(encodeMatrix, data, computation);
      for (int j = 0; j < 3; j++) {
        output[j][i] = out.get(j + 6, 0);
      }
    }
  }

  // We fixed the data number to 6 and the number of parity to 3
  void decode(byte[][] inputs, int[] erasure, byte[][] output) {
    // 0 Check
    if (inputs.length != 9) {
      throw new RuntimeException("The length of input data must be 9!");
    }
    for (int i = 0; i < erasure.length; i++) {
      if (erasure.length >= 6) {
        throw new RuntimeException("Erasure index must be less than or equal to 6."
            + " If you wanna recover parity, please decode firstly decode, then encode !");
      }
      if (i < erasure.length - 1 && erasure[i] >= erasure[i+1]) {
        throw new RuntimeException("Erasure must be monotonically increasing!");
      }
    }
    if (output.length != erasure.length) {
      throw new RuntimeException("The length of input data must be equals to erasure!");
    }
    int bytePerData = -1;
    int validInputs = 0;
    Set<Integer> erasured = new HashSet<>();
    for (int i = 0; i < erasure.length; i++) {
      erasured.add(erasure[i]);
    }
    Byte[][] tmpEncodeMatrixData = new Byte[6][6];
    int[] validInputIndexes = new int[6];
    for (int i = 0; i < inputs.length; i++) {
      if (inputs[i] != null && !erasured.contains(i)) {
        if (bytePerData != -1 && inputs[i].length != bytePerData) {
          throw new RuntimeException("The length of 6 data cannot be aligned!");
        }
        bytePerData = inputs[i].length;
        if (validInputs < 6) {
          tmpEncodeMatrixData[validInputs] = new Byte[6];
          for (int j = 0; j < 6; j++) {
            tmpEncodeMatrixData[validInputs][j] = encodeMatrix.get(i, j);
          }
          validInputIndexes[validInputs] = i;
        }
        validInputs++;
      }
    }
    if (validInputs < 6) {
      throw new RuntimeException("The length of input data must be greater than or equal to 6!");
    }

    for (int i = 0; i < erasure.length; i++) {
      if (output[i] == null) {
        output[i] = new byte[bytePerData];
      }
    }

    // Step1: calculated inverse Matrix for encode Matrix, we called `decode Matrix`
    Matrix<Byte> decodeMatrix = new Matrix<Byte>(tmpEncodeMatrixData).inverse(computation);

    // Step 2: dataMatrix multiply `decode Matrix`, get the data
    for (int i = 0; i < bytePerData; i++) {
      Byte[][] tmpData = new Byte[6][1];
      for (int j = 0; j < 6; j++) {
        tmpData[j] = new Byte[1];
        tmpData[j][0] = inputs[validInputIndexes[j]][i];
      }
      Matrix<Byte> data = new Matrix<Byte>(tmpData);
      Matrix<Byte> out = Matrix.multiply(decodeMatrix, data, computation);
      for (int j = 0; j < erasure.length; j++) {
        output[j][i] = out.get(erasure[j], 0);
      }
    }
  }
}
