package zcy.ec.coder.math;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import zcy.ec.coder.math.Fraction;
import zcy.ec.coder.math.Matrix;
import zcy.ec.coder.operator.Computation;
import zcy.ec.coder.operator.DefaultComputation;
import zcy.ec.coder.operator.FractionComputation;
import zcy.ec.coder.operator.GaloisFieldComputation;

public class TestMatrix {

  @Test
  public void testDoubleMatrix() {
    /* numpy test:
>>> import numpy as np
>>> a1=np.mat([[1,2,3],[4,5,6],[7,8,9]])
>>> a2=np.mat([[2,2,2],[2,2,2],[2,2,2]])
>>> a1+a2
matrix([[ 3,  4,  5],
        [ 6,  7,  8],
        [ 9, 10, 11]])
>>> a1*a2
matrix([[12, 12, 12],
        [30, 30, 30],
        [48, 48, 48]])
>>> a3=np.mat([[2,2],[2,2],[3,3]])
>>> a1*a3
matrix([[15, 15],
        [36, 36],
        [57, 57]])
>>> a4=np.mat([[1,1,1],[1,2,4],[1,3,9]])
>>> a4.I
matrix([[ 3. , -3. ,  1. ],
        [-2.5,  4. , -1.5],
        [ 0.5, -1. ,  0.5]])
>>> a5=np.mat([[0,1,0],[1,0,0],[0,0,1]])
>>> a5.I
matrix([[0., 1., 0.],
        [1., 0., 0.],
        [0., 0., 1.]])
    * */
    Matrix<Double> a1 = new Matrix<Double>(new Double[][]{{1d,2d,3d},{4d,5d,6d},{7d,8d,9d}});
    Matrix<Double> a2 = new Matrix<Double>(new Double[][]{{2d,2d,2d},{2d,2d,2d},{2d,2d,2d}});
    Matrix<Double> a3 = new Matrix<Double>(new Double[][]{{2d,2d},{2d,2d},{3d,3d}});
    Computation computation = new DefaultComputation();
    Matrix.add(a1, a2, computation).print();
    Matrix.multiply(a1, a2, computation).print();
    Matrix.multiply(a1, a3, computation).print();
    ArithmeticException thrown = assertThrows(
        ArithmeticException.class,
        () -> a1.inverse(computation).print()
    );
    assertTrue(thrown.getMessage().contains("Found singular matrix!") || thrown.getMessage()
        .contains("Gaussian elimination error"));
    Matrix<Double> a4 = new Matrix<Double>(new Double[][]{{1d,1d,1d},{1d,2d,4d},{1d,3d,9d}});
    Matrix<Double> a4r = a4.inverse(computation);
    a4r.print();
    assertTrue(Matrix.multiply(a4, a4r, computation).isUnitMatrix(computation));
    Matrix<Double> a5 = new Matrix<Double>(new Double[][]{{0d,1d,0d},{1d,0d,0d},{0d,0d,1d}});
    a5.inverse(computation).print();
  }

  @Test
  public void testFractionMatrix() {
    /* numpy test:
>>> import numpy as np
>>> a1=np.mat([[1,2,3],[4,5,6],[7,8,9]])
>>> a2=np.mat([[2,2,2],[2,2,2],[2,2,2]])
>>> a1+a2
matrix([[ 3,  4,  5],
        [ 6,  7,  8],
        [ 9, 10, 11]])
>>> a1*a2
matrix([[12, 12, 12],
        [30, 30, 30],
        [48, 48, 48]])
>>> a3=np.mat([[2,2],[2,2],[3,3]])
>>> a1*a3
matrix([[15, 15],
        [36, 36],
        [57, 57]])
>>> a4=np.mat([[1,1,1],[1,2,4],[1,3,9]])
>>> a4.I
matrix([[ 3. , -3. ,  1. ],
        [-2.5,  4. , -1.5],
        [ 0.5, -1. ,  0.5]])
>>> a5=np.mat([[0,1,0],[1,0,0],[0,0,1]])
>>> a5.I
matrix([[0., 1., 0.],
        [1., 0., 0.],
        [0., 0., 1.]])
    * */
    Matrix<Fraction> a1 = new Matrix<Fraction>(
        new Fraction[][]{
            {Fraction.create(1), Fraction.create(2), Fraction.create(3)},
            {Fraction.create(4), Fraction.create(5), Fraction.create(6)},
            {Fraction.create(7), Fraction.create(8), Fraction.create(9)}});
    Matrix<Fraction> a2 = new Matrix<Fraction>(
        new Fraction[][]{
            {Fraction.create(2), Fraction.create(2), Fraction.create(2)},
            {Fraction.create(2), Fraction.create(2), Fraction.create(2)},
            {Fraction.create(2), Fraction.create(2), Fraction.create(2)}});
    Matrix<Fraction> a3 = new Matrix<Fraction>(
        new Fraction[][]{
            {Fraction.create(2), Fraction.create(2),},
            {Fraction.create(2), Fraction.create(2)},
            {Fraction.create(3), Fraction.create(3)}});
    Computation computation = new FractionComputation();
    Matrix.add(a1, a2, computation).print();
    Matrix.multiply(a1, a2, computation).print();
    Matrix.multiply(a1, a3, computation).print();
    ArithmeticException thrown = assertThrows(
        ArithmeticException.class,
        () -> a1.inverse(computation).print()
    );
    assertTrue(thrown.getMessage().contains("Found singular matrix!") || thrown.getMessage()
        .contains("Gaussian elimination error"));
    Matrix<Fraction> a4 = new Matrix<Fraction>(
        new Fraction[][]{
            {Fraction.create(1), Fraction.create(1), Fraction.create(1)},
            {Fraction.create(1), Fraction.create(2), Fraction.create(4)},
            {Fraction.create(1), Fraction.create(3), Fraction.create(9)}});
    Matrix<Fraction> a4r = a4.inverse(computation);
    a4r.print();
    assertTrue(Matrix.multiply(a4, a4r, computation).isUnitMatrix(computation));
    Matrix<Fraction> a5 = new Matrix<Fraction>(
        new Fraction[][]{
            {Fraction.create(0), Fraction.create(1), Fraction.create(0)},
            {Fraction.create(1), Fraction.create(0), Fraction.create(0)},
            {Fraction.create(0), Fraction.create(0), Fraction.create(1)}});
    a5.inverse(computation).print();
  }


  @Test
  public void testGaloisFieldMatrix() {
    Computation computation = new GaloisFieldComputation();
    Matrix<Byte> a = new Matrix<Byte>(
        new Byte[][]{
            {GaloisFieldComputation.intToByte(1), GaloisFieldComputation.intToByte(1), GaloisFieldComputation.intToByte(1)},
            {GaloisFieldComputation.intToByte(1), GaloisFieldComputation.intToByte(2), GaloisFieldComputation.intToByte(4)},
            {GaloisFieldComputation.intToByte(1), GaloisFieldComputation.intToByte(3), GaloisFieldComputation.intToByte(9)}});
    Matrix<Byte> ar = a.inverse(computation);
    ar.print();
    assertTrue(Matrix.multiply(a, ar, computation).isUnitMatrix(computation));
  }
}
