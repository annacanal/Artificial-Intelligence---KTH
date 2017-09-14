package duckhunt;

import java.util.Random;


class Player {
    
    public int numStates = 7; //6+unknown
    public int numObs = 10;   //9+dead
    public Matrix A;
    public Matrix B;
    public Matrix pi;
    
    public Player() {
        //A = new Matrix(numStates, numStates);
        //B = new Matrix(numStates, numObs);
        pi = new Matrix(1,numStates);
        
        // fill Matrices... randomly?
        A = Matrix.random(numStates, numStates);
        B = Matrix.random(numStates, numObs);
        pi.setValue(0, 0, 1.0);
    }

    /**
     * Shoot!
     *
     * This is the function where you start your work.
     *
     * You will receive a variable pState, which contains information about all
     * birds, both dead and alive. Each bird contains all past moves.
     *
     * The state also contains the scores for all players and the number of
     * time steps elapsed since the last time this function was called.
     *
     * @param pState the GameState object with observations etc
     * @param pDue time before which we must have returned
     * @return the prediction of a bird we want to shoot at, or cDontShoot to pass
     */
    public Action shoot(GameState pState, Deadline pDue) {
        /*
         * Here you should write your clever algorithms to get the best action.
         * This skeleton never shoots.
         */
        int bird_specie = Constants.SPECIES_UNKNOWN;
        Random aleat = new Random();
        Bird bird;
        do{
            int bird_index = aleat.nextInt(pState.getNumBirds());
            bird = pState.getBird(bird_index);
            bird_specie = find_specie(bird);
            if(bird.isAlive()){
                if(bird_specie == Constants.SPECIES_UNKNOWN||bird_specie == Constants.SPECIES_BLACK_STORK){
                    // don't know the specie yet or it is protected
                    return cDontShoot;
                }else{
                    //any specie that I can shoot!  :)
                    //predict next movement function
                    this.recalculateAB(pState, pDue);
                    int next_move = predict_next_movement(bird_specie);
                    return new Action(bird_index, next_move);
                }
            }
            
        }while(bird.isDead());
        
        // This line would predict that bird 0 will move right and shoot at it.
        // return Action(0, MOVE_RIGHT);
    
        return cDontShoot;
    }

    /**
     * Guess the species!
     * This function will be called at the end of each round, to give you
     * a chance to identify the species of the birds for extra points.
     *
     * Fill the vector with guesses for the all birds.
     * Use SPECIES_UNKNOWN to avoid guessing.
     *
     * @param pState the GameState object with observations etc
     * @param pDue time before which we must have returned
     * @return a vector with guesses for all the birds
     */
    public int[] guess(GameState pState, Deadline pDue) {
        /*
         * Here you should write your clever algorithms to guess the species of
         * each bird. This skeleton makes no guesses, better safe than sorry!
         */
        int[] lGuess = new int[pState.getNumBirds()];
        for (int i = 0; i < pState.getNumBirds(); ++i){
            lGuess[i] = find_specie(pState.getBird(i));
        }
        
        //compare_guessing:
            
        return lGuess;
    }

    /**
     * If you hit the bird you were trying to shoot, you will be notified
     * through this function.
     *
     * @param pState the GameState object with observations etc
     * @param pBird the bird you hit
     * @param pDue time before which we must have returned
     */
    public void hit(GameState pState, int pBird, Deadline pDue) {
        System.err.println("HIT BIRD!!!");
    }

    /**
     * If you made any guesses, you will find out the true species of those
     * birds through this function.
     *
     * @param pState the GameState object with observations etc
     * @param pSpecies the vector with species
     * @param pDue time before which we must have returned
     */
    public void reveal(GameState pState, int[] pSpecies, Deadline pDue) {
                  
    }
    
    //funcio que li passo l'ocell i em retorna l'especie
    public int find_specie(Bird bird){
        int[] observations = new int [bird.getSeqLength()];
        for(int j = 0; j < bird.getSeqLength(); j++){
            if (bird.getObservation(j) == -1){//Constants.MOVE_DEAD){
                observations[j] = 9;

            }else{
                observations[j] = bird.getObservation(j);
            }
        }
        Matrix O = new Matrix(observations);
        Matrix delta, delta_idx;
        
        // Initialize alpha matrix
        delta = new Matrix(A.getRows(), O.getRows());
        delta_idx = new Matrix(A.getRows(), O.getRows());
        //delta.fillColumn(pi.mult_column(B, O.getIntElement(0,0)),0);   
        delta.fillColumn(pi.mult_column_log(B, O.getIntElement(0,0)),0); 
        //delta.show();
        //delta_idx first column is -1 (we don't care)
        delta_idx.fillColumnSameNumber(-1,delta_idx.getRows(),0);
        //delta_idx.show();
        Matrix aux = new Matrix(A.getCols(),1);
        //double temp_max = 0.0;
        double temp_max;// = Double.NEGATIVE_INFINITY;
        //double current = 0.0;
        double current;// = Double.NEGATIVE_INFINITY;
        int max_state = -1;

        
        // Iterate over all observations
        for (int i = 1; i < O.getRows(); i++){
            for (int j = 0; j < A.getCols(); j++){
                 temp_max = Double.NEGATIVE_INFINITY;
                for (int k = 0; k < A.getRows(); k++){
                        current = Math.log10(A.getElement(k, j)) + delta.getElement(k, i-1);
                    if(temp_max < current){
                        temp_max = current;
                        max_state = k;
                    }
                }
                if (temp_max + Math.log10(B.getElement(j, O.getIntElement(i, 0))) == Double.NEGATIVE_INFINITY){  	
                        max_state = -1; //I don't care, 
                }
                delta_idx.setValue(j, i, max_state);
                aux.setValue(j, 0, temp_max);
                delta.fillColumn(aux.plus_column_log(B, O.getIntElement(i, 0)), i);
            }
        }
        
        // Decide most probable state sequence
        Matrix sequence = new Matrix(1,O.getRows());
        double max = Double.NEGATIVE_INFINITY;     
        //last position
        for (int i = 0; i < delta.getRows(); i++){
            if(delta.getElement(i, delta.getCols()-1)>max){
                max = delta.getElement(i, delta.getCols()-1);
                sequence.setValue(0,sequence.getCols()-1,i);
            }
        }
        /*for (int i = O.getRows()-1; i > 0; i--){
            sequence.setValue(0,i-1,delta_idx.getElement(sequence.getIntElement(0, i), i));
        }*/
        //System.err.println("hola!");
        
        return sequence.getIntElement(0, O.getRows()-1);
        //return 0;
    }
    
    public int predict_next_movement(int bird_specie){
        //tinc l'estat -> max in B column
        double most_prob_move_prob = 0.0;
        int most_prob_move = -2;
        for(int i = 0; i<B.getCols(); i++){
            if (B.getElement(bird_specie, i) > most_prob_move_prob){
                most_prob_move = i;
                most_prob_move_prob = B.getElement(bird_specie, i);
            }
        }
        return most_prob_move;
    }
    
    public void recalculateAB (GameState pState, Deadline pDue){
        //Initialize
        long maxIters = 10000000;
        int iters = 0;
        double oldLogProb = Double.NEGATIVE_INFINITY;
        int N = A.getCols(), T = pState.getBird(0).getSeqLength(), M = B.getCols();
        Matrix gamma;
        Matrix3D di_gamma;
        double logProb = 0.0;

        for(int ocells = 0; ocells<pState.getNumBirds(); ocells++){
            Matrix alpha = new Matrix(N,T);
            Matrix beta = new Matrix(N,T);
            Matrix c = new Matrix(T,1);

            int[] observations;
            Bird bird = pState.getBird(ocells);
            observations = new int [bird.getSeqLength()];
            for(int j = 0; j < bird.getSeqLength(); j++){
                if (bird.getObservation(j) == -1){
                    observations[j] = 9;
                }else{
                    observations[j] = bird.getObservation(j);
                }
            }
            Matrix O = new Matrix(observations);
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
    }
    
    public static final Action cDontShoot = new Action(-1, -1);
}
