package com.fredrikux.greed.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the model of a dice container, it contains dices and handles all
 * actions performed on them, for example: roll and point counting.
 */
public class DiceContainerModel implements Parcelable {

    private final int STRAIGHT      =   1000;
    private final int THREES        =   100;
    private final int THREES_ONES   =   1000;
    private final int ONES_SINGLE   =   100;
    private final int FIVES_SINGLE  =   50;
    private final List<DiceModel> diceModelList;

    private Listener listener;

    public DiceContainerModel(final int _nrDices,
                              final int _nrSides) throws RuntimeException {
        diceModelList = new ArrayList<>(_nrDices);
        isEnoughDices(_nrDices);
        addDicesToModel(_nrDices, _nrSides);
    }

    private void addDicesToModel(int _nrDices, int _nrSides) {
        for(int i = 0; i < _nrDices; i++){
            diceModelList.add(i, new DiceModel(_nrSides));
        }
    }

    private void isEnoughDices(int _nrDices) {
        if(_nrDices < 1){
            throw new RuntimeException("Needs to be at least one dice.");
        }
    }

    protected DiceContainerModel(Parcel in) {

        DiceModel[] array
                = (DiceModel[]) in.readParcelableArray(DiceModel.class
                .getClassLoader());
        diceModelList = new ArrayList<>(Arrays.asList(array));
    }

    public static final Creator<DiceContainerModel> CREATOR = new Creator<DiceContainerModel>() {
        @Override
        public DiceContainerModel createFromParcel(Parcel in) {
            return new DiceContainerModel(in);
        }

        @Override
        public DiceContainerModel[] newArray(int size) {
            return new DiceContainerModel[size];
        }
    };

    /**
     * Sets a listener for this model to listen for changes to the model.
     *
     * @param _listener the listener to add.
     */
    public void setListener(final Listener _listener){
        listener = _listener;
    }

    /**
     * Toggles whether a dice should be locked or not.
     *
     * @param position the position of the dice to toggle
     * @return the dice new locked status, true for locked false otherwise.
     */
    public boolean toggleDiceLock(int position) {
        boolean locked = diceModelList.get(position).toggleLock();
        notifyDataUpdated();
        return locked;
    }

    /**
     * Rolls all dices in this container.
     */
    public void roll() {
        for(DiceModel dice : diceModelList){
            try {
                dice.roll();
            } catch (LockedDieException e) {
                // If a dice is locked, don't roll it
            }
        }

        notifyDataUpdated();
    }

    /**
     * Unlocks all dices in the container.
     */
    public void unlockAllDices(){
        for(DiceModel dice : diceModelList){
            dice.unlock();
        }
        notifyDataUpdated();
    }

    /**
     * When the model changes, call this method to announce to the listeners
     * that the model has been updated.
     */
    private void notifyDataUpdated() {
        if(listener != null){
            listener.onDataUpdate();
        }
    }


    /**
     * Calculates and returns the points for last roll.
     *
     * @return Total points for last throw.
     */
    public int getPoints(){

        // First: Check to see if it is straight
        if(hasStraight()){
            return STRAIGHT;

        }else {
            int points = 0;

            for(int i = 1; i < diceModelList.get(0).getNrSides() + 1; i++){

                // Second: Check for threes
                int threes = hasThrees(i);
                if(i == 1 && threes > 0){
                    points += THREES_ONES * threes;
                }else{
                    points += THREES * i * threes;
                }
            }

            // Third: Check for singles
            int ones = hasSingles(1);
            points += ones * ONES_SINGLE;

            int fives = hasSingles(5);
            points += fives * FIVES_SINGLE;

            return points;
        }

    }

    /**
     * Check to see if there exists three of a kind of provided value.
     * @param _value the value to look for.
     * @return the number of three of a kind found
     */
    public int hasThrees(final int _value) {

        int list = 0;
        for (DiceModel dice : diceModelList){
            if(dice.getValue() == _value){
                list++;
            }
        }

        return (list - (list % 3)) / 3;
    }

    /**
     * Checks to see how meany singles provided value has.
     * @param _value the value to look for.
     * @return the number of singles found.
     */
    public int hasSingles(final int _value) {

        int ret = 0;

        for (DiceModel dice : diceModelList){
            if(dice.getValue() ==_value){
                ret++;
            }
        }

        int threes = hasThrees(_value);

        return ret - threes*3;
    }

    /**
     * Checks to see whether there exists a straight.
     *
     * @return true if it does, false otherwise.
     */
    public boolean hasStraight() {

        int[] straight = new int[diceModelList.size()];

        for (DiceModel dice : diceModelList){

            // Get the index to use for the dice number
            int index = dice.getValue() - 1;

            // Increment that index by one
            straight[index]++;

            // If the new value is bigger then on, then no straight, return
            // false.
            if(straight[index] > 1){
                return false;
            }
        }

        // If none of the values are bigger then 1, then we have a straight.
        return true;
    }

    /**
     * Fetches a {@code int} array containing all de values of de dices.
     *
     * @return a {@code int} array containing all de values of de dices.
     */
    public int[] getValues() {

        int[] values = new int[diceModelList.size()];

        for(int i = 0; i < diceModelList.size(); i++){
            values[i] = diceModelList.get(i).getValue();
        }
        return values;
    }

    /**
     * Set a dices value.
     *
     * @param value the value to set.
     * @param dice the dice to set the value for, zero-based.
     */
    public void setValueForDice(final int value, final int dice) {
        diceModelList.get(dice).setValue(value);
    }

    /**
     * Fetches the number of dices in the container.
     * @return number of dices.
     */
    public int getNrDices() {
        return diceModelList.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(diceModelList.toArray(new
                Parcelable[diceModelList.size()
                ]), flags);
    }

    /**
     * Fetches the value on a dice at specific position.
     * @param position the position of the dice.
     * @return the value of the dice.
     */
    public int getValue(final int position) {
        return diceModelList.get
                (position).getValue();
    }

    /**
     * Checks to see whether a dice at certain position is locked or not.
     * @param position the position fo the dice.
     * @return true if the dice is locked, false otherwise.
     */
    public boolean isLocked(final int position) {
        return diceModelList.get(position).isLocked();
    }

    /**
     * Returns the size of the model.
     *
     * @return the model size.
     */
    public int size() {
        return diceModelList.size();
    }

    /**
     * Gets a specific {@code DiceModel}.
     * @param position the position of the dice in question.
     * @return a {@code DiceModel} object at {@code position}.
     */
    public Object get(final int position) {
        return diceModelList.get(position);
    }

    /**
     * An interface used for listening for events on the {@code
     * DiceContainerModel}
     */
    public interface Listener {
        void onDataUpdate();
    }

}
