import java.util.*;
import jdk.nashorn.internal.runtime.arrays.ArrayData;

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
        
        Random random = new Random();
        return nextStates.elementAt(minimax(gameState, Constants.CELL_X));
    }


    public int minimax (GameState gameState, int player){
        if (gameState.isEOG()){
            return utility(gameState, player);
        }else{
            Vector<GameState> nextStates = new Vector<GameState>();
            gameState.findPossibleMoves(nextStates);
            if(player == Constants.CELL_X){
                int bestPossible = Integer.MIN_VALUE;
                int v;
                for(int i = 0; i<nextStates.size(); i++){
                    v = minimax(nextStates.get(i),Constants.CELL_O);
                    bestPossible = max(bestPossible, v);
                }
                return bestPossible;
            }else if(player == Constants.CELL_O){
                int bestPossible = Integer.MAX_VALUE;
                int v;
                for(int i = 0; i<nextStates.size(); i++){
                    v = minimax(nextStates.get(i),Constants.CELL_X);
                    bestPossible = min(bestPossible, v);
                }
                return bestPossible;
            }else{
                System.err.println("Hi ha un error, nano!");
                return -1;
            }
            
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
        

        
        return -1;
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
                    score = 2;
                }else if (score == -1){
                    return 0;
                }else{
                    score = 1;
                }
            }else if(gameState.at(1, 1)==Constants.CELL_O){
                if(score == -1){
                    score = -2;
                }else if (score == 1){
                    return 0;
                }else{
                    score = -1;
                }
            }
            
            if(gameState.at(2, 2)==Constants.CELL_X){
                if(score > 0){
                    score++;
                }else if (score == -1){
                    return 0;
                }else{
                    score = 1;
                }
            }else if(gameState.at(2, 2)==Constants.CELL_O){
                if(score < 0){
                    score--;
                }else if (score == 1){
                    return 0;
                }else{
                    score = -1;
                }
            }
            
            if(gameState.at(3, 3)==Constants.CELL_X){
                if(score > 0){
                    score++;
                }else if (score == -1){
                    return 0;
                }else{
                    score = 1;
                }
            }else if(gameState.at(3, 3)==Constants.CELL_O){
                if(score < 0){
                    score--;
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
                score = 2;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(1, 2)==Constants.CELL_O){
            if(score == -1){
                score = -2;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(2, 1)==Constants.CELL_X){
            if(score > 0){
                score++;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(2, 1)==Constants.CELL_O){
            if(score < 0){
                score--;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(3, 0)==Constants.CELL_X){
            if(score > 0){
                score++;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(3, 0)==Constants.CELL_O){
            if(score < 0){
                score--;
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
                score = 2;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(row, 1)==Constants.CELL_O){
            if(score == -1){
                score = -2;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(row, 2)==Constants.CELL_X){
            if(score > 0){
                score++;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(row, 2)==Constants.CELL_O){
            if(score < 0){
                score--;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(row, 3)==Constants.CELL_X){
            if(score > 0){
                score++;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(row, 3)==Constants.CELL_O){
            if(score < 0){
                score--;
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
                score = 2;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(1, col)==Constants.CELL_O){
            if(score == -1){
                score = -2;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(2, col)==Constants.CELL_X){
            if(score > 0){
                score++;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(2,col)==Constants.CELL_O){
            if(score < 0){
                score--;
            }else if (score == 1){
                return 0;
            }else{
                score = -1;
            }
        }

        if(gameState.at(3, col)==Constants.CELL_X){
            if(score > 0){
                score++;
            }else if (score == -1){
                return 0;
            }else{
                score = 1;
            }
        }else if(gameState.at(3, col)==Constants.CELL_O){
            if(score < 0){
                score--;
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
