//package Duck;
final public class Matrix {
    private final int M;             // number of rows
    private final int N;             // number of columns
    private final double[][] data;   // M-by-N array

    // CONSTRUCTOR: create M-by-N matrix of 0's
    public Matrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new double[M][N];
    }
    
    public Matrix(int [] data, int length){
        this.M = length;
        this.N = 1;
        this.data = new double [M][N];
        for(int i = 0; i<M; i++){
            this.data[i][0] = data[i];
        }
    }
    public static Matrix random(int M, int N) {
        Matrix A = new Matrix(M, N);
        double acum, init;
        
        for (int i = 0; i < M; i++){
        	acum = 0.0;
        	init = 0.5;
            for (int j = 0; j < N-1; j++){
                A.data[i][j] = Math.random()*(1-acum -init);
            	acum +=  A.data[i][j];
            }
            A.data[i][N-1] = 1-acum;
        }
        
        return A;
    }

    
    public static Matrix initialize(int M, int N) {
        Matrix A = new Matrix(M, N);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if(j==i){
                	A.data[i][j] = 0.7;
                }else{
                	A.data[i][j]= 0.3/(N-1);
                }
        return A;
    }

    // GETTERS
    public int getRows (){
        return this.M;
    }
    
    public int getCols (){
        return this.N;
    }
    
    public int getIntElement (int row, int col){
        return (int) this.data[row][col];
    }
    
    public double getElement (int row, int col){
        return data[row][col];
    }
    
    public Matrix getColumn (int col){
        Matrix A = new Matrix(this.M, 1);
        for (int i=0; i < M; i++){
            A.setValue(i, 0, data[i][col]);
        }
        return A;
    }
    
    
    // SETTERS
    public void setValue(int x, int y, double value){
        data[x][y] = value;
    }
    

    // METHODS
    // fill matrix with values from String []
    public void fillMatrix (String [] buf, int start_data){
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

     // return C = A + log(B) 
    public Matrix plus_column_log(Matrix B,  int col) {
        Matrix A = this;
       // if (B.M != A.M || B.N != A.N) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(B.M, 1);
        for (int i = 0; i < B.M; i++){
        	C.data[i][0] = A.data[i][0] + Math.log10(B.data[i][col]);
    	}
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
            C.data[i][0] = Math.log10(A.data[i][0]* B.data[i][col]);
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
                System.err.print(data[i][j]+" ");
            System.err.println();
        }
        System.err.println();
    }
    
    //print matrix to HMM0 output
    public void showHMM0solution(){
        String solution = this.M + " " + this.N + " ";
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                solution += data[i][j] + " ";
        }
        System.err.print(solution);
    }
    
    public double sum(){
    	 double solution = 0.0;
         for (int i = 0; i < M; i++) {
             for (int j = 0; j < N; j++) 
                 solution += data[i][j];
         }
         return solution;
    }
    //print matrix to HMM1 output: sums up all elements and prints
    public void showHMM1solution(){
        double solution = 0.0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) 
                solution += data[i][j];
        }
        System.err.println(solution);
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
                solution += data[i][j] + " ";
        }
        System.out.println(solution);
        solution = "";
        System.out.print(B.getRows()+" "+ B.getCols()+" ");
        for (int i = 0; i < B.getRows(); i++) {
            for (int j = 0; j <  B.getCols(); j++) 
                solution += B.getElement(i, j) + " ";
        }
        System.out.println(solution);
    }
}
