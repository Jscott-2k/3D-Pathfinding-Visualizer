package io.pathfinder.math;

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
		// System.out.println("Multiply Matrix Vector: " + matrix + ", " +
		// vector.toString());
		Matrix vectorMatrix = vector.toMatrix();
		return vectorMatrix.multiplyMatrix(matrix);
	}

	public static Matrix MultiplyMatrix(Matrix matrix1, Matrix matrix2) {
		return matrix1.multiplyMatrix(matrix2);
	}

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
	
	public Matrix getInverse() {
		
		double det = determinant();
		
		return getAdjoint().getTranspose().divide(det);
		
	}
	
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
	
	@Override
	public boolean equals(Object matrix) {
		
		if(! (matrix instanceof Matrix) ) {
			return false;
		}
		Matrix castedMatrix = (Matrix) matrix;
	
		if(!isDimension(castedMatrix.getRows(), castedMatrix.getColumns())) {
			return false;
		}
		
		for(int fi=0;fi<rows;fi++) {
			for(int fj=0;fj<columns;fj++) {
				if(castedMatrix.get(fi, fj) != get(fi, fj)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	public static Matrix QuickInverse(Matrix m) // Only for Rotation/Translation Matrices
	{

		Matrix matrix = new Matrix(4,4);
		matrix.set(0,0,m.get(0,0)); matrix.set(0,1,m.get(1,0));; matrix.set(0,2,matrix.get(2, 0)); matrix.set(0,3,0);
		
		matrix.set(2,0,m.get(0,2)); matrix.set(2,1,m.get(1,2));; matrix.set(2,2,matrix.get(2, 2)); matrix.set(2,3,0);
		matrix.set(3,0,
				-(m.get(3,0) * m.get(0,0) + m.get(3,1) * m.get(1,0) + m.get(3,2) * m.get(2,0))
				);
		matrix.set(3,1,
				-(m.get(3,0) * matrix.get(0,1) + m.get(3,1) * matrix.get(1,1) + m.get(3,2) * matrix.get(2,1))
				);
		matrix.set(3,2,
				-(m.get(3,0) * matrix.get(0,2) + m.get(3,1) * matrix.get(1,2) + m.get(3,2) * matrix.get(2,2))
				);
		matrix.set(3,3,1);
		
		return matrix;
	}
	
}