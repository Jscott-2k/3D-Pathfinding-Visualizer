package io.pathfinder.math;

/**
 * Data class for a n x m Matrix. 
 * Able to multiply with other matrices or vectors, and perform other matrix operations.
 *
 * @author Justin Scott
 *
 */
public class Matrix {

	private int rows, columns = 0;
	private double[][] matrix;

	public Matrix(int rows, int columns) {
		this.setRows(rows);
		this.setColumns(columns);
		this.matrix = new double[rows][columns];
	}
	public Matrix(int order) {
		this.setRows(order);
		this.setColumns(order);
		this.matrix = new double[order][order];
	}

	public double get(int row, int column) {
		return matrix[row][column];
	}

	public static Matrix MultiplyMatrixVector(Matrix matrix, Vector vector) {
		Matrix vectorMatrix = vector.toMatrix();
		return vectorMatrix.multiplyMatrix(matrix);
	}

	public static Matrix MultiplyMatrix(Matrix matrix1, Matrix matrix2) {
		return matrix1.multiplyMatrix(matrix2);
	}

	
	
	/**
	 * Multiplies two matrices by summing the product of the rows
	 * Returns a resulting matrix
	 * @param pMatrix
	 * @return
	 */
	public Matrix multiplyMatrix(Matrix pMatrix) {

		if (columns != pMatrix.getRows()) {
			return null;
		}

		Matrix resultMatrix = new Matrix(rows, pMatrix.getColumns());

		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < pMatrix.getColumns(); column++) {
				double sum = 0;
				for (int i = 0; i < pMatrix.getRows(); i++) {

					sum += get(row, i) * pMatrix.get(i, column);
				}
				resultMatrix.set(row, column, sum);
			}

		}
		return resultMatrix;
	}

	public int getRows() {
		return rows;
	}

	private void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	private void setColumns(int columns) {
		this.columns = columns;
	}

	public double[][] getMatrix() {
		return matrix;
	}

	public void set(int row, int col, double val) {
		matrix[row][col] = val;
	}

	public Matrix multiplyMatrixVector(Vector v) {

		return MultiplyMatrixVector(this, v);
	}

	public int getOrder() {
		if(isSquare() && rows >= 0) {
			return rows;
		}
		return -1;
	}
	
	public boolean isDimension(int rows, int columns) {
		return this.rows == rows && this.columns == columns;
	}

	public boolean isSquare() {
		return rows == columns;
	}

	/**
	 * Calculates the cofactor matrix and uses a given tempMatrix as output
	 * @param tempMatrix
	 * @param pRow
	 * @param pColumn
	 * @param rSize
	 */
	private void getCofactor(Matrix tempMatrix, int pRow, int pColumn, int rSize) {

		int i = 0, j = 0;

		for (int row = 0; row < rSize; row++) {
			for (int column = 0; column < rSize; column++) {
				if (column != pColumn && row != pRow) {
					tempMatrix.set(i, j++, get(row, column));
					if (j >= rSize - 1) {
						j = 0;
						i++;
					}
				}
			}
		}

	}
	
	public Matrix getCofactor(int pRow, int pCol) {
		if(isSquare()) {
			getCofactor(this, pRow, pCol, getOrder());
			return this;
		}else {
			return null;
		}
	}
	
	
	public double determinant() {
		
		//Cannot compute non-square matrix.
		if(!isSquare()) { 
			return 0;
		}
		return determinant(this, getOrder());
		
	}
	

	/**
	 * Recursively calculates determinant of given square matrix
	 * 
	 * @param matrix
	 * @param order
	 * @return
	 */
	private double determinant(Matrix matrix, int order) {

		//Cannot compute non-square matrix. Sanity check.
		if (!matrix.isSquare()) { 
			return 0;
		}
		
		//Base case
		if(order == 1) {
			return matrix.get(0, 0);
		}
		
		double determinant = 0;
		
		Matrix tempMatrix = new Matrix(order, order);
		int sign = 1;
		for (int f = 0; f < order; f++) {

			matrix.getCofactor(tempMatrix, 0, f, order);
			
			determinant += sign * matrix.get(0, f) * determinant(tempMatrix,  order - 1);
			sign = -sign;
		}

		return determinant;
	}

	/**
	 * The transpose of the cofactor matrix
	 * @return
	 */
	public Matrix getAdjoint() {
		
		final int order = getOrder();
		
		Matrix resultMatrix = new Matrix(order);
		if(order == 1) {
			resultMatrix.set(0, 0, 1);
			return resultMatrix;
		}
		
		Matrix tempMatrix = new Matrix(order);
		
		int sign = 1;
		
		
		for(int fi=0;fi<order;fi++) {
			for(int fj=0;fj<order;fj++) {
				
				getCofactor(tempMatrix, fi, fj, order);
				tempMatrix = tempMatrix.getTranspose();
				
				resultMatrix.set(fi, fj, sign * determinant(tempMatrix, order - 1));
				
				sign = -sign;
			}

		}
		
		return resultMatrix.getTranspose();
	} 
	
	/**
	 * Calculates the inverse of the matrix by taking the adjoint, transpose, and dividing by the determinant.
	 * Note there are faster ways for calculating the inverse for square matrices of specific sizes,
	 * however this method will work for any square matrix
	 * 
	 * @return
	 */
	public Matrix getInverse() {
		
		double det = determinant();
		
		return getAdjoint().getTranspose().divide(det);
		
	}
	
	/**
	 * Multiply all values by some value a in the matrix
	 * @param a
	 * @return
	 */
	public Matrix multiply(double a) {
		Matrix resultMatrix = new Matrix(rows, columns);
		
		for(int fi=0;fi<rows;fi++) {
			for(int fj=0;fj<columns;fj++) 
			{
				resultMatrix.set(fi, fj, get(fi, fj) * a);
			}
		};
		
		return resultMatrix;
	}
	
	/**
	 * Divide all values by some value a in the matrix
	 * @param a
	 * @return
	 */
	public Matrix divide(double a) {
		Matrix resultMatrix = new Matrix(rows, columns);
		
		for(int fi=0;fi<rows;fi++) {
			for(int fj=0;fj<columns;fj++) 
			{
				resultMatrix.set(fi, fj, get(fi, fj) / a);
			}
		};
		
		return resultMatrix;
	}
	
	/**
	 * Rotates a matrix. Turns all of the rows into columns and all columns into rows
	 * @return
	 */
	public Matrix getTranspose() {
		Matrix resultMatrix = new Matrix(columns, rows);
		for(int row=0;row<rows;row++) {
			for(int column=0;column<columns;column++) {
				resultMatrix.set(column, row, get(row, column));
			}
		}
		return resultMatrix;
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				str += get(row, column) + " ";
			}
			str += "\n";
		}
		return str;
	}
	
	/**
	 * Checks equality of matrices by contained values in the 2D matrix array
	 */
	@Override
	public boolean equals(Object matrix) {
		
		// Ensure the passed in matrix is a Matrix type
		if(! (matrix instanceof Matrix) ) {
			return false;
		}
		Matrix castedMatrix = (Matrix) matrix;
	
		// Make sure it is same dimension
		if(!isDimension(castedMatrix.getRows(), castedMatrix.getColumns())) {
			return false;
		}
		
		// Lastly check to see if all the values are the same
		for(int fi=0;fi<rows;fi++) {
			for(int fj=0;fj<columns;fj++) {
				if(castedMatrix.get(fi, fj) != get(fi, fj)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
		
}