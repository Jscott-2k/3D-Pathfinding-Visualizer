package io.pathfinder.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.pathfinder.math.Matrix;
import io.pathfinder.math.MatrixBuilder;

class MatrixTest {

	
	private Matrix matrix3x3;
	private Matrix matrix4x4;
	private Matrix matrix3x2;
	
	@BeforeEach
	void setUp() throws Exception {
		
		matrix3x3 = new Matrix(3);
		
		matrix3x3.set(0, 0, -5);
		matrix3x3.set(0, 1, 5);
		matrix3x3.set(0, 2, 5);
		
		matrix3x3.set(1, 0, 5);
		matrix3x3.set(1, 1, 25);
		matrix3x3.set(1, 2, 5);
		
		matrix3x3.set(2, 0, 5);
		matrix3x3.set(2, 1, 5);
		matrix3x3.set(2, 2, -5);
		
		matrix4x4 = new Matrix(4);
		matrix4x4.set(0, 0, 1);
		matrix4x4.set(0, 1, 2);
		matrix4x4.set(0, 2, 3);
		matrix4x4.set(3, 2, -6);
		
		matrix3x2 = new Matrix(3,2);
		
		matrix3x2.set(0, 0, 3);
		matrix3x2.set(0, 1, 3);
		matrix3x2.set(1, 0, 1);
		matrix3x2.set(1, 1, 2);
		matrix3x2.set(2, 0, 4);
		matrix3x2.set(2, 1, 9);
		
	}

	@AfterEach
	void tearDown() throws Exception {
		matrix3x3 = null;
	}

	@Test
	void testIsSquare() {
		assertTrue(matrix3x3.isSquare());
	}
	
	@Test
	void testPrint() {	
		System.out.println(matrix3x3);
	}
	
	@Test
	void testDeterminant() {
		System.out.println(matrix3x3.determinant());
		Assertions.assertEquals(matrix3x3.determinant(), 500);
	}
	@Test
	void testIdentity() {
		System.out.println(MatrixBuilder.getIdentityMatrix(2));
		System.out.println(MatrixBuilder.getIdentityMatrix(3));
		System.out.println(MatrixBuilder.getIdentityMatrix(4));
	}
	
	@Test
	void testAdjoint() {
		System.out.println(matrix3x3);
		Matrix expectedMatrix = new Matrix(3,3);
		
		expectedMatrix.set(0, 0, -150);
		expectedMatrix.set(0, 1, 50);
		expectedMatrix.set(0, 2, -100);
		
		expectedMatrix.set(1, 0, 50);
		expectedMatrix.set(1, 1, 0);
		expectedMatrix.set(1, 2, 50);
		
		expectedMatrix.set(2, 0, -100);
		expectedMatrix.set(2, 1, 50);
		expectedMatrix.set(2, 2, -150);
		
		Matrix adjointMatrix = matrix3x3.getAdjoint();
		System.out.println(adjointMatrix);
		
		assertEquals(expectedMatrix, adjointMatrix);
	}
	
	@Test
	void testInverse() {
		
		System.out.println("Test Inverse");
		
		System.out.println(matrix3x3);
		Matrix expectedMatrix = new Matrix(3,3);
		expectedMatrix.set(0, 0, -3/10.0);
		expectedMatrix.set(0, 1, 1/10.0);
		expectedMatrix.set(0, 2, -1/5.0);
		
		expectedMatrix.set(1, 0, 1/10.0);
		expectedMatrix.set(1, 1, 0);
		expectedMatrix.set(1, 2, 1/10.0);
		
		expectedMatrix.set(2, 0, -1/5.0);
		expectedMatrix.set(2, 1, 1/10.0);
		expectedMatrix.set(2, 2, -3/10.0);
		
		Matrix inverseMatrix = matrix3x3.getInverse();
		
		System.out.println("-expected-");
		System.out.println(expectedMatrix);
		System.out.println("-actual-");
		System.out.println(inverseMatrix);
		
		assertEquals(expectedMatrix, inverseMatrix);
		
	}
	@Test
	void testTranspose() {
		System.out.println(matrix4x4);
		System.out.println(matrix4x4.getTranspose());
		
		System.out.println(matrix3x2);
		System.out.println(matrix3x2.getTranspose());
		System.out.println();
		
		System.out.println(matrix3x3);
		System.out.println(matrix3x3.getTranspose());
	}

}
