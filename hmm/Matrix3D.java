/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hmm;

/**
 *
 * @author USUARIO
 */
final public class Matrix3D {

    private final int M;             // number of rows
    private final int N;             // number of columns
    private final int L;             // number of columns
    private final double[][][] data;   // M-by-N array

    // CONSTRUCTOR: create M-by-N-by-L matrix of 0's
    public Matrix3D(int M, int N, int L) {
        this.M = M;
        this.N = N;
        this.L = L;
        data = new double[M][N][L];
    }

    // GETTERS
    public int getRows (){
        return this.M;
    }
    
    public int getCols (){
        return this.N;
    }
    
   
    public double getElement (int row, int col, int l){
        return data[row][col][l];
    }
    /*
    public Matrix getColumn (int col){
        Matrix A = new Matrix(this.M, 1);
        for (int i=0; i < M; i++){
            A.setValue(i, 0, data[i][col]);
        }
        return A;
    }
    
    */
    // SETTERS
    public void setValue(int x, int y, int z, double value){
        data[x][y][z] = value;
    }
    
    
    // METHODS
    // fill matrix with values from String []
    /*public void fillMatrix (String [] buf, int start_data){
        for (int i=0; i < M; i++){
            for (int j=0; j< N; j++){
                    data[i][j]= Double.parseDouble(buf[start_data]);
                    start_data++;
            }
        }
    }
    
    // fill column with values from a column matrix
    public void fillColumn (Matrix B, int col){
        for (int i=0; i < M; i++){
            data[i][col] = B.getElement(i, 0);
        }
    }
    // fill column with values from a column matrix
    public void fillColumnSameNumber (double num, int num_rows, int col){
        for (int i=0; i < num_rows; i++){
            data[i][col] = num;
        }
    }
    
    // create and return the transpose of the invoking matrix
    public Matrix transpose() {
        Matrix A = new Matrix(N, M);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                A.data[j][i] = this.data[i][j];
        return A;
    }

    // return C = A + B
    public Matrix plus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] + B.data[i][j];
        return C;
    }

    // return C = log(A) + log(B) = log (A·B)
    public Matrix plus_column_log(Matrix B,  int col) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
        	C.data[i][col] = Math.log(A.data[i][col]) + Math.log(B.data[i][col]);
        return C;
    }

    // return C = A - B
    public Matrix minus(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                C.data[i][j] = A.data[i][j] - B.data[i][j];
        return C;
    }

    // does A = B exactly?
    public boolean eq(Matrix B) {
        Matrix A = this;
        if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if (A.data[i][j] != B.data[i][j]) return false;
        return true;
    }

    // return C = A * B
    public Matrix multiply(Matrix B) {
        Matrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(A.M, B.N);
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;
    }
    
 // return C = log(A * B)
    public Matrix multiply_log(Matrix B) {
        Matrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(A.M, B.N);
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] += Math.log((A.data[i][k] * B.data[k][j]));
        return C;
    }
    
    // return C = A * B[:][col] (multiplies by a column of B, elementwise)
    public Matrix mult_column(Matrix B, int col){
        Matrix A = this;
        if(A.N > 1 && A.M > 1){
           // System.out.println("First matrix has to be a row or a column.");
        }else if(A.N>1){ //if it is a row
            A = A.transpose();
        }
        Matrix C = new Matrix (B.M, 1);
        for (int i = 0; i< B.M; i++){
            C.data[i][0] = A.data[i][0] * B.data[i][col];
        }
        return C;
    }
    
    // return C = log( A * B[:][col]) (multiplies by a column of B, elementwise)
    public Matrix mult_column_log(Matrix B, int col){
        Matrix A = this;
        if(A.N > 1 && A.M > 1){
           // System.out.println("First matrix has to be a row or a column.");
        }else if(A.N>1){ //if it is a row
            A = A.transpose();
        }
        Matrix C = new Matrix (B.M, 1);
        for (int i = 0; i< B.M; i++){
            C.data[i][0] = Math.log (A.data[i][0] * B.data[i][col]);
        }
        return C;
    }
    
    // return max of the element-wise product
    public Matrix max(){
        Matrix max = new Matrix(M,1);
        for (int i=0; i < M; i++){
            for (int j=0; j< N; j++){
                    if(data[i][j]>max.getElement(i,0)){
                        max.setValue(i, 0, data[i][j]);
                    }
            }
        }
        return max;
    }
    
    
    // PRINTING METHODS TO SHOW SOLUTIONS:
    // print matrix to standard output
    public void show() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                System.out.print(data[i][j]+" ");
            System.out.println();
        }
        System.out.println();
    }
    
    //print matrix to HMM0 output
    public void showHMM0solution(){
        String solution = this.M + " " + this.N + " ";
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                solution += data[i][j] + " ";
        }
        System.out.print(solution);
    }
    
    //print matrix to HMM1 output: sums up all elements and prints
    public void showHMM1solution(){
        double solution = 0.0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                solution += data[i][j];
        }
        System.out.println(solution);
    }
    
    //print matrix to HMM2 output 
    public void showHMM2solution(){
        String solution = "";
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                solution += this.getIntElement(i, j) + " ";
        }
        System.out.print(solution);
    }
    
    //print matrix A and B to HMM3 output
    public void showHMM3solution(Matrix B){
        String solution = "";
        System.out.print(M+" "+ N+" ");
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                solution += this.getIntElement(i, j) + " ";
        }
        System.out.print(solution);
        solution = "";
        System.out.print(B.getRows()+" "+ B.getCols()+" ");
        for (int i = 0; i < B.getRows(); i++) {
            for (int j = 0; j <  B.getCols(); j++) 
                solution += B.getIntElement(i, j) + " ";
        }
        System.out.print(solution);
    }*/
}