package com.fredrikux.greed.controllers;


import android.os.Parcel;
import android.os.Parcelable;

import com.fredrikux.greed.models.DiceContainerModel;
import com.fredrikux.greed.models.Player;
import com.fredrikux.greed.models.PlayerManager;

/**
 * This is the main controller. It contains the {@code PlayerManager} as well as
 * the {@code DiceContainerModel} which contains all the dices.
 */
public class GameController implements Parcelable{

    public static final int ROUND_PAS = 1;
    public static final int ROUND_NOT_PAS = 2;
    public static final int ROUND_SAVED = 3;
    public static final int NEW_TURN = 4;
    public static final int WINNER = 5;

    public static final int TO_WINN = 10000;


    private final PlayerManager mPlayerManager;
    private final DiceContainerModel mDataModel;

    private Listener mListener;
    private int mCurrentPoints = 0;
    private int mCurrentState = NEW_TURN;

    public GameController(final String[] players,
                          final DiceContainerModel model){
        mPlayerManager = new PlayerManager(players);
        mDataModel = model;
    }

    protected GameController(Parcel in) {
        mPlayerManager = in.readParcelable(PlayerManager.class.getClassLoader());
        mDataModel = in.readParcelable(DiceContainerModel.class.getClassLoader());
        mCurrentPoints = in.readInt();
        mCurrentState = in.readInt();
    }

    public static final Creator<GameController> CREATOR = new Creator<GameController>() {
        @Override
        public GameController createFromParcel(Parcel in) {
            return new GameController(in);
        }

        @Override
        public GameController[] newArray(int size) {
            return new GameController[size];
        }
    };

    public void startGame(){
        if(haveListener()){

            // Set up default values
            Player player = mPlayerManager.getCurrentPlayer();
            mListener.onActionPerformed(NEW_TURN, player, "New turn, player = " +
                    "next player");
            mCurrentState = NEW_TURN;
        }
    }

    /**
     * Sets a listener to listen for events fired from this object.
     *
     * @param listener the listener to set.
     */
    public void setListener(final Listener listener){
        mListener = listener;
    }

    /**
     * Rolls all dices and trigger proper actions for the result.
     */
    public void roll(){

        // Roll the dices
        mDataModel.roll();

        // Get the points produced from this roll
        int points = mDataModel.getPoints();

        // The rolling player is at the first turn
        if(isFirstTurn()){

            // the roll is more then 300
            if(points >= 300){
                passRound(points);
            }

            // The first roll was to low
            else{
                notPassRound(points);
            }
        }

        // This rolled points are more than current points
        else if(points > mCurrentPoints) {
            passRound(points);
        }

        // Else the turn ends
        else{
            notPassRound(points);
        }

    }

    private void notPassRound(int points) {
        mCurrentPoints = points;

        if(haveListener()){
            mListener.onActionPerformed(ROUND_NOT_PAS, mPlayerManager
                    .getCurrentPlayer(), "Current Player");
            mCurrentState = ROUND_NOT_PAS;
        }
    }

    private void passRound(int points) {

        mCurrentPoints = points;

        // Announce that the score changed
        if(haveListener()){
            mListener.onActionPerformed(ROUND_PAS, mPlayerManager
                    .getCurrentPlayer(), "Current Player");
            mCurrentState = ROUND_PAS;
        }
    }

    private boolean isFirstTurn() {
        return mCurrentPoints == 0;
    }

    private boolean isWinner() {
        Player player = mPlayerManager.getCurrentPlayer();
        return player.getPoints() >= TO_WINN;
    }

    /**
     * Saves the points collected this round in to the current players total
     * points.
     */
    public void saveRound(){
        Player player = mPlayerManager.getCurrentPlayer();
        player.incrementPoints(mCurrentPoints);

        if(haveListener()){

            // If we have a winner
            if(isWinner()){
                mListener.onActionPerformed(WINNER, getCurrentPlayer(), "Winner");
                mCurrentState = WINNER;
            }else{
                mListener.onActionPerformed(ROUND_SAVED, player, "Player saved");
                mCurrentState = ROUND_SAVED;
            }
        }
    }

    /**
     * If a dice gets clicked, call this method.
     *
     * @param position the position of the dice which got clicked.
     */
    public void diceClick(final int position) {
        mDataModel.toggleDiceLock(position);
    }

    private boolean haveListener() {
        return mListener != null;
    }

    /**
     * Giv the turn to the next player.
     */
    public void nextPlayer(){
        mCurrentPoints = 0;

        mDataModel.unlockAllDices();
        if(haveListener()){
            Player player = mPlayerManager.setNextPlayer();
            mListener.onActionPerformed(NEW_TURN, player, "New turn, player = next " +
                    "player");
            mCurrentState = NEW_TURN;
        }
    }

    /**
     * Fetches the amount of turns past since the beggining of the game.
     * @return number of turns.
     */
    public int getPassedTurns(){
        return mPlayerManager.getTurns();
    }

    /**
     * Returns the current score for the player currently playing.
     *
     * @return currently playing players current score for this turn.
     */
    public int getCurrentScore(){
        return mCurrentPoints;
    }

    /**
     * fetches the current state of this {@code GameController} object.
     * @return the current state.
     */
    public int getCurrentState() {
        return mCurrentState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mPlayerManager, flags);
        dest.writeParcelable(mDataModel, flags);
        dest.writeInt(mCurrentPoints);
        dest.writeInt(mCurrentState);
    }

    /**
     * Gets the current player playing.
     *
     * @return current player.
     */
    public Player getCurrentPlayer() {
        return mPlayerManager.getCurrentPlayer();
    }

    /**
     *  An interface to listen to events fired in the {@code GameController}.
     */
    public interface Listener {

        void onActionPerformed(final int id, final Object source, final
                               String description);
    }

}
