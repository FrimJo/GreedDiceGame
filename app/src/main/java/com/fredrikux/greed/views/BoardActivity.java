package com.fredrikux.greed.views;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.fredrikux.greed.R;
import com.fredrikux.greed.controllers.DiceContainerController;
import com.fredrikux.greed.controllers.GameController;
import com.fredrikux.greed.helpers.ViewManager;
import com.fredrikux.greed.models.Player;
import com.fredrikux.greed.models.DiceContainerModel;

/**
 * This is the main activity for the game. It contains {@code
 * TextViews} for representing the current state of the game. It also contains
 * a {@code GridView} to represent the values of the dices in the game. It
 * also contains {@code Buttons} to interact with the game.
 */
public class BoardActivity extends AppCompatActivity {

    private final static String TAG = BoardActivity.class.getName();
    private final static int DICES = 6; // Number of dices to use
    private final static int SIDES = 6; // Number of side each dice should have

    private final int[] mTextViews = {R.id.player_text, R.id.score_text,
            R.id.saved_text};

    private final int[] mButtons = {R.id.roll_button, R.id.save_button,
            R.id.end_button};

    private final ViewManager mViewController = new ViewManager();

    // Saved fields to be used within the object
    private MediaPlayer         mMediaPlayer;
    private Bundle              mSavedInstanceState;
    private DiceContainerModel  mDiceContainerModel;
    private GameController      mGameController;
    private GridView            mDiceContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        // Load the media player with the sound
        mMediaPlayer = MediaPlayer.create(BoardActivity.this, R.raw
                .dice);

        // Set up the views
        setUpViews();

        // Save the saved instance state
        mSavedInstanceState = savedInstanceState;

        // Set up the dice container model
        mDiceContainerModel = setupDiceContainerModel();

        // Set up the game controller
        mGameController = setupGameController(mDiceContainerModel);

        // Set up the dice container view
        mDiceContainerView = setupDiceContainerView(mDiceContainerModel);

    }

    private void setUpViews(){

        // Set up the text views
        for(int id : mTextViews){
            mViewController.addView(setUpTextView(id));
        }

        // Create a click listener
        OnClickListener listener = setupClickListener();

        // For each id, create a button and add the listener
        for(int id : mButtons){
            mViewController.addView(setUpButton(id, listener));
        }
    }

    private TextView setUpTextView(final int id){

        return (TextView) findViewById(id);
    }

    private OnClickListener setupClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Fetch the id of the view
                final int id = v.getId();

                // Automatically disable all the button on click
                disableViewsWithIds(mButtons);

                // Run the corresponding method
                switch (id){

                    case R.id.roll_button:

                        if(mMediaPlayer.isPlaying())
                            mMediaPlayer.stop();

                        mMediaPlayer.start();
                        mGameController.roll();

                        // Show all dices
                        mDiceContainerView.setVisibility(View.VISIBLE);
                        break;

                    case R.id.save_button:
                        mGameController.saveRound();
                        break;

                    case R.id.end_button:
                        mGameController.nextPlayer();

                        // Hide all dices
                        mDiceContainerView.setVisibility(View.INVISIBLE);
                    default:
                        break;

                }
            }

            private void disableViewsWithIds(int[] mButtons) {
                for(int buttonId : mButtons){
                    mViewController.disableView(buttonId);
                }
            }
        };
    }

    private Button setUpButton(final int id, OnClickListener listener){

        Button button = (Button) findViewById(id);
        button.setOnClickListener(listener);
        return button;
    }

    private DiceContainerModel setupDiceContainerModel() {
        DiceContainerModel diceContainerModel;

        if(isGameRunning()){
            diceContainerModel = (DiceContainerModel) mSavedInstanceState
                    .get("DiceContainerModel");

        }else{
            diceContainerModel = new DiceContainerModel(DICES, SIDES);
        }
        return diceContainerModel;
    }

    private GameController setupGameController(DiceContainerModel diceContainerModel) {

        GameController gameController;

        // Create the game controller
        if(isGameRunning()){
            gameController = mSavedInstanceState.getParcelable
                    ("GameController");
        } else {
            gameController = new GameController(getPlayers(),
                    diceContainerModel);
        }

        // Set the listener
        gameController.setListener(getGameListener());

        return gameController;
    }

    private String[] getPlayers() {
        String[] players = new String[0];
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            players = extras.getStringArray("players");
        }
        return players;
    }

    private GameController.Listener getGameListener() {

        return new GameController.Listener() {

            @Override
            public void onActionPerformed(int state, Object source, String description) {
                setState(state);
            }
        };
    }


    private void setState(int state){

        Player player = mGameController.getCurrentPlayer();
        String pointsStr = "" + mGameController.getCurrentScore();
        String savedPointsStr = "" + player.getPoints();
        String name = player.getName();

        switch (state){

            case GameController.ROUND_PAS:
                mViewController.enableView(R.id.save_button);
                mViewController.enableView(R.id.roll_button);
                mViewController.enableView(R.id.end_button);
                break;

            case  GameController.ROUND_SAVED:
            case GameController.ROUND_NOT_PAS:

                mViewController.enableView(R.id.end_button);
                break;

            case GameController.NEW_TURN:

                mViewController.enableView(R.id.roll_button);
                break;

            case GameController.WINNER:

                Intent intent = new Intent(BoardActivity.this,
                        VictoryActivity.class);

                int turns = mGameController.getPassedTurns();
                intent.putExtra("turns", turns);
                intent.putExtra("winner", name);
                startActivity(intent);

                break;
        }
        mViewController.setText(R.id.player_text, name);
        mViewController.setText(R.id.score_text, pointsStr);
        mViewController.setText(R.id.saved_text, savedPointsStr);
    }

    private GridView setupDiceContainerView(DiceContainerModel
                                                    diceContainerModel) {

        DiceContainerController diceContainerController
                = new DiceContainerController(BoardActivity.this,diceContainerModel);

        // Get the Dice Container View
        GridView diceContainerView = setupDiceContainerView();

        // Set the adapter for the View
        diceContainerView.setAdapter(diceContainerController);

        // Set a on click item handler for the View
        diceContainerView.setOnItemClickListener(setupItemClickListener());

        return diceContainerView;
    }

    private GridView setupDiceContainerView() {
        GridView diceContainerView = (GridView) findViewById(R.id
                .dice_view);

        if(isGameRunning()){
            int visibility = mSavedInstanceState.getInt("GridView");
            diceContainerView.setVisibility(
                    visibility == 0 ? View.VISIBLE : View.INVISIBLE);
        }

        return diceContainerView;
    }

    private OnItemClickListener setupItemClickListener(){
        return new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mGameController.diceClick(position);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If a game hasn't already started, start a new game.
        if(!isGameRunning()){
            mGameController.startGame();
        }

        // Else get the saved state and set it
        else{
            setState(mGameController.getCurrentState());
        }
    }

    private boolean isGameRunning() {
        return mSavedInstanceState != null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // Saves the game controller which keeps track of the players
        saveGameController(outState);

        // Saves the model used for representing the dice container
        saveDiceContainerModelState(outState);

        // Save grid view hidden state
        outState.putInt("GridView", mDiceContainerView.getVisibility());

        super.onSaveInstanceState(outState);
    }

    private void saveGameController(Bundle outState) {
        outState.putParcelable("GameController", mGameController);
    }

    private void saveDiceContainerModelState(Bundle outState) {
        outState.putParcelable("DiceContainerModel", mDiceContainerModel);
    }

    @Override
    protected void onStop() {
        mMediaPlayer.release();
        mMediaPlayer = null;
        super.onStop();
    }
}
