package andromeda.basicgames.tictactoe;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static java.lang.Thread.sleep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class gameArena extends Activity {
    TextView[] box = new TextView[9];
    TextView player1, player2, time;
    private final char[] grid = new char[9];
    protected static char userChoice, computerChoice;
    protected boolean condition = true, control = true;

    private ExecutorService backgroundExecutor, threadTime;

    private int gameEnd = 0;

    LinearLayout row1, row2, row3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_arena);

        //Declaring boxes to track data stored in them
        box[0] = findViewById(R.id.box1);
        box[1] =  findViewById(R.id.box2);
        box[2] =  findViewById(R.id.box3);
        box[3] =  findViewById(R.id.box4);
        box[4] =  findViewById(R.id.box5);
        box[5] =  findViewById(R.id.box6);
        box[6] =  findViewById(R.id.box7);
        box[7] =  findViewById(R.id.box8);
        box[8] =  findViewById(R.id.box9);

        //Initialise all the box's values to be empty
        for(int i = 0; i < 9; i++)
            box[i].setText(null);

        userChoice  = '0';

        //Just for fun
        row1 = findViewById(R.id.row1);
        row2 = findViewById(R.id.row2);
        row3 = findViewById(R.id.row3);

        //Initialising and making the Score boxes visible/invisible
        player1 = findViewById(R.id.player1Score);
        player2 = findViewById(R.id.player2Score);

        //Initialise time box to object
        time = findViewById(R.id.displayTime);

        //Setting them visible/invisible
        player1.setVisibility(View.VISIBLE);
        player2.setVisibility(View.VISIBLE);

        //Initialize the Executor Service
        backgroundExecutor = Executors.newSingleThreadExecutor();
        threadTime = Executors.newSingleThreadExecutor();
    }

    //Once user gives an input, computer will provide it's input using a background thread.
    public void start() {
        backgroundExecutor.execute(() -> {
            // Determine the best move using the Minimax algorithm

            convertIntoChar(box); //This will populate the grid var. with the contents of box[]

            //Obtain the best move from the minimax algorithm with alpha beta pruning.
            int bestMove = Minimax_Alpha_Beta.findBestMove(grid);
            if(bestMove == -1)
            {
                evaluate();
            }
            else {
                // Update the UI with the chosen move
                runOnUiThread(() -> {
                    box[bestMove].setText(String.valueOf(computerChoice));
                    Log.d("Value of computer choice", "=" + computerChoice);
                    Log.d("Box fill successful", "Box " + bestMove + " has been filled successfully.");
                });
            }
            evaluate();
        });
    }

    public void chooseX(View view){
        if(condition) {
            userChoice = 'X'; //User has chosen X
            condition = false;
            computerChoice = 'O';
            Log.d("UserChoice","User has chosen X");
            makeText(this, "You've chosen "+userChoice+".", LENGTH_SHORT).show();

            //Start game time calculation
            control = true;
            threadTime.execute(this::calculateTime);
        } else
            makeText(this, "You've chosen "+userChoice+" already", LENGTH_SHORT).show();
        //This is to ensure that the user doesn't change his choice in middle of the game :)
    }

    public void chooseO(View view){
        if(condition){
            userChoice = 'O'; //User has chosen O
            condition = false;
            computerChoice = 'X';
            Log.d("UserChoice","User has chosen O");
            makeText(this, "You've chosen "+userChoice+".", LENGTH_SHORT).show();

            //Start game time calculation
            control = true;
            threadTime.execute(this::calculateTime);
        }
        else
            makeText(this, "You've chosen "+userChoice+" already", LENGTH_SHORT).show();
        //This is to ensure that the user doesn't change his choice in middle of the game :)
    }

    public void box(View view){
        //a box has been pressed.
        if(user())
            if(populateBox(view))
                if(MainActivity.isBot == 1) start();
                else evaluate();

        Log.d("Inputs provided","both the user and "+(MainActivity.isBot==1?"computer":"player2")+" have given their inputs");
        //Once both the user and computer have given their inputs, we'll check for line formation
        //evaluate();
    }

    protected void evaluate(){
        if(Minimax_Alpha_Beta.isWinner(grid, userChoice)) {
            manageAllTextViews();
            gameEnd = 1;
            control = false;
            runOnUiThread(() -> makeText(this, "User has Won", LENGTH_SHORT).show()
            );

            Log.i("Game Over", "User has won");
        } else if (Minimax_Alpha_Beta.isWinner(grid, computerChoice)) {
            manageAllTextViews();
            gameEnd = 1;
            control = false;
            runOnUiThread(() -> makeText(this, "Computer has Won", LENGTH_SHORT).show());
            Log.i("Game Over", "User has won");

        }
        else if(Minimax_Alpha_Beta.isBoardFull(grid)){
            manageAllTextViews();
            gameEnd = 1; //To imply that he game has ended
            control = false; //To imply that time calculation has to be stopped.
            runOnUiThread(() -> makeText(this, "It is a draw", LENGTH_SHORT).show());
            Log.i("Game Over", "A draw match");
        }
    }

    protected boolean user(){
        //This method checks if the user has chosen a variable or not.
        if(userChoice=='0') makeText(this, "Please chose your variable", LENGTH_LONG).show();
        else return true;
        return false;
    }

    protected boolean populateBox(View view){
        //This method will fill the boxes based on user's inputs.

        String boxText = ((TextView) view).getText().toString();
        if (boxText.equals("O") || boxText.equals("X")) {
            makeText(this, "This box already has " + boxText + ", choose another box.", LENGTH_LONG).show();
            return false;
        }
        else ((TextView) view).setText(String.valueOf(userChoice));
        return true;
    }

    //Method to disable/enable click of all textview elements
    protected void manageAllTextViews(){
        //Condition: true = available for interaction
        //Condition: false = not available for interaction

        if(gameEnd == 1 || Minimax_Alpha_Beta.isBoardFull(grid)){
            for(int i = 0; i < 9; i++)
                box[i].setEnabled(false);
            Log.d("Grid condition changed", "Condition="+false);
        }
        else {
            for(int i = 0; i < 9; i++)
                box[i].setEnabled(true);
            Log.d("Grid condition changed", "Condition="+true);
        }
    }

    //This method will convert the TextView stored content into a character array.
    protected void convertIntoChar(TextView[] box){
        for(int i = 0; i < 9; i++)
            grid[i] = (box[i].getText().toString().equals(""))? ' ': box[i].getText().charAt(0);
    }

    //This method is called for restarting the game.
    public void startNewGame(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        Log.d("Game restart", "The app is restarted.");
    }

    private void calculateTime(){
        //This method will calculate time

        int hr=0, mn=0, ss=0, ms=0;
        while(gameEnd == 0){
            if(control){
                String duration = hr+":"+mn+":"+ss+"."+ms;
                runOnUiThread(() -> time.setText(duration));
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ms += 10;
                if(ms == 60) {
                    ms = 0;
                    ss++;
                }
                else if(ss == 60) {
                    ss = 0;
                    mn++;
                }
                else if(mn == 60) {
                    mn = 0;
                    hr++;
                }
                else if(hr == 24 && mn == 60 && ss == 60 && ms == 100){
                    hr = 0;
                    mn = 0;
                    ss = 0;
                    ms = 0;
                }
            }
            else {
                break;
            }
        }
    }
}
