package hmm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class HMM3 {
    public static void solve_hmm3() {
            Matrix A, B, pi, O, alpha, beta, c, gamma;
            Matrix3D di_gamma;
            String line;
            String [] buf;
            int rows, cols;
            BufferedReader reader;
            
            //readInput(A,B,pi);
            try {
                //System.out.println("Reading input...");
                //reader = new BufferedReader(new InputStreamReader(System.in));
                reader = new BufferedReader(new FileReader("src/hmm/input3.txt"));

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
                
                //Initialize
                long maxIters;
                maxIters = 1000000000;
                int iters = 0;
                double oldLogProb = Double.NEGATIVE_INFINITY;
                int N = A.getCols(), T = O.getRows(), M = B.getCols();
                
                alpha = new Matrix(N,T);
                beta = new Matrix(N,T);
                c = new Matrix(T,1);
                double logProb = 0.0;

                
                
                while(iters < maxIters && logProb > oldLogProb){
                    /* alpha-pass */
                    //compute alpha_zero
                    if (iters!=0){
                        oldLogProb = logProb;
                    }
                    
                    //System.out.println("I'm here! - in");
                    for(int i = 0; i<N; i++){
                        alpha.setValue(i, 0, pi.getElement(0, i) * B.getElement(i, O.getIntElement(0, 0)));
                        c.setValue(0, 0, c.getElement(0, 0) + alpha.getElement(i,0));
                    }
                    // scale alpha_zero
                    c.setValue(0, 0, 1/c.getElement(0, 0));
                    for(int i = 0; i<N; i++){
                        alpha.setValue(i, 0, c.getElement(0, 0)*alpha.getElement(i,0));
                    }
                    //System.out.println("I'm here! - alpha zero done");
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
                        //System.out.println("I'm here! - computing alpha_t");
                        for(int i = 0; i<N; i++){
                            alpha.setValue(i, t, c.getElement(t, 0)*alpha.getElement(i, t));
                        }
                    }
                    //System.out.println("this is the end, my friend...");
                    /* beta-pass */
                    // Initialize
                    for(int i = 0; i<N; i++){
                        beta.setValue(i, T-1, c.getElement(T-1, 0));
                    }
                    //System.out.println("beta init end");

                    // beta-pass
                    for(int t = T-2; t>=0; t--){
                        for(int i = 0; i<N; i++){
                            beta.setValue(i, t, 0.0);
                            for(int j = 0; j<N; j++){
                                beta.setValue(i, t, beta.getElement(i, t) + 
                                        A.getElement(i,j)*B.getElement(j,O.getIntElement(t+1, 0))
                                                *beta.getElement(j, t+1));
                                //System.out.println("I'm here! - beta pass");
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
                        //System.out.println("I'm here! - gamma!");
                        for (int i = 0; i<N; i++){
                            gamma.setValue(i, t, 0.0);
                            for (int j = 0; j<N; j++){
                                di_gamma.setValue(i, j, t, alpha.getElement(i, t)*A.getElement(i,j)
                                        *B.getElement(j,O.getIntElement(t+1, 0))*beta.getElement(j, t+1)/denom);
                                gamma.setValue(i, t, gamma.getElement(i,t)+di_gamma.getElement(i,j,t));
                            }
                        }
                    }
                    //System.out.println("I'm here! - pre denom");
                    //special case for gamma T-1
                    denom = 0.0;
                    for(int i = 0; i<N; i++){
                        denom = denom + alpha.getElement(i, T-1);
                    }
                    for(int i = 0; i<N; i++){
                        gamma.setValue(i, T-1, alpha.getElement(i, T-1)/denom);
                        //System.out.println("I'm here! - gamma computing");
                    }


                    /* re-estimation of A,B,pi */
                    // pi
                    for(int i = 0; i<N; i++){
                        pi.setValue(0,i, gamma.getElement(i,0));
                    }

                    // A
                    double numer = 0.0;
                    for(int i = 0; i<N; i++){
                        for(int j = 0; j<N; j++){
                            numer = 0.0;
                            //System.out.println("I'm here! - reestimating A");
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

                    /* Compute log probability */
                    logProb = 0.0;
                    for(int i = 0; i<T; i++){
                        logProb += Math.log(c.getElement(i,0));
                    }
                    logProb = -logProb;
                    //System.out.println("I'm here! - this is the end");

                    // To iterate or not to iterate... that is the question
                    iters = iters + 1;
                    //System.out.println("iteracions " + iters);
                
                }
                
                //A.show();
                //B.show();
                
                // Show result of HMM3
                A.showHMM3solution(B);
                
            } catch(Exception e){
                // Something went wrong
                System.out.println("ERROR MY FRIEND");
            }    
    }
}
