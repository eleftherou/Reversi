import java.util.ArrayList;
import java.util.Random;

public class GamePlayer
{
    private int maxDepth;
    private int playerLetter;

    public GamePlayer(int maxDepth, int playerLetter)
    {
        this.maxDepth = maxDepth;
        this.playerLetter = playerLetter;
    }

    public Move MiniMax(Board board) {
        if (playerLetter == Board.X) //X's turn to play
            return max(new Board(board), 0);
        else //O's turn to play
            return min(new Board(board), 0);

    }

    int a = Integer.MIN_VALUE;
    int b = Integer.MAX_VALUE;
    public Move max(Board board, int depth) {

        Random random = new Random();

        if (board.isTerminal() || depth == maxDepth) {
            Move lastMove = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.heuristic());
            return lastMove;
        }

        ArrayList<Board> children = new ArrayList<Board>(board.getChildren(Board.X));
        Move maxMove = new Move(Integer.MIN_VALUE);
        for (Board child:children) {
            Move move = min(child, depth+1); //creating a child and increasing the depth

            if (move.getValue() >= maxMove.getValue()) { //choose the child with the max value
                if ((move.getValue() == maxMove.getValue())) {
                    if (random.nextInt(2) == 0) {
                        maxMove.setRow(child.getLastMove().getRow());
                        maxMove.setCol(child.getLastMove().getCol());
                        maxMove.setValue(move.getValue());
                        if(maxMove.getValue()>b){ //checking for pruning
                            b=maxMove.getValue();
                            break;
                        }
                    }
                } else {
                    maxMove.setRow(child.getLastMove().getRow());
                    maxMove.setCol(child.getLastMove().getCol());
                    maxMove.setValue(move.getValue());
                    if(maxMove.getValue()>b){ //checking for pruning
                        b=maxMove.getValue();
                        break;
                    }
                }
            }
        }
        return maxMove;
    }

    public Move min(Board board, int depth) {

        Random random = new Random();

        if (board.isTerminal() || depth == maxDepth) {
            Move lastMove = new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.heuristic());
            return lastMove;
        }

        ArrayList<Board> children = new ArrayList<Board>(board.getChildren(Board.O));
        Move minMove = new Move(Integer.MAX_VALUE);
        for (Board child:children) {
            Move move = max(child, depth+1);

            if (move.getValue() <= minMove.getValue()) { //we choose the child with the min value
                if (( .getValue() == minMove.getValue())) { //if the max value equals the value of the heuristic
                    if (random.nextInt(2) == 0) {
                        minMove.setRow(child.getLastMove().getRow());
                        minMove.setCol(child.getLastMove().getCol());
                        minMove.setValue(move.getValue());
                        if(minMove.getValue()<a){ //checking for pruning
                            a = minMove.getValue();
                            break;
                        }
                    }
                } else {
                    minMove.setRow(child.getLastMove().getRow());
                    minMove.setCol(child.getLastMove().getCol());
                    minMove.setValue(move.getValue());
                    if(minMove.getValue()<a){ //checking for pruning
                        a = minMove.getValue();
                        break;
                    }
                }
            }
        }
        return minMove;
    }
}