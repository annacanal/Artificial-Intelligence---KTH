//package Duck;


import java.util.Random;

class Player {
    private final Specie[] species = new Specie[Constants.COUNT_SPECIES];
    
    public Player() {
        for(int i = 0; i<Constants.COUNT_SPECIES; i++){
            Specie specie = new Specie(); //depending on i it'll be a different specie
            species[i] = specie;
        }
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
        int[] lGuess = new int[pState.getNumBirds()];
        for (int i = 0; i < pState.getNumBirds(); ++i){
            lGuess[i] = find_specie(pState.getBird(i));
        }
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
        for(int i=0; i<pSpecies.length; i++){
            Matrix O = getObservationsFromBird(pState.getBird(i));
            species[pSpecies[i]].Baum_Welch(O);
        }
    }
    
    
	public int find_specie(Bird bird){
		double max = 0.0;
		double prob_specie;
		int specie = -1;
		
		for(int i = 0; i < Constants.COUNT_SPECIES; i++){ 
            prob_specie = species[i].forward(getObservationsFromBird(bird));
            if (prob_specie > max){
            	max = prob_specie;
            	specie = i;
            }
        }
		return specie;
		 
	}
    public Matrix getObservationsFromBird(Bird bird){
        int[] observations = new int [bird.getSeqLength()];
        int len = 0;
        for(int j = 0; j < bird.getSeqLength() && bird.wasAlive(j); j++){
            observations[j] = bird.getObservation(j);
            len++;
        }
        Matrix O = new Matrix(observations,len);
        return O;
    }
    public static final Action cDontShoot = new Action(-1, -1);
}