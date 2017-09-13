package hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class HMM2 {

	  
    public static void solve_hmm2() {
            Matrix A, B, pi, O, delta, delta_idx;
            String line;
            String [] buf;
            int rows, cols;
            BufferedReader reader;
            
            //readInput(A,B,pi);
            try{
                //System.out.println("Reading input...");
            	//reader = new BufferedReader(new InputStreamReader(System.in));
                reader = new BufferedReader(new FileReader("src/hmm/input2_2.txt"));

                // read and fill matrix A
                line = reader.readLine();
                buf = line.split(" ");
                rows = Integer.parseInt(buf[0]);
                cols = Integer.parseInt(buf[1]);
                A = new Matrix(rows, cols);
                A.fillMatrix(buf, 2);
              //  A.show();

                // read and fill matrix B
                line = reader.readLine();
                buf = line.split(" ");
                rows = Integer.parseInt(buf[0]);
                cols = Integer.parseInt(buf[1]);
                B = new Matrix(rows, cols);
                B.fillMatrix(buf, 2);
               // B.show();

                // read and fill matrix pi
                line = reader.readLine();
                buf = line.split(" ");
                rows = Integer.parseInt(buf[0]);
                cols = Integer.parseInt(buf[1]);
                pi = new Matrix(rows, cols);
                pi.fillMatrix(buf, 2);
              //  pi.show();
                
                // read and fill matrix O (observations)
                line = reader.readLine();
                buf = line.split(" ");
                rows = Integer.parseInt(buf[0]);
                cols = 1;
                O = new Matrix(rows, cols);
                O.fillMatrix(buf, 1);
              //  O.show();
                
                
                // Initialize alpha matrix
                delta = new Matrix(A.getRows(), O.getRows());
                delta_idx = new Matrix(A.getRows(), O.getRows());
              //  delta.fillColumn(pi.mult_column(B, O.getIntElement(0,0)),0);   
                delta.fillColumn(pi.mult_column_log(B, O.getIntElement(0,0)),0); 
               // delta.show();
                // delta_idx first column is -1 (we don't care)
                delta_idx.fillColumnSameNumber(-1,delta_idx.getRows(),0);
                //delta_idx.show();
                Matrix aux = new Matrix(A.getCols(),1);
            //    double temp_max = 0.0;
               double temp_max = Double.NEGATIVE_INFINITY;
              //  double current = 0.0;
                double current = Double.NEGATIVE_INFINITY;
                int max_state = -1;
                
                
                // Iterate over all observations
                for (int i = 1; i < O.getRows(); i++){
                	/* System.out.print("Valor i: ");
                	 System.out.println(i);*/
                	 
                    for (int j = 0; j < A.getCols(); j++){
                    	/* System.out.print("Valor j: ");
                    	 System.out.println(j);*/
                    	// temp_max = 0.0;
                    	 temp_max = Double.NEGATIVE_INFINITY;
                    	
                        for (int k = 0; k < A.getRows(); k++){
                        	/*System.out.print("Valor k: ");
                        	 System.out.println(k);*/
                        	// current = A.getElement(k, j)*delta.getElement(k, i-1);
                        	current = Math.log10(A.getElement(k, j)) + delta.getElement(k, i-1);
                        	/*System.out.println("current:");
                        	System.out.println(current);*/
                            if(temp_max < current){
                                temp_max = current;
                                max_state = k;
                            }
                          /*  System.out.print("Valor max_current: ");
                            System.out.println(temp_max);*/
                        }/*
                        if (temp_max * B.getElement(j, O.getIntElement(i, 0)) == 0){  	
                        	max_state = -1; //I don't care, 
                        }*/
                        if (temp_max + Math.log10( B.getElement(j, O.getIntElement(i, 0))) == Double.NEGATIVE_INFINITY){  	
                        	max_state = -1; //I don't care, 
                        }
                        delta_idx.setValue(j, i, max_state);
                        aux.setValue(j, 0, temp_max);
                        //multiply by B-column according to observation
                      //  delta.fillColumn(aux.mult_column(B, O.getIntElement(i, 0)),i);
                        delta.fillColumn(aux.plus_column_log(B, O.getIntElement(i, 0)),i);
               
                        
                    }
                    
                }
             /*   System.out.println("delta: ");
                delta.show();
                System.out.println("delta index: "); 
                delta_idx.show();   */   
                
                // Decide most probable state sequence
                Matrix sequence = new Matrix(1,O.getRows());
              //  double max = 0.0;  
                double max = Double.NEGATIVE_INFINITY;     
                //last position
                for (int i = 0; i < delta.getRows(); i++){
                    if(delta.getElement(i, delta.getCols()-1)>max){
                        max = delta.getElement(i, delta.getCols()-1);
                        sequence.setValue(0,sequence.getCols()-1,i);
                    }
                }
              //  sequence.show();
                
                for (int i = O.getRows()-1; i > 0; i--){
                    sequence.setValue(0,i-1,delta_idx.getElement(sequence.getIntElement(0, i), i));
                }
                
                // Show result of HMM2
                sequence.showHMM2solution();
                
            }catch(Exception e){
                // Something went wrong
            }    
    }


}
