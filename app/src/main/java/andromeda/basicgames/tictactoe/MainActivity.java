package andromeda.basicgames.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    TextView[] box = new TextView[9];
    protected char userChoice, computerChoice;
    protected boolean condition = true;

    private ExecutorService backgroundExecutor;

    LinearLayout row1, row2, row3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialising boxes to track data stored in them
        box[0] = findViewById(R.id.box1);
        box[1] =  findViewById(R.id.box2);
        box[2] =  findViewById(R.id.box3);
        box[3] =  findViewById(R.id.box4);
        box[4] =  findViewById(R.id.box5);
        box[5] =  findViewById(R.id.box6);
        box[6] =  findViewById(R.id.box7);
        box[7] =  findViewById(R.id.box8);
        box[8] =  findViewById(R.id.box9);

        userChoice  = '0';

        row1 = findViewById(R.id.row1);
        row2 = findViewById(R.id.row2);
        row3 = findViewById(R.id.row3);

        //Initialize the Executor Service
        backgroundExecutor = Executors.newSingleThreadExecutor();
    }

    public void start() {
        //User has chosen the computer to start.
        //Disabling the main grid textView from user interaction
        manageAllTextViews(false);

        //Starting a background thread for computing the task
        backgroundExecutor.execute(new Runnable(){
            @Override
            public void run(){
                //Background task starts here
                String boxValue; //to fetch the selected box's content
                int computerBox;
                boolean b;

                //Computer get the other variable always.
                if(userChoice=='X') computerChoice = 'O';
                else computerChoice = 'X';

                do {
                    //Fetch a random number between 0-9
                    computerBox = (int) (Math.random() * 9);

                    //Fetch the content in the box of the previously fetched random number
                    boxValue = box[computerBox].getText().toString();

                    //Check if the content of that box has either one's choice, else continue.
                    b = boxValue.equals(String.valueOf(userChoice)) || boxValue.equals((String.valueOf(computerChoice)));
                }while(b);

                Log.d("Computer choice", "Computer has finished choosing it's decision= "+computerBox);
                Log.d("Fill box request", "box fill request has been initialized");

                //Once the task is completed, this method will provide the output.
                final int finalComputerBox = computerBox;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        box[finalComputerBox].setText(String.valueOf(computerChoice));
                        Log.d("Box fill successful","Box " +finalComputerBox+" bas been filled successfully.");

                        //Allowing the user to interact once the computer has marked a box
                        manageAllTextViews(true);
                    }
                });
            }
        });
    }

    public void chooseX(View view){
        if(condition) {
            userChoice = 'X'; //User has chosen X
            condition = false;
            Toast.makeText(this, "You've chosen "+userChoice+".", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "You've chosen "+userChoice+" already", Toast.LENGTH_SHORT).show();
        //This is to ensure that the user doesn't change his choice in middle of the game :)
    }

    public void chooseO(View view){
        if(condition){
            userChoice = 'O'; //User has chosen O
            condition = false;
            Toast.makeText(this, "You've chosen "+userChoice+".", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "You've chosen "+userChoice+" already", Toast.LENGTH_SHORT).show();
        //This is to ensure that the user doesn't change his choice in middle of the game :)
    }

    public void box(View view){
        //a box has been pressed.
        if(user())
            if(populateBox(view))
                start();

        //Once both the user and computer have given their inputs, we'll check for line formation
        checkForResult();
    }

    protected boolean user(){
        //This method checks if the user has chosen a variable or not.
        if(userChoice=='0') Toast.makeText(this, "Please chose your variable", Toast.LENGTH_LONG).show();
        else return true;
        return false;
    }

    protected boolean populateBox(View view){
        //This method will fill the boxes based on user's inputs.
        String boxText = ((TextView) view).getText().toString();
        if (boxText.equals("O") || boxText.equals("X")) {
            Toast.makeText(this, "This box already has " + boxText + ", choose another box.", Toast.LENGTH_LONG).show();
            return false;
        }
        else ((TextView) view).setText(String.valueOf(userChoice));
        return true;
    }

    //Method to disable/enable click of all textview elements
    protected void manageAllTextViews(boolean condition){
        //Condition: true = available for interaction
        //Condition: false = not available for interaction

        //Managing row 1 elements for interaction
        for(int i = 0; i< row1.getChildCount(); i++){
            View child = row1.getChildAt(i);
            child.setClickable(condition);
        }

        //Managing row 2 elements for interaction
        for(int i = 0; i< row2.getChildCount(); i++){
            View child = row2.getChildAt(i);
            child.setClickable(condition);
        }

        //Managing row 3 elements for interaction
        for(int i = 0; i< row3.getChildCount(); i++){
            View child = row3.getChildAt(i);
            child.setClickable(condition);
        }
    }

    //This method checks for any line formations on the grid, if yes declares that particular variable winner.
    protected void checkForResult(){
        //Reference of grid layout in xml
        /*
        0 1 2
        3 4 5
        6 7 8
         */

        //major problem: since at the beginning of the app, all the boxes have '-' , the app returns user has won.


        //a for loop to check for horizontal line formation at different rows
        for(int i = 0; i < 7; i++) {
            if (!box[i].getText().equals("-"))
                if (box[i].getText().toString().equals(box[i + 1].getText().toString()))
                    if (box[i + 1].getText().toString().equals(box[i + 2].getText().toString()))
                        formationOccurred("Horizontal");
            Log.i("Content of box[i]", box[i].getText().toString());
        }
        Log.e("Horizontal line formation","Failed");

        //a loop to check for horizontal line formation at different columns (ehe, imaginary columns)
        for(int i = 0; i < 3; i++)
            if(!box[i].getText().equals("-"))
                if(box[i].getText().toString().equals(box[i + 3].getText().toString()))
                    if (box[i + 3].getText().toString().equals(box[i + 6].getText().toString()))
                        formationOccurred("Vertical");

        Log.e("Vertical line formation","Failed");

        //Condition to check for diagonal line formation
        if(!box[0].getText().equals("-"))
            if(box[0].getText().toString().equals(box[4].getText().toString()))
                if(box[4].getText().toString().equals(box[8].getText().toString()))
                    formationOccurred("Diagonal");

        if(!box[2].getText().equals("-"))
            if(box[2].getText().toString().equals(box[4].getText().toString()))
                if(box[4].getText().toString().equals(box[6].getText().toString()))
                    formationOccurred("Diagonal");

        Log.e("Diagonal Line formation","failed");
    }

    protected void formationOccurred(String type){
        //Setting all boxes as un-interact-able for user
        manageAllTextViews(false);
        Toast.makeText(this, userChoice+" has won the game with "+type+" attack!!", Toast.LENGTH_SHORT).show();
    }

    //This method is called for restarting the game.
    public void startNewGame(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}