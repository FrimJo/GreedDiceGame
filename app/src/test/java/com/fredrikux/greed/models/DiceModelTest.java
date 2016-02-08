package com.fredrikux.greed.models;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class DiceModelTest {


    @Test(expected = Exception.class)
    public void shouldThrowExceptionForCreateDiceWith1Sides() throws Exception {
        new DiceModel(1);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionForCreateDiceWith0Sides() throws Exception {
        new DiceModel(0);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionForCreateDiceWithNegativeSides() throws
            Exception {
        new DiceModel(-1);
    }

    @Test
    public void shallCreateAnewDiceWith6Sides() throws Exception {
        createSixSideDice();
    }

    @Test
    public void shallReturnArandomDiceNr() throws LockedDieException {
        DiceModel d = createSixSideDice();
        d.roll();
        int value = d.getValue();
        assert (value > 0 && value < 7);
    }

    @Test
    public void shallReturnArandomDiceNrFor100Rolls() throws LockedDieException {
        DiceModel d = createSixSideDice();

        int value;
        for(int i = 0; i < 100; i++){
            d.roll();
            value = d.getValue();
            if(value < 1 || value > 6){
                assert false;
            }
        }
        assert true;
    }

    @Test
    public void shallReturnLockedFalseForNewDice() throws Exception {
        DiceModel d = createSixSideDice();
        assertEquals(false, d.isLocked());
    }

    @Test
    public void shallReturnLockedTrueForLockedDice() throws Exception {
        DiceModel d = createSixSideDice();
        d.lock();
        assertEquals(true, d.isLocked());
    }

    @Test
    public void shallReturnLockedfalseForLockedThenUnlockedDice()
            throws Exception {
        DiceModel d = createSixSideDice();
        d.lock();
        d.unlock();
        assertEquals(false, d.isLocked());
    }

    @Test(expected = Exception.class)
    public void shallNotRollDiceIfLocked() throws LockedDieException {
        DiceModel d = createSixSideDice();
        d.lock();
        d.roll();
    }

    private final Random r = new Random(System.currentTimeMillis());

    @Test
    public void shallSetDiceToValueRandomNumber() {
        DiceModel d = createSixSideDice();
        int rNr = r.nextInt(6) + 1;
        d.setValue(rNr);
        int newNr = d.getValue();
        assertEquals(rNr, newNr);
    }

    private DiceModel createSixSideDice() {
        return new DiceModel(6);
    }
}