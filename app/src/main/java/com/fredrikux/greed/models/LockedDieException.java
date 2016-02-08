package com.fredrikux.greed.models;

/**
 * A {@code Exception} which is thrown if the value of a dice is being
 * manipulated while being locked.
 */
public class LockedDieException extends Exception {

    public LockedDieException(String s) {
        super(s);
    }
}
