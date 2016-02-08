package com.fredrikux.greed.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fredrikux.greed.R;

/**
 * This activity is show when a player reaches the goal amount of points
 * needed to win.
 */
public class VictoryActivity extends AppCompatActivity {

    private TextView mWinnerText;
    private TextView mWinnerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        // Fetch the text view used in this action
        mWinnerTitle = (TextView) findViewById(R.id.winner_title);
        mWinnerText = (TextView) findViewById(R.id.winner_text);


        // If we have saved instances
        if(savedInstanceState != null){

            mWinnerText.setText(savedInstanceState.getString("winnerText"));
            mWinnerTitle.setText(savedInstanceState.getString("winnerTitle"));

        }

        // Else set up the text views as normal using the intents extra values
        else {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {

                int turns = extras.getInt("turns");
                String winner = extras.getString("winner");

                mWinnerText.setText(winner);
                mWinnerTitle.setText("The winner after " + turns + " turns is");
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        String winnerTitle = mWinnerTitle.getText().toString();
        String winnerText = mWinnerText.getText().toString();


        outState.putString("winnerTitle", winnerTitle);
        outState.putString("winnerText", winnerText);

        super.onSaveInstanceState(outState);
    }
}
