package zcy.ec.coder.math;

import zcy.ec.coder.operator.Computation;

public class Matrix<T> {

  private int row;
  private int column;
  private Object[][] data;

  public Matrix(Object[][] data) {
    this.data = data;
    if (this.data != null) {
      this.row = data.length;
      if (this.row > 0) {
        this.column = data[0].length;
        for (int i = 1; i < data.length; i++) {
          if (this.column != this.data[i].length) {
            throw new RuntimeException("not a matrix");
          }
        }
      }
    }
  }

  public void set(int r, int c, T b) {
    if (r < 0 || r >= row || c < 0 || c >= column) {
      throw new RuntimeException("wrong row " + row + " or wrong column " + column);
    }
    this.data[r][c] = b;
  }

  public T get(int r, int c) {
    if (r < 0 || r >= row || c < 0 || c >= column) {
      throw new RuntimeException("wrong row " + row + " or wrong column " + column);
    }
    return (T) this.data[r][c];
  }

  public static <T> Matrix<T> multiply(Matrix<T> left, Matrix<T> right, Computation<T> computation) {
    if (left.column != right.row) {
      throw new RuntimeException("Matrix can not match, left.row=" + left.row + ", left.column=" +
          left.column + ", right.row=" + right.row + ", right.column=" + right.column);
    }
    Object[][] data = new Object[left.row][right.column];
    for (int i = 0; i < left.row; i ++) {
      for (int j = 0; j < right.column; j++) {
        data[i][j] = computation.zero();
        for (int k = 0; k < left.column; k++) {
          T t = computation.mul(left.get(i, k), right.get(k, j));
          data[i][j] = computation.add((T) data[i][j], t);
        }
      }
    }
    return new Matrix(data);
  }

  public static <T> Matrix<T> add(Matrix<T> left, Matrix<T> right, Computation<T> computation) {
    if (left.row != right.row || left.column != right.column) {
      throw new RuntimeException("Matrix can not match, left.row=" + left.row + ", left.column=" +
          left.column + ", right.row=" + right.row + ", right.column=" + right.column);
    }
    Object[][] data = new Object[left.row][right.column];
    for (int i = 0; i < left.row; i ++) {
      for (int j = 0; j < left.column; j++) {
        data[i][j] = (T) computation.add(left.get(i, j), right.get(i, j));
      }
    }
    return new Matrix<T>(data);
  }

  // The matrix inversion is realized by Gaussian elimination method
  public <T> Matrix<T> inverse(Computation<T> computation) {
    if (this.column != this.row) {
      throw new RuntimeException("Can not get inverse matrix! column=" + column + ", row=" + row);
    }
    int n = this.row;
    Matrix tmp = this.copy();
    // construct unit matrix
    Object[][] output = new Object[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (i == j) {
          output[i][j] = computation.unit();
        } else {
          output[i][j] = computation.zero();
        }
      }
    }
    Matrix inverse = new Matrix(output);
    for (int i = 0; i < n; i++) {
      // Handle i column:
      //    Make data[i][i] is unit and data[k][i] is zero.
      //    Then in the i column, only the i row is unit, other row is all zero.
      // Steps:
      // (1) Compute the multiplicative inverse of data[i][i], we called `mi`
      // (2) Multiply all the elements in row i by `mi`, now data[i][i] is unit element.
      // (3) For the other rows k:
      //     Compute the addition inverse of data[k][i], we called 'ai'
      //     Add all element data[k][j] + ai * (data[i][j])
      //
      // add the addition inverse of row i multiplied by -data[k][i]. now data[k][i] is zero
      // Note: Execute (2)(3) while updating output

      // Setup for Step1
      // Handle the special case, if tmp.get(i, i) is zero, we should found the tmp.get(j, i) which
      // is not zero, then add the row j to row i
      if (computation.isZero((T) tmp.get(i, i))) {
        for (int j = i + 1; j < n; j++) {
          if (!computation.isZero((T) tmp.get(j, i))) {
            for (int k = 0; k < n; k ++) {
              tmp.set(i, k, computation.add((T) tmp.get(j, k), (T)tmp.get(i, k)));
              inverse.set(i, k, computation.add((T) inverse.get(j, k), (T)inverse.get(i, k)));
            }
            break;
          }
        }
      }
      // Step 1
      T mi = computation.mulInv((T) tmp.get(i, i));
      if (computation.isNan(mi)) {
        throw new ArithmeticException("Found singular matrix!");
      }

      // Step 2
      for (int j = 0; j < n; j++) {
        tmp.set(i, j, computation.mul((T) tmp.get(i, j), mi));
        inverse.set(i, j, computation.mul((T) inverse.get(i, j), mi));
      }

      // Step 3
      for (int k = 0; k < n; k++) {
        if (k != i) {
          T ai = computation.addInv((T) tmp.get(k, i));
          for (int j = 0; j < n; j++) {
            tmp.set(k, j,
                computation.add((T) tmp.get(k, j), computation.mul((T) tmp.get(i, j), ai)));
            inverse.set(k, j,
                computation.add((T) inverse.get(k, j), computation.mul((T) inverse.get(i, j), ai)));
          }
        }
      }
    }
    if (!tmp.isUnitMatrix(computation)) {
      throw new ArithmeticException(
          "Gaussian elimination error, the matrix can not convert to unit matrix");
    }
    return inverse;
  }

  public void print() {
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < column; j++) {
        System.out.print(this.data[i][j].toString() + " ");
      }
      System.out.println();
    }
  }

  public Matrix<T> copy() {
    Object[][] data = new Object[row][column];
    for (int i = 0; i < row; i++) {
      data[i] = new Object[column];
      for (int j = 0; j < column; j++) {
        data[i][j] = this.get(i, j);
      }
    }
    return new Matrix<T>(data);
  }

  public boolean isUnitMatrix(Computation<T> computation) {
    if (this.row != this.column) {
      throw new RuntimeException("Not a square matrix, row=" + row + ", column=" + column);
    }
    for (int i = 0; i < this.row; i ++) {
      for (int j = 0; j < this.column; j++) {
        if (i == j && !computation.isUnit((T) this.data[i][j])) {
          return false;
        } else if (i != j && !computation.isZero((T) this.data[i][j])) {
          return false;
        }
      }
    }
    return true;
  }
}