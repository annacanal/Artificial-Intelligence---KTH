//package tictactoe.Java_skeletons.TTT;
import java.util.*;
//import jdk.nashorn.internal.runtime.arrays.ArrayData;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
	private int thisPlayer;
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);
        

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
        
        //Random random = new Random();
        int depth = 3;
        thisPlayer = gameState.getNextPlayer();
        Pair result = alpha_beta(gameState, thisPlayer,depth,Integer.MIN_VALUE,Integer.MAX_VALUE);
        return nextStates.elementAt(result.index);
    }


    
    public Pair alpha_beta (GameState gameState, int player, int depth, int alpha, int beta){
        int v=0;
        Pair result = new Pair (-1, -1);
        Pair aux;
        if (gameState.isEOG() || depth == 0){
            return new Pair(-1,utility(gameState,player));
        }else{
            Vector<GameState> nextStates = new Vector<GameState>();
            gameState.findPossibleMoves(nextStates);
            boolean no_prune = true;
            if(player == thisPlayer){
                v = Integer.MIN_VALUE;
                for(int i = 0; i<nextStates.size() && no_prune; i++){
                    aux = alpha_beta(nextStates.get(i), nextStates.get(i).getNextPlayer(), depth-1, alpha, beta);
                    if(v<aux.value){
                        v=aux.value;
                        result = new Pair(i, v); 
                    }
                    alpha = max(alpha, v);
                    if (beta<=alpha){
                        no_prune = false;
                    }
                }
            }else{
                v = Integer.MAX_VALUE;
                for(int i = 0; i<nextStates.size(); i++){
                    aux = alpha_beta(nextStates.get(i), nextStates.get(i).getNextPlayer(), depth-1, alpha, beta);
                    if(v>aux.value){
                    	v=aux.value;
                    	result = new Pair(i, v);
                    }
                    beta = min(beta, v);
                    if(alpha<=beta){
                        no_prune = false;
                    }
                }
            }
            return result;
            
        }
    }
    
    
  
    
    public int utility(GameState gameState, int player){
        int score = 0;
        //evaluate_rows
        for(int i = 0; i<gameState.BOARD_SIZE; i++){
            score += evaluate_row(i,gameState);
            score += evaluate_col(i,gameState);
        }
        //evaluate diagonals
        score += evaluate_diagonal1(gameState);
        score += evaluate_diagonal2(gameState);
        if(gameState.isXWin()&&player == Constants.CELL_X){
            score += 10000;
        }else if(gameState.isOWin()&&player == Constants.CELL_O){
            score += -10000;
        
        }
        return score;
    }
    
    public int evaluate_diagonal1(GameState gameState){
        int score = 0;
        if(gameState.at(0, 0)==Constants.CELL_X){
                score=1;
            }else if(gameState.at(0, 0)==Constants.CELL_O){
                score=-1;
            }
            
            if(gameState.at(1, 1)==Constants.CELL_X){
                if(score == 1){
                    score = 10;
                }else if (score == -1){
                    return 0;
                }else{
                    score = 1;
                }
            }else if(gameState.at(1, 1)==Constants.CELL_O){
                if(score == -1){
                    score = -10;
                }else if (score == 1){
                    return 0;
                }else{
                    score = -1;
                }
            }
            
            if(gameState.at(2, 2)==Constants.CELL_X){
                if(score > 0){
                    score*=10;
                }else if (score == -1){
                    return 0;
                }else{
                    score = 1;
                }
            }else if(gameState.at(2, 2)==Constants.CELL_O){
                if(score < 0){
                    score*=10;
                }else if (score == 1){
                    return 0;
                }else{
                    score = -1;
                }
            }
            
            if(gameState.at(3, 3)==Constants.CELL_X){
                if(score > 0){
                    score*=10;
                }else if (score == -1){
                    return 0;
                }else{
                    score = 1;
                }
            }else if(gameState.at(3, 3)==Constants.CELL_O){
                if(score < 0){
                    score*=10;
                }else if (score == 1){
                    return 0;
                }else{
                    score = -1;
                }
            }
        return score;
    }
    
    
    public int evaluate_diagonal2(GameState gameState){
        int score = 0;
        if(gameState.at(0, 3)==Constants.CELL_X){
            score=1;
        }else if(gameState.at(0, 3)==Constants.CELL_O){
            score=-1;
        }

        if(gameState.at(1, 2)==Constants.CELL_X){
            if(score == 1){
                score = 10;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(1, 2)==Constants.CELL_O){
            if(score == -1){
                score = -10;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(2, 1)==Constants.CELL_X){
            if(score > 0){
                score*=10;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(2, 1)==Constants.CELL_O){
            if(score < 0){
                score*=10;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(3, 0)==Constants.CELL_X){
            if(score > 0){
                score*=10;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(3, 0)==Constants.CELL_O){
            if(score < 0){
                score*=10;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }
        return score;
    }
    
    
    public int evaluate_row(int row, GameState gameState){
        int score = 0;
        if(gameState.at(row, 0)==Constants.CELL_X){
            score=1;
        }else if(gameState.at(row, 0)==Constants.CELL_O){
            score=-1;
        }

        if(gameState.at(row, 1)==Constants.CELL_X){
            if(score == 1){
                score = 10;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(row, 1)==Constants.CELL_O){
            if(score == -1){
                score = -10;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(row, 2)==Constants.CELL_X){
            if(score > 0){
                score*=10;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(row, 2)==Constants.CELL_O){
            if(score < 0){
                score*=10;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(row, 3)==Constants.CELL_X){
            if(score > 0){
                score*=10;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(row, 3)==Constants.CELL_O){
            if(score < 0){
                score*=10;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }
        return score;
    }
    
    
    public int evaluate_col(int col, GameState gameState){
        int score = 0;
        if(gameState.at(0, col)==Constants.CELL_X){
            score=1;
        }else if(gameState.at(0, col)==Constants.CELL_O){
            score=-1;
        }

        if(gameState.at(1, col)==Constants.CELL_X){
            if(score == 1){
                score = 10;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(1, col)==Constants.CELL_O){
            if(score == -1){
                score = -10;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(2, col)==Constants.CELL_X){
            if(score > 0){
                score*=10;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(2,col)==Constants.CELL_O){
            if(score < 0){
                score*=10;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(3, col)==Constants.CELL_X){
            if(score > 0){
                score*=10;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(3, col)==Constants.CELL_O){
            if(score < 0){
                score*=10;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }
        return score;
    }
    
    public int max(int a, int b){
        if(a>b){
            return a;
        }else{
            return b;
        }
    }
    public int min(int a, int b){
        if(a<b){
            return a;
        }else{
            return b;
        }
    }
}