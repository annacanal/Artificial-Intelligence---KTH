package hmm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HMM0 {

    public static void solve_hmm0() {
            Matrix A, B, pi, result;
            String line;
            String [] buf;
            int rows, cols;
            BufferedReader reader;
            
            //readInput(A,B,pi);
            try{
                
                reader = new BufferedReader(new InputStreamReader(System.in));
                //reader = new BufferedReader(new FileReader("src/hmm/input0.txt"));

                // read and fill matrix A
                line = reader.readLine();
                buf = line.split(" ");
                rows = Integer.parseInt(buf[0]);
                cols = Integer.parseInt(buf[1]);
                A = new Matrix(rows, cols);
                A.fillMatrix(buf, 2);
                //A.show();

                // read and fill matrix B
                line = reader.readLine();
                buf = line.split(" ");
                rows = Integer.parseInt(buf[0]);
                cols = Integer.parseInt(buf[1]);
                B = new Matrix(rows, cols);
                B.fillMatrix(buf, 2);
                //B.show();

                // read and fill matrix pi
                line = reader.readLine();
                buf = line.split(" ");
                rows = Integer.parseInt(buf[0]);
                cols = Integer.parseInt(buf[1]);
                pi = new Matrix(rows, cols);
                pi.fillMatrix(buf, 2);
                //pi.show();
                
                // compute observation probabilities
                result = pi.multiply(A).multiply(B);
                
                // Show result of HMM0
                result.showHMM0solution();
                
            }catch(Exception e){
                // Something went wrong
            }    
    }
}