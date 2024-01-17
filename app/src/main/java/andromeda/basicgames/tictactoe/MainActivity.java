package andromeda.basicgames.tictactoe;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected static char userChoice, computerChoice;
    public gameArena gamearena = new gameArena();
    Button pvc, pvp;

    protected static int visibility = 0, isBot = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pvc = findViewById(R.id.plrVsBot);
        pvp = findViewById(R.id.plrVsComp);
    }

    public void pvp(View view){
        //Code to be executed when it's player vs player

        //Setting the boxes invisible
        visibility = 1;

        //Providing sign that it's player Vs computer
        isBot = 0;

        //Handing over the activity to game_arena
        Intent intent = new Intent(this, gameArena.class);
        startActivity(intent);
    }

    public void pvc(View view){
        //Code to be executed when it's player vs computer

        //Setting the boxes visible
        visibility = 1;

        //Providing sign that it's player Vs player
        isBot = 1;

        //Handing over the activity to game_arena
        Intent intent = new Intent(this, gameArena.class);
        startActivity(intent);
    }
    
    public void history(View view){
        //Need to develop this once the game is in working condition
        Toast.makeText(this, "Should be implemented", Toast.LENGTH_SHORT).show();
    }
}