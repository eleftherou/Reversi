import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String args[]) throws InterruptedException {

        //choosing number of players
        System.out.println("\nChoose number of players(0 or 1): ");
        Scanner input = new Scanner(System.in);
        int numOfPlayers = input.nextInt();
        while (numOfPlayers != 1 && numOfPlayers != 0 ) {
            System.out.println("\nTry again. If you want to play press 1, else press 0!");
            input = new Scanner(System.in);
            numOfPlayers = input.nextInt();
        }

        int numOfTurn=0;
        if  (numOfPlayers == 1 ) { //if the player wants to play
            //choosing first player
            System.out.println("\nIf you want to play first press 1, else press 2.");
            input = new Scanner(System.in);
            numOfTurn = input.nextInt();
            while (numOfTurn != 1 && numOfTurn != 2) {
                System.out.println("Try again. If you want to play first press 1, else press 2.");
                input = new Scanner(System.in);
                numOfTurn = input.nextInt();
            }
        }

        //choosing depth
        System.out.println("\nNow choose the depth of search: ");
        input = new Scanner(System.in);
        int depth = input.nextInt();

        while (depth <= 0) {
            System.out.println("\nTry again. Choose the depth of search (>0). ");
            input = new Scanner(System.in);
            depth = input.nextInt();
        }

        //creating players
        GamePlayer XPlayer = new GamePlayer(depth, Board.X);
        GamePlayer OPlayer = new GamePlayer(depth, Board.O);

        //creating board
        Board board = new Board();

        //O --> Player
        //X --> Computer
        if (numOfTurn==1) { // if the player wants to play first
            board.setLastLetterPlayed(Board.X); //player plays first
        } else if(numOfTurn==2){ //if the player wants to play second
            board.setLastLetterPlayed(Board.O);//computer plays first
        } else if (numOfPlayers == 0) {
            int turn = new Random().nextBoolean() ? -1 : 1;
            board.setLastLetterPlayed(turn); //if the player does not play, we randomly choose who plays first
        }

        if (!(numOfPlayers ==1) || !(numOfTurn == 1)) { //if the player does not play first or does not play at all, we print the default board, without the available moves
            board.print();
            TimeUnit.SECONDS.sleep(1); //if you do not want a time delay, comment out this line
        }


        boolean term = board.isTerminal();
        while(!term) {
            System.out.println();
            switch (board.getLastLetterPlayed()) {
                //If X played last, then O plays now
                case Board.X:
                    if (numOfPlayers == 1) { //if it's player's turn
                        boolean available;
                        System.out.println("It's your turn\n");
                        available = board.availableMoves(Board.O, false); //update the values of the available moves
                        board.print(); //print the board and show the available moves

                        if (!available) { //if there is not an available move, pass
                            board.setLastLetterPlayed(Board.O);
                            System.out.println("You don't have any available moves at the moment. Wait for the next round.\n");//pass
                            break; //next player's turn
                        }

                        System.out.println("Choose the coordinates");

                        int moveRow, moveCol;
                        boolean valid = false;

                        while (!valid) { //player might not be able to make this move
                            System.out.println("\nChoose the row (1-8): ");
                            moveRow = input.nextInt();
                            while (moveRow < 1 || moveRow > 8) {
                                System.out.println("\nTry again. The row must be between 1 and 8.");
                                moveRow = input.nextInt();
                            }

                            System.out.println("\nChoose the column (1-8)");
                            moveCol = input.nextInt();
                            while (moveCol < 1 || moveCol > 8) {
                                System.out.println("\nTry again. The column must be between 1 and 8.");
                                moveCol = input.nextInt();
                            }

                            List<String> dir = board.directions(moveRow - 1, moveCol - 1, Board.O); //list with all the possible directions
                            if (dir.size() > 0) { //if size > 0, it means that we have at least 1 available move
                                board.makeMove(moveRow - 1, moveCol - 1, Board.O, dir); //moves according to the coordinates that the player has given
                                valid = true;
                                System.out.println("\n\nYou played: (" + (moveRow) + " , " + (moveCol) + ")");
                            } else
                                System.out.println("\nYou can't make this move, choose valid coordinates\n");
                        }
                    } else { //if it is O's turn
                        Move OMove = OPlayer.MiniMax(board);
                        if (OMove.getRow() == -1 && OMove.getCol() == -1) { //it didn't find a move, it returns the default move, created by the constructor //checks if player O has available movements
                            board.setLastLetterPlayed(Board.O);
                            System.out.println("Player O : Pass (not available movements)."); //if it doesn't have an available movement and the game is not over yet, he passes
                            break;//next player's turn

                        } else { //if it has at least an available move he plays
                            List<String> dir = board.directions(OMove.getRow(), OMove.getCol(), Board.O);
                            board.makeMove(OMove.getRow(), OMove.getCol(), Board.O, dir);
                            System.out.println("\n\nO played: (" + (OMove.getRow() + 1) + " , " + (OMove.getCol() + 1) + ")");
                        }
                    }
                    board.availableMoves(Board.O, true);
                    if (numOfPlayers == 1) //output when there is a real player
                        System.out.println("\nBoard after you played:");
                    else //output when only computer plays
                        System.out.println("\nBoard after O played:");
                    board.print();
                    TimeUnit.SECONDS.sleep(2); //if you do not want a time delay, comment out this line
                    board.score();
                    break;
                //If O played last, then X plays now
                case Board.O:
                    Move XMove = XPlayer.MiniMax(board);
                    if (XMove.getRow() == -1 && XMove.getCol() == -1) { //checks if player X has available movements
                        board.setLastLetterPlayed(Board.X);
                        System.out.println("Player X : Pass (not available movements)."); //if he doesn't have an available movement and the game is not over, then it passes
                        break;//next player's turn

                    } else { //if it has at least an available move he plays
                        List<String> dir = board.directions(XMove.getRow(), XMove.getCol(), Board.X);
                        board.makeMove(XMove.getRow(), XMove.getCol(), Board.X, dir);
                    }

                    if (numOfPlayers == 0) { //output when only computer plays
                        System.out.println("\n\nX played: (" + (XMove.getRow() + 1) + " , " + (XMove.getCol() + 1) + ")\n");
                        System.out.println("Board after X played:");
                    } else { //output when there is a real player
                        System.out.println("\n\nComputer played: (" + (XMove.getRow() + 1) + " , " + (XMove.getCol() + 1) + ")\n");
                        System.out.println("Board after Computer played:");
                    }
                    board.availableMoves(Board.X, true);
                    board.print();
                    TimeUnit.SECONDS.sleep(2); //if you do not want a time delay, comment out this line
                    board.score();

                    break;
                default:
                    break;
            }

            term = board.isTerminal();
        }//terminal ends
        board.winner();
    }
}