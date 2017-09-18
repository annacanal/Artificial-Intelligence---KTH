//package Duck;

public class Specie {
	private int numStates = 5;// 5 patterns
	private int numObs = 9;   //9+dead
	private Matrix A;
	private Matrix B;
	private Matrix pi;
	
	 public Specie() {
	    
	        pi = new Matrix(1,numStates);
	        // fill Matrices randomly
	        A = Matrix.initialize(numStates, numStates); 
	        //System.err.println("A: ");
	   	      //  A.show();
	        B = Matrix.random(numStates, numObs);
	        pi.setValue(0, 0, 1.0);
	        //B.show();
	       // pi = Matrix.random(1, numStates);
	        //pi.show();
	    }
	
	 public void Baum_Welch (Matrix O){
	        //Initialize
	        long maxIters = 50;
	        int iters = 0;
	        double oldLogProb = Double.NEGATIVE_INFINITY;
	        int N = A.getCols(), T = O.getCols(), M = B.getCols();
	        Matrix gamma;
	        Matrix3D di_gamma;
	        double logProb = 0.0;
	        
	        Matrix alpha = new Matrix(N,T);
	        Matrix beta = new Matrix(N,T);
	        Matrix c = new Matrix(T,1);

	        iters = 0;
	        while(iters < maxIters && logProb > oldLogProb){
	            //compute alpha_zero
	            if (iters!=0){
	                oldLogProb = logProb;
	            }
	
	            for(int i = 0; i<N; i++){
	                alpha.setValue(i, 0, pi.getElement(0, i) * B.getElement(i, O.getIntElement(0, 0)));
	                c.setValue(0, 0, c.getElement(0, 0) + alpha.getElement(i,0));
	            }
	            // scale alpha_zero
	            c.setValue(0, 0, 1/c.getElement(0, 0));
	            for(int i = 0; i<N; i++){
	                alpha.setValue(i, 0, c.getElement(0, 0)*alpha.getElement(i,0));
	            }
	            // compute alpha_t
	            for(int t = 1; t < T; t++){
	                c.setValue(t, 0, 0.0);
	                for(int i = 0; i < N; i++){
	                    alpha.setValue(i, t, 0.0);
	                    for(int j = 0; j < N; j++){
	                        alpha.setValue(i, t, alpha.getElement(i,t)+alpha.getElement(j, t-1)*A.getElement(j, i));
	                    }
	
	                    alpha.setValue(i, t, alpha.getElement(i, t)*B.getElement(i, O.getIntElement(t,0)));
	                    c.setValue(t, 0, c.getElement(t, 0) + alpha.getElement(i, t));
	                }
	                //scale alpha_t
	                c.setValue(t, 0, 1/c.getElement(t, 0));
	                for(int i = 0; i<N; i++){
	                    alpha.setValue(i, t, c.getElement(t, 0)*alpha.getElement(i, t));
	                }
	            }
	            // Initialize
	            for(int i = 0; i<N; i++){
	                beta.setValue(i, T-1, c.getElement(T-1, 0));
	            }
	
	            // beta-pass
	            for(int t = T-2; t>=0; t--){
	                for(int i = 0; i<N; i++){
	                    beta.setValue(i, t, 0.0);
	                    for(int j = 0; j<N; j++){
	                        beta.setValue(i, t, beta.getElement(i, t) + 
	                                A.getElement(i,j)*B.getElement(j,O.getIntElement(t+1, 0))
	                                        *beta.getElement(j, t+1));
	                    }
	                    //scale beta_t
	                    beta.setValue(i, t, c.getElement(t, 0)*beta.getElement(i, t));
	                }
	            }
	
	            // compute gamma and di-gamma
	            gamma = new Matrix(N,T);
	            di_gamma = new Matrix3D(N,N,T);
	            double denom = 0.0;
	            for(int t = 0; t< T-1; t++){
	                denom = 0.0;
	                for (int i = 0; i<N; i++){
	                    for (int j = 0; j<N; j++){
	                        denom += alpha.getElement(i, t)*A.getElement(i,j)*B.getElement(j,O.getIntElement(t+1, 0))*beta.getElement(j, t+1);
	                    }
	                }
	                for (int i = 0; i<N; i++){
	                    gamma.setValue(i, t, 0.0);
	                    for (int j = 0; j<N; j++){
	                        di_gamma.setValue(i, j, t, alpha.getElement(i, t)*A.getElement(i,j)
	                                *B.getElement(j,O.getIntElement(t+1, 0))*beta.getElement(j, t+1)/denom);
	                        gamma.setValue(i, t, gamma.getElement(i,t)+di_gamma.getElement(i,j,t));
	                    }
	                }
	            }
	            //special case for gamma T-1
	            denom = 0.0;
	            for(int i = 0; i<N; i++){
	                denom = denom + alpha.getElement(i, T-1);
	            }
	            for(int i = 0; i<N; i++){
	                gamma.setValue(i, T-1, alpha.getElement(i, T-1)/denom);
	            }
	
	
	            // pi
	            for(int i = 0; i<N; i++){
	                pi.setValue(0,i, gamma.getElement(i,0));
	            }
	
	            // A
	            double numer = 0.0;
	            for(int i = 0; i<N; i++){
	                for(int j = 0; j<N; j++){
	                    numer = 0.0;
	                    denom = 0.0;
	                    for(int t = 0; t < T-1; t++){
	                        numer += di_gamma.getElement(i, j, t);
	                        denom += gamma.getElement(i, t);
	                    }
	                    A.setValue(i, j, numer/denom);
	                }
	            }
	
	            // B 
	            for(int i = 0; i<N; i++){
	                for(int j = 0; j<M; j++){
	                    numer = 0.0;
	                    denom = 0.0;
	                    for(int t = 0; t<T; t++){
	                        if(O.getIntElement(t,0)==j){
	                            numer += gamma.getElement(i, t);
	                            //System.out.println("I'm here! - reB inside if");
	                        }
	                        denom += gamma.getElement(i, t);
	                    }
	                    B.setValue(i,j,numer/denom);
	                }
	            }
	
	            logProb = 0.0;
	            for(int i = 0; i<T; i++){
	                logProb += Math.log(c.getElement(i,0));
	            }
	            logProb = -logProb;
	
	            // To iterate or not to iterate... that is the question
	            iters = iters + 1;
	
	        }
	    }
	    
	 public double forward(Matrix O){
		
		 double oldLogProb = Double.NEGATIVE_INFINITY;
	     int N = A.getCols(), T = O.getCols(), M = B.getCols();     
	     double logProb = 0.0;	        
         Matrix alpha = new Matrix(N,T);
         Matrix c = new Matrix(T,1);
  
		  // Initialize alpha matrix
        /* alpha = pi.mult_column(B, O.getIntElement(0,0));
         // Iterate over all observations
         for (int i = 1; i< O.getRows(); i++){
             alpha = A.transpose().multiply(alpha).mult_column(B, O.getIntElement(i, 0));
         }
        */
		  //compute alpha_zero
     
        oldLogProb = logProb;
         

         for(int i = 0; i<N; i++){
             alpha.setValue(i, 0, pi.getElement(0, i) * B.getElement(i, O.getIntElement(0, 0)));
             c.setValue(0, 0, c.getElement(0, 0) + alpha.getElement(i,0));
         }
         // scale alpha_zero
         c.setValue(0, 0, 1/c.getElement(0, 0));
         for(int i = 0; i<N; i++){
             alpha.setValue(i, 0, c.getElement(0, 0)*alpha.getElement(i,0));
         }
         // compute alpha_t
         for(int t = 1; t < T; t++){
             c.setValue(t, 0, 0.0);
             for(int i = 0; i < N; i++){
                 alpha.setValue(i, t, 0.0);
                 for(int j = 0; j < N; j++){
                     alpha.setValue(i, t, alpha.getElement(i,t)+alpha.getElement(j, t-1)*A.getElement(j, i));
                 }

                 alpha.setValue(i, t, alpha.getElement(i, t)*B.getElement(i, O.getIntElement(t,0)));
                 c.setValue(t, 0, c.getElement(t, 0) + alpha.getElement(i, t));
             }
             //scale alpha_t
             c.setValue(t, 0, 1/c.getElement(t, 0));
             for(int i = 0; i<N; i++){
                 alpha.setValue(i, t, c.getElement(t, 0)*alpha.getElement(i, t));
             }
         }
		 
         return alpha.sum();
		 
	 }
	 public double viterbi(Matrix O){
		 
	 }
	 }
}
