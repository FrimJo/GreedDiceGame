package com.fredrikux.greed.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * A model representation of a dice. This class is {@code Parcelable} so
 * it can easily be recreated. All methods manipulating the value of the dice
 * are {@code synchronized} to prevent strange behavioral.
 */
public class DiceModel implements Parcelable {

    private final static Random RANDOM = new Random(System.currentTimeMillis());
    private final int nrSides;

    private int value;
    private boolean locked = false;

    public static final Creator<DiceModel> CREATOR = new Creator<DiceModel>() {
        @Override
        public DiceModel createFromParcel(Parcel in) {
            return new DiceModel(in);
        }

        @Override
        public DiceModel[] newArray(int size) {
            return new DiceModel[size];
        }
    };

    /**
     * Creates and returns a {@code DiceModel} object with provided number of
     * sides.
     *
     * @param _nrSides number of sides to use in this dice.
     * @throws RuntimeException is thrown if number of sides is less then two.
     * A dice need to have at least 2 sides.
     */
    protected DiceModel(final int _nrSides) throws RuntimeException {
        if(_nrSides < 2){
            throw new RuntimeException("Can't create a dice with less then 2" +
                    "sides");
        }

        nrSides = _nrSides;
        value = getRandomNr();
    }

    private DiceModel(Parcel in) {
        nrSides = in.readInt();
        value = in.readInt();
    }

    /**
     * Rolls the die and returns the old value, to get the new value: use
     * {@code getValue()}.
     *
     * @throws LockedDieException if the die is locked, it will not be rolled.
     */
    public synchronized void roll() throws LockedDieException {
        if(locked){
            throw new LockedDieException("Can't roll a locked dice");
        }
        value = getRandomNr();
    }

    /**
     * Fetches the value of the dice.
     *
     * @return the dice value.
     */
    public synchronized int getValue(){
        return value;
    }

    /**
     * Toggles the lock on the dice.
     *
     * @return true if the dice is lock after this call, false otherwise.
     */
    public boolean toggleLock() {
        locked = !locked;
        return locked;
    }

    /**
     * Locks the dice.
     */
    public void lock() {
        locked = true;
    }

    /**
     * Checks to see whether the dice is locked or not.
     *
     * @return true if it's locked, false otherwise.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Unlocks the dice.
     */
    public void unlock() {
        locked = false;
    }

    /**
     * Returns a random number between 1 and the number of sides (min 2).
     * @return the random value between 1 and the number of sides (min 2).
     */
    private int getRandomNr(){
        return RANDOM.nextInt(nrSides) + 1;
    }

    /**
     * Sests the value of the dice to a specific value.
     *
     * @param _value the value to set.
     */
    public synchronized void setValue(final int _value) {
        value = _value;
    }

    /**
     * Fetches the number of sides on this dice.
     *
     * @return the number of sides.
     */
    public int getNrSides(){
        return nrSides;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nrSides);
        dest.writeInt(value);
    }
}
