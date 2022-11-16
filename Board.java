import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int X = 1;
    public static final int O = -1;
    public static final int EMPTY = 0;
    public static final int AVAILABLE = 100;


    private int [][] gameBoard;
    private int lastLetterPlayed;
    private Move lastMove;

    public Board() { //constructor. Creates the default board.
        gameBoard = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i==3 && j==3) || (i==4 && j==4) )  {
                    gameBoard[i][j] = X;
                } else if((i==3 && j==4) ||(i==4 && j==3) ) {
                    gameBoard[i][j] = O;
                } else {
                    gameBoard[i][j] = EMPTY;
                }
            }
        }
    }

    public Board(Board board) { //constructor, used when making children(copies of the board)
        lastMove = board.lastMove;
        lastLetterPlayed = board.lastLetterPlayed;
        gameBoard = new int[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                gameBoard[row][col] = board.gameBoard[row][col];
            }
        }
    }

    public void setLastLetterPlayed(int lastLetterPlayed)
    {
        this.lastLetterPlayed = lastLetterPlayed;
    }

    public int getLastLetterPlayed()
    {
        return lastLetterPlayed;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void makeMove(int row, int col, int letter, List<String> dir) {
        gameBoard[row][col] = letter;
        lastMove = new Move(row, col);
        lastLetterPlayed = letter;

        int size = dir.size();
        for (int i = 0; i < size; i++ ) {
            int r = row;
            int c = col;
            if (dir.get(0).equals("u")) {
                dir.remove(0);
                while (gameBoard[r - 1][c] == -letter) {
                    gameBoard[r - 1][c] = letter; //converts the checks
                    r--;
                }
            }
            else if (dir.get(0).equals("ul")) {
                dir.remove(0);
                while (gameBoard[r - 1][c - 1] == -letter) {
                    gameBoard[r - 1][c - 1] = letter;
                    r--;
                    c--;
                }
            }
            else if (dir.get(0).equals("l")) {
                dir.remove(0);
                while (gameBoard[r][c - 1] == -letter) {
                    gameBoard[r][c - 1] = letter;
                    c--;
                }
            }
            else if (dir.get(0).equals("dl")) {
                dir.remove(0);
                while (gameBoard[r + 1][c - 1] == -letter) {
                    gameBoard[r + 1][c - 1] = letter;
                    r++;
                    c--;
                }
            }
            else if (dir.get(0).equals("d")) {
                dir.remove(0);
                while (gameBoard[r + 1][c] == -letter) {
                    gameBoard[r + 1][c] = letter;
                    r++;
                }
            }
            else if (dir.get(0).equals("dr")) {
                dir.remove(0);
                while (gameBoard[r + 1][c + 1] == -letter) {
                    gameBoard[r + 1][c + 1] = letter;
                    r++;
                    c++;
                }
            }
            else if (dir.get(0).equals("r")) {
                dir.remove(0);
                while (gameBoard[r][c + 1] == -letter) {
                    gameBoard[r][c + 1] = letter;
                    c++;
                }
            }
            else if (dir.get(0).equals("ru")) {
                dir.remove(0);
                while (gameBoard[r - 1][c + 1] == -letter) {
                    gameBoard[r - 1][c + 1] = letter;
                    r--;
                    c++;
                }
            }
        }
    }

    public void print() {
        //print board
        System.out.print("\n\n");
        System.out.print("  ");
        int i,j;
        for (i = 1; i <= 8; i++) {
            System.out.print( i+ "   "); //prints the first row of numbers
        }
        System.out.print("\n");
        System.out.print(" ");
        for (i = 1; i <= 8; i++) { //counts rows
            System.out.print("--------------------------------\n");
            for (j = 1; j <= 9; j++) {
                if ( j == 9) {
                    System.out.print("| ");
                    break;
                } else
                    System.out.print("| ");

                switch (gameBoard[i-1][j-1])
                {
                    case AVAILABLE:
                        System.out.print("* ");
                        break;
                    case X:
                        System.out.print("X ");
                        break;
                    case O:
                        System.out.print("O ");
                        break;
                    case EMPTY:
                        System.out.print("  ");
                        break;
                    default:
                        break;
                }

            }
            System.out.print(i);
            System.out.print("\n");
        }
        System.out.print("--------------------------------");
        System.out.print("\n\n");

    }

    public ArrayList<Board> getChildren(int letter) {
        ArrayList<Board> children = new ArrayList<Board>();
        for(int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                List<String> dir = directions(row,col, letter);
                if (dir.size() > 0) {
                    Board child = new Board(this);
                    child.makeMove(row, col, letter, dir);
                    children.add(child);
                }
            }
        }
        return children;
    }

    //the board is full with no available space
    //the board has checkers of one color only
    //no player can make a valid move
    public boolean isTerminal()
    {
        if (boardIsFull() || onlyOneColor() || notValidMoves()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean boardIsFull(){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (gameBoard[row][col] == EMPTY || gameBoard[row][col] == AVAILABLE ) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean onlyOneColor(){
        int letter = EMPTY;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (gameBoard[row][col] != EMPTY || gameBoard[row][col] != AVAILABLE) {
                    letter =  gameBoard[row][col];
                    break;
                }
            }
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (gameBoard[row][col] != EMPTY || gameBoard[row][col] != AVAILABLE && gameBoard[row][col] != letter) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean notValidMoves() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ( directions(row, col, O).size() > 0 || directions(row, col, X).size() > 0 ) { //if there is a valid move for X or for O
                    return false;
                }
            }
        }
        return true;
    }

    public int heuristic() {
        int[] scores = increaseScore();
        //score[0] contains the score of player X
        //score[1] contains the score of player O

        //checking if someone does not have a check at the board
        if (scores[0] == 0)
            scores[1] = 10000;
        else if (scores[1] == 0)
            scores[0] = 10000;

        //2)checking corners
        if (gameBoard[0][0] == X || gameBoard[0][7] == X || gameBoard[7][0] == X || gameBoard[7][7] == X) {
            scores[0] = scores[0] + 100;
        } else if (gameBoard[0][0] == O || gameBoard[0][7] == O || gameBoard[7][0] == O || gameBoard[7][7] == O) {
            scores[1] = scores[1] + 100;
        }

        //3) checking left line
        for (int row=0; row<8; row++) {
            if (gameBoard[row][0] == X) {
                scores[0] = scores[0] + 10;
            } else if (gameBoard[row][0] == O) {
                scores[1] = scores[1] + 10;
            }
        }

        //4) checking right line
        for (int row=0; row<8; row++) {
            if (gameBoard[row][7] == X ) {
                scores[0] = scores[0] + 10;
            } else if (gameBoard[row][7] == O ) {
                scores[1] = scores[1] + 10;
            }
        }

        //5) checking up line
        for (int col=0; col<8; col++) {
            if (gameBoard[0][col] == X) {
                scores[0] = scores[0] + 10;
            } else if (gameBoard[0][col] == O) {
                scores[1] = scores[1] + 10;
            }
        }

        //6) checking down line
        for (int col=0; col<8; col++) {
            if (gameBoard[7][col] == X) {
                scores[0] = scores[0] + 10;
            } else if (gameBoard[7][col] == O) {
                scores[1] = scores[1] + 10;
            }
        }
        return scores[0]-scores[1];
    }

    public boolean availableMoves(int letter, boolean player) {
        boolean available = false;
        for (int i = 0; i<8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameBoard[i][j] == AVAILABLE) { //converts all the previous available positions to empty
                    gameBoard[i][j] = EMPTY;
                }
                if (!player) { //show the available moves only if it is player's turn to play
                    if (directions(i, j, letter).size() > 0) { //if there is an available move
                        gameBoard[i][j] = AVAILABLE;
                        available = true;
                    }
                }
            }
        }
        return available;
    }

    public List<String> directions(int moveRow, int moveCol, int letter) {

        List<String> directions = new ArrayList<String>();

        if (gameBoard[moveRow][moveCol ] == EMPTY || gameBoard[moveRow][moveCol ] == AVAILABLE) { //empty position, can put a checker
            //first we check in which neighbor position there is the opposite letter

            //box above the given position
            if (moveRow != 0 && moveRow != 1) {
                if (gameBoard[moveRow - 1][moveCol] == (-letter))  //if letter = X:1 then O:(-X)
                    if (isValid(moveRow - 1, moveCol, letter, "u") != null) { //if it is not a valid move, it does not add the direction in the list
                        directions.add("u"); //as a parameter, gives the position of the found X);
                    }
            }
            //upper left box
            if (moveRow != 0 && moveRow != 1 && moveCol != 0 && moveCol != 1) {
                if (gameBoard[moveRow - 1][moveCol - 1] == (-letter))
                    if (isValid(moveRow - 1, moveCol -1, letter, "ul") != null) {
                        directions.add("ul");
                    }
            }
            //left position
            if (moveCol != 0 && moveCol != 1) {
                if (gameBoard[moveRow][moveCol - 1] == (-letter))
                    if (isValid(moveRow , moveCol - 1, letter, "l") != null) {
                        directions.add("l");
                    }
            }
            //down left box
            if (moveRow != 7 && moveRow !=6 && moveCol != 0 && moveCol !=1) {
                if (gameBoard[moveRow + 1][moveCol - 1] == (-letter))
                    if (isValid(moveRow + 1, moveCol - 1, letter, "dl") != null) {
                        directions.add("dl");
                    }
            }
            //down box
            if (moveRow != 7 && moveRow != 6) {
                if (gameBoard[moveRow + 1][moveCol] == (-letter))
                    if (isValid(moveRow + 1, moveCol, letter, "d") != null) {
                        directions.add("d");
                    }
            }
            //down right position
            if (moveRow != 7 && moveRow != 6 && moveCol != 7 && moveCol != 6) {
                if (gameBoard[moveRow + 1][moveCol + 1] == (-letter))
                    if (isValid(moveRow + 1, moveCol + 1, letter, "dr") != null) {
                        directions.add("dr");
                    }
            }
            //right position
            if (moveCol !=7 && moveCol != 6) {
                if (gameBoard[moveRow][moveCol + 1] == (-letter))
                    if (isValid(moveRow, moveCol + 1, letter, "r") != null) {
                        directions.add("r");
                    }
            }
            //right up position
            if (moveRow != 0 && moveRow != 1 && moveCol != 7 && moveCol != 6) {
                if (gameBoard[moveRow - 1][moveCol + 1] == (-letter))
                    if (isValid(moveRow - 1, moveCol + 1, letter, "ru") != null) {
                        directions.add("ru");
                    }
            }
        }
        return directions;
    }

    public String isValid(int row, int col, int letter, String dir) { //letter = current player. Opposites letter cords.
        if (dir.equals("u")) {
            while (gameBoard[row-1][col] != letter) { //if you are at row 0, do not go up
                row--;
                if ((row-1) < 0 || gameBoard[row][col] == EMPTY || gameBoard[row][col] == AVAILABLE)
                    return null;
            }
            return "u";
        } else if (dir.equals("ul")) {
            while (gameBoard[row-1][col-1] != letter ) { //row 0 or col 0
                row--;
                col--;
                if ( (row-1) < 0 || (col-1) < 0 || gameBoard[row][col] == EMPTY || gameBoard[row][col] == AVAILABLE)
                    return null;
            }
            return "ul";
        } else if (dir.equals("l")) {
            while (gameBoard[row][col-1] != letter ) { //col 0
                col--;
                if ((col-1) < 0 || gameBoard[row][col] == EMPTY || gameBoard[row][col] == AVAILABLE)
                    return null;
            }
            return "l";
        } else if (dir.equals("dl")) {
            while (gameBoard[row+1][col-1] != letter ) { //row 7 or col 0
                row++;
                col--;
                if ((row+1) > 7 || (col-1) < 0 || gameBoard[row][col] == EMPTY || gameBoard[row][col] == AVAILABLE)
                    return null;
            }
            return "dl";
        } else if (dir.equals("d")) {
            while (gameBoard[row+1][col] != letter ) { //row 7
                row++;
                if ( (row+1) > 7 || gameBoard[row][col] == EMPTY || gameBoard[row][col] == AVAILABLE)
                    return null;
            }
            return "d";
        } else if (dir.equals("dr")) {
            while (gameBoard[row+1][col+1] != letter ) { //row 7 or col 7
                row++;
                col++;
                if ((row+1) > 7 || (col+1) > 7 || gameBoard[row][col] == EMPTY || gameBoard[row][col] == AVAILABLE  )
                    return null;
            }
            return "dr";
        } else if (dir.equals("r")) {
            while (gameBoard[row][col+1] != letter  ) { //col 7
                col++;
                if ((col+1) > 7 || gameBoard[row][col] == EMPTY || gameBoard[row][col] == AVAILABLE)
                    return null;
            }
            return "r";
        } else {
            while (gameBoard[row-1][col+1] != letter  ) { //row 7 or col 0
                row--;
                col++;
                if ((row-1) < 0 || (col+1) > 7 || gameBoard[row][col] == EMPTY || gameBoard[row][col] == AVAILABLE )
                    return null;
            }
            return "ru";
        }
    }

    public void score() {
        int[] scores = increaseScore();

        System.out.println("SCORE -->   X = " + scores[0] + "  ,   O = " + scores[1] + "\n");
    }

    public void winner() {
        int[] scores = increaseScore();

        System.out.println("Game Over!\n");
        if (scores[0] > scores[1]) {
            System.out.println("The Winner is X!");
        }else if (scores[0] < scores[1]){
            System.out.println("The Winner is O!");
        } else {
            System.out.println("Draw");
        }
    }

    public int[] increaseScore() { //calculates the amount of checks of each player
        int[] scores = new int[2];
        int scoreX = 0;
        int scoreO = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (gameBoard[row][col] == X) {
                    scoreX++;
                } else if (gameBoard[row][col] == O) {
                    scoreO++;
                }
            }
        }
        scores[0] = scoreX;
        scores[1] = scoreO;

        return scores;
    }
}
