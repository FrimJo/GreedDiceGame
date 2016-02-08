package com.fredrikux.greed.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * This class keeps track of all the players, it decides which turn it is and
 * also keeps track of how many turns has past.
 */
public class PlayerManager implements Parcelable{

    private final LinkedList<Player> players = new LinkedList<>();
    private int pCurrent = 0;
    private int turnCounter = 1;

    /**
     * Creates and returns a {@code PlayerManager} object containing
     * players with provided names.
     *
     * @param _names a String array containg all players to add at
     *               initialization. Players can also be added later with
     *               {@code addPlayer(String)}.
     */
    public PlayerManager(final String... _names){

        for(int i = 0; i < _names.length; i++){
            addPlayer(_names[i]);
        }
    }

    protected PlayerManager(Parcel in) {
        pCurrent = in.readInt();
        turnCounter = in.readInt();
        Player[] array
                = (Player[]) in.readParcelableArray(Player.class
                .getClassLoader());
        players.addAll(Arrays.asList(array));
    }

    public static final Creator<PlayerManager> CREATOR = new Creator<PlayerManager>() {
        @Override
        public PlayerManager createFromParcel(Parcel in) {
            return new PlayerManager(in);
        }

        @Override
        public PlayerManager[] newArray(int size) {
            return new PlayerManager[size];
        }
    };

    /**
     * Adds a player to the {@code PlayerManager}.
     * @param _name the name of the player.
     */
    public void addPlayer(final String _name) {
        players.add(new Player(_name));
    }

    /**
     * Fetches the number of players in this manager.
     *
     * @return number of players.
     */
    public int getNrPlayers() {
        return players.size();
    }

    /**
     * Returns the player which turn it is.
     *
     * @return the player which turn is is.
     *
     * @throws RuntimeException is thrown if no players are added to the
     * manager.
     */
    public Player getCurrentPlayer() throws RuntimeException {
        if(!players.isEmpty()){
            return players.get(pCurrent);
        }
        throw new RuntimeException("No players added to the game, use addPlayer" +
                "(String)");
    }

    /**
     * Sets the next player as current player.
     *
     * @return the next player.
     *
     * @throws RuntimeException is thrown if no players are added to the
     * manager.
     */
    public Player setNextPlayer() throws RuntimeException {
        if(!players.isEmpty()){
            if(pCurrent == players.size() - 1){
                pCurrent = 0;
                turnCounter++;
            }else{
                pCurrent++;
            }
        }else{
            throw new RuntimeException("No players added to the game, use addPlayer" +
                    "(String)");
        }
        return getCurrentPlayer();
    }

    /**
     * Fetches the turns that has past so far.
     *
     * @return nubmer of turns.
     */
    public int getTurns(){
        return turnCounter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pCurrent);
        dest.writeInt(turnCounter);
        dest.writeParcelableArray(
                players.toArray(new Parcelable[players.size()]), flags);
    }
}
