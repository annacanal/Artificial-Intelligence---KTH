package hmm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HMM1 {
    
    public static void solve_hmm1() {
            Matrix A, B, pi, O, alpha;
            String line;
            String [] buf;
            int rows, cols;
            BufferedReader reader;
            
            //readInput(A,B,pi);
            try{
                //System.out.println("Reading input...");
                reader = new BufferedReader(new InputStreamReader(System.in));
                //reader = new BufferedReader(new FileReader("src/hmm/input1.txt"));

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
                
                // read and fill matrix O
                line = reader.readLine();
                buf = line.split(" ");
                rows = Integer.parseInt(buf[0]);
                cols = 1;
                O = new Matrix(rows, cols);
                O.fillMatrix(buf, 1);
                //O.show();
                
                
                // Initialize alpha matrix
                alpha = pi.mult_column(B, O.getIntElement(0,0));
                // Iterate over all observations
                for (int i = 1; i< O.getRows(); i++){
                    alpha = A.transpose().multiply(alpha).mult_column(B, O.getIntElement(i, 0));
                }
                
                // Show result of HMM1
                alpha.showHMM1solution();
                
            }catch(Exception e){
                // Something went wrong
            }    
    }

}
