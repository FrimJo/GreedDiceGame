package com.fredrikux.greed.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents a single player. This class is {@code Parcelable} so
 * it can easily be recreated.
 */
public class Player implements Parcelable {

    private final String name;
    private int points = 0;

    /**
     * Creates and returns a new {@code Player} object.
     *
     * @param _name the name of the player.
     */
    protected Player(final String _name) {
        name = _name;
    }

    protected Player(Parcel in) {
        name = in.readString();
        points = in.readInt();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    /**
     * Fetches the name of the player.
     * @return plyer name.
     */
    public String getName(){
        return name;
    }

    /**
     * Fetches the total collected points for this player.
     * @return current collected points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Increments this players total points and returns the new amount of
     * total points this player has.
     *
     * @param _points the amount of positive points to increment.
     * @return the new total amount of points this player has.
     * @throws ArithmeticException is thrown when a negative value for {@code
     * _points} are provided.
     */
    public int incrementPoints(int _points) throws ArithmeticException {
        if (_points < 0){
            throw new ArithmeticException("Can't add negative points.");
        }

        return points += _points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(points);
    }
}
