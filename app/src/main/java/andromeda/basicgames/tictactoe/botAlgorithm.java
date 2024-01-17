package andromeda.basicgames.tictactoe;

import android.widget.TextView;

import androidx.annotation.NonNull;

public class botAlgorithm extends MainActivity{

    //This program will analyse the boxes entered by the user and try to make it hard for the player to compete against it.
    //This program consists of the minimax algorithm and may use Alpha-Beta pruning to reduce CPU load.

    protected char[] grid = new char[9];

    protected int loop = 0;
    protected int[] score, computerMove; //keeps the track of computer played move, for which the algorithm wins

    private int miniMax(TextView[] board){

        //This is the main function which will be called
        //It takes care of the recursion of the algorithmic function

        //Gather the number of possible outcomes
        int availableBoxes = emptyBoxes(grid);

        //Iterate the algorithm to get different outcomes
        for(int i = 0; i < availableBoxes; i++)
            computerTurn(board); //Kind-of replicates recursive operation

        //Determine the best move to be taken by the computer
        int max = 0;
        //Get the highest score move
        for (int j : score)
            if (score[max] < j)
                max = j;
        return computerMove[max];
    }

    private void initialise(@NonNull TextView[] boxes){

        // This method will extract the text from the main grid and copies it to a temporary array
        // this will ensure the algorithm doesn't modify the main grid, which caused error in previous versions.

        for(int i = 0; i < boxes.length; i++){
            grid[i] = boxes[i].getText().charAt(0);
        }
    }

    protected int emptyBoxes(char[] arena){
        int availableMoves = 0;
        for(int i = 0; i < 9; i++)
            if(arena[i] != userChoice && arena[i] != computerChoice)
                availableMoves++;
        return availableMoves;
    }

    //This method will directly fill the score and computer moves into the respective variables.
    private void computerTurn(TextView[] boxes) {

        //This method will be called from the MAIN program when it is computer's turn
        // It'll use the minimax algorithmic procedure to find the best possible move.

        initialise(boxes);  //Create a copy of the grid

        //Make computer play a turn using random number
        // check for empty boxes
        // then ask the other player to make move
        //and repeat check for empty boxes

        int pMove;
        int cMove;
        int turns = 0;
        boolean exit = false;
        int[] trackCMove = new int[9];
        do {
            boolean repeatMove = true;
            do {
                //if TRUE, loop continues as the box is not available
                cMove = (int) Math.round(Math.random() * 10); //Returns a random number 0-9

                //Check if the computer is repeating the same move again, if yes, reject that move.
                for (int j : trackCMove) {
                    repeatMove = j == cMove;
                    if (repeatMove){
                        exit = true;
                        break;
                    }
                }

                //check for availability
            } while (emptyBox(cMove) && exit); //if box is AVAILABLE, loop exits and it is not a repeated choice.

            //Computer makes move
            grid[cMove] = computerChoice;
            trackCMove[turns++] = cMove;

            //Store the computer's first move
            if (loop == 0) {
                computerMove[cMove] = cMove;
                loop++;
            }

            do {
                pMove = (int) Math.round(Math.random() * 10); //Returns a random number 0-9
                //check for availability
            } while (emptyBox(pMove)); //Box is AVAILABLE, loop exits.

            //Anti-computer makes a move
            grid[pMove] = userChoice;

        }while(!emptyBox(75));
        //sending 75 to check if any box is empty, if none are empty, evaluation begins
        //else the game (loop) continues

        //Resetting loop variable
        loop = 0;

        //Evaluation of the board
        score[cMove] = score();
    }

    protected int score(){

        //This method checks who wins the game and returns the respective score.
        if(checkWin(grid) == userChoice)
            return 10;
        else if(checkWin(grid) == computerChoice)
            return -10;
        else
            return 0;
    }

    //returns who has won
    protected char checkWin(@NonNull char[] grid){

        //This method will check the grid for who has won the game, and returns TRUE if player wins else FALSE
        //The grid has nine boxes so, 9 values in the grid[] array.

        //Check for horizontal line formation
        if(grid[0] == grid[1] && grid[1] == grid[2])
            return grid[0];
        if(grid[3] == grid[4] && grid[4] == grid[5])
            return grid[3];
        if(grid[6] == grid[7] && grid[7] == grid[8])
            return grid[6];

        //Check for vertical line formation
        if(grid[0] == grid[3] && grid[3] == grid[6])
            return grid[0];
        if(grid[1] == grid[4] && grid[4] == grid[7])
            return grid[1];
        if(grid[2] == grid[5] && grid[5] == grid[8])
            return grid[0];

        //check for diagonal line formation
        if(grid[0] == grid[4] && grid[4] == grid[8])
            return grid[0];
        if(grid[2] == grid[4] && grid[4] == grid[6])
            return grid[2];

        //Adding a default return statement, for a draw ending
        return ' ';
    }

    private boolean emptyBox(int gridLocation){

        //This method will check for empty boxes, if none return game ended.
        //send 75 for checking if any box is empty, else send any number 0-9 to check if it is available

        if(gridLocation == 75) {
            //check any box is empty
            int i;
            for (i = 0; i < 9; i++)
                if (grid[i] != userChoice && grid[i] != computerChoice)
                    break; //Box is EMPTY
            // the location of empty box
            return i == 9; //All boxes are full

        }
        else //check 'GRID LOCATION' is empty
            return grid[gridLocation] == userChoice || grid[gridLocation] == computerChoice;
    }

//    private void fillBox(int location, boolean player){
//
//        //This method will fill the boxes based on the player's turn: FALSE for user & TRUE for computer
//
//        grid[location] = player? userChoice : computerChoice;
//    }
}
