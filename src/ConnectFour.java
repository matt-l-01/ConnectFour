//Matt Love and Bradley Depippo
import java.util.Scanner;

public class ConnectFour {
    private Board board;
    private GameState s;
    private Scanner scan;

    public ConnectFour() {
        board = new Board();
        s = GameState.RUNNING;
        scan = new Scanner(System.in);
    }

    //Method that runs the actual program
    public void run() {
        System.out.println("Welcome to Connect Four! Get ready to play...");
        while(s == GameState.RUNNING) {
            if(board.checkWin()) {
                System.out.println("-----------------------------------");
                System.out.println();
                System.out.println("            WINNER: " + board.currentWinner());
                System.out.println();
                System.out.println("-----------------------------------");
                s = GameState.ENDGAME;
                break;
            }

            if(board.checkTie()) {
                System.out.println("-----------------------------------");
                System.out.println();
                System.out.println("                TIED");
                System.out.println();
                System.out.println("-----------------------------------");
                s = GameState.ENDGAME;
                break;
            }

            //Gets correct input from user
            int correctInput = 0;
            while(correctInput == 0) {
                System.out.println("Player \"" + board.currentPlayer() + "\", choose your column:");
                int colm = scan.nextInt();
                if(board.move(colm, board.findRow(colm)) == 0) {
                    board.printBoard();
                    correctInput = 1;
                } else if (board.move(colm, board.findRow(colm)) == 1) {
                    System.out.println("-----------------------------------");
                    System.out.println("That column is filled up, try again!");
                    System.out.println("-----------------------------------");
                } else {
                    System.out.println("-----------------------------------");
                    System.out.println("That is not a correct range (0-6)!");
                    System.out.println("-----------------------------------");
                }
            }
        }
    }
}

class Board {
    private Box[][] boxes;
    private int count;

    public Board() {
        boxes = new Box[6][7];
        initBoxes();
        count = 0;
    }

    private void initBoxes() {
        for(int i = 0; i < boxes.length; i++) {
            for(int j = 0; j < boxes[i].length; j++) {
                boxes[i][j] = new Box(BoxType.NONE);
            }
        }
    }

    //Error Codes
    // 0 - Correct, 1 - Spot taken, 2 - Invalid Range
    //Returns these codes to send error message to player
    public int move(int c, int r) {
        if(r == -2)
            return 2;

        if(r == -1) {
            return 1;
        }

        if(count == 0) {
            boxes[r][c] = new Box(BoxType.O);
            count = 1;
            return 0;
        } else {
            boxes[r][c] = new Box(BoxType.X);
            count = 0;
            return 0;
        }
    }

    //When given a column, finds an open row spot
    public int findRow(int c) {
        //Returns -2 if out of bounds
        if(c > 6 || c < 0)
            return -2;

        for(int i = 5; i >= 0; i--)
            if(boxes[i][c].getBoxType() == BoxType.NONE)
                return i;
        return -1;
    }

    public String currentPlayer() {
        if(count == 0) {
            return "O";
        }
        return "X";
    }

    //Runs opposite of current player as it is updated before win is checked
    public String currentWinner() {
        if(count == 1) {
            return "O";
        }
        return "X";
    }

    //Checks if a user has won the game
    public boolean checkWin() {
        //Checks horizontal
        for(int i = 0; i < boxes.length; i++) {
            for(int j = 0; j < boxes[i].length - 3; j++) {
                if(boxes[i][j].getBoxType() == boxes[i][j+1].getBoxType()
                        && boxes[i][j+1].getBoxType() == boxes[i][j+2].getBoxType()
                        && boxes[i][j+2].getBoxType() == boxes[i][j+3].getBoxType()
                        && boxes[i][j].getBoxType() != BoxType.NONE) {
                    return true;
                }
            }
        }

        //Checks vertical
        for(int i = 0; i < boxes[0].length; i++) {
            for(int j = 0; j < boxes.length - 3; j++) {
                if(boxes[j][i].getBoxType() == boxes[j+1][i].getBoxType()
                        && boxes[j+1][i].getBoxType() == boxes[j+2][i].getBoxType()
                        && boxes[j+2][i].getBoxType() == boxes[j+3][i].getBoxType()
                        && boxes[j][i].getBoxType() != BoxType.NONE) {
                    return true;
                }
            }
        }

        //Checks diagonal: L to R
        for (int i = boxes.length - 1; i >= 3; i--) {
            for (int j = 0; j <= 3; j++) {
                if (boxes[i][j].getBoxType() == boxes[i-1][j+1].getBoxType()
                        && boxes[i-1][j+1].getBoxType() == boxes[i-2][j+2].getBoxType()
                        && boxes[i-2][j+2].getBoxType() == boxes[i-3][j+3].getBoxType()
                        && boxes[i][j].getBoxType() != BoxType.NONE) {
                    return true;
                }
            }
        }

        //Checks diagonal: R to L
        for (int i = boxes.length - 1; i >= 3; i--) {
            for (int j = 3; j <= boxes[i].length - 1; j++) {
                if (boxes[i][j].getBoxType() == boxes[i-1][j-1].getBoxType()
                        && boxes[i-1][j-1].getBoxType() == boxes[i-2][j-2].getBoxType()
                        && boxes[i-2][j-2].getBoxType() == boxes[i-3][j-3].getBoxType()
                        && boxes[i][j].getBoxType() != BoxType.NONE) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkTie() {
        for(int i = 0; i < boxes.length; i++) {
            for(int j = 0; j < boxes[i].length; j++) {
                if(boxes[i][j].getBoxType() == BoxType.NONE)
                    return false;
            }
        }
        return true;
    }

    public void printBoard() {
        System.out.println("-----------------------------");
        for(int i = 0; i < boxes.length; i++) {
            for(int j = 0; j < boxes[i].length; j++) {
                System.out.print("| " + boxes[i][j].getBoxType().toString() + " ");

                if(j == boxes[i].length - 1)
                    System.out.print("|");
            }
            System.out.println();
            System.out.println("-----------------------------");
        }
    }
}

class Box {
    private BoxType bt;

    public Box(BoxType bt) {
        this.bt = bt;
    }

    public BoxType getBoxType() {
        return bt;
    }
}

enum GameState {
    RUNNING, ENDGAME
}

enum BoxType {
    X {
        public String toString() {
            return "X";
        }
    }, O {
        public String toString() {
            return "O";
        }
    }, NONE {
        public String toString() {
            return "-";
        }
    }
}