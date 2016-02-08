package com.fredrikux.greed.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class DiceContainerModelTest {

    @Test(expected = Exception.class)
    public void shallThrowExceptionForZeroDices() throws Exception {
        new DiceContainerModel(0, 6);
    }


    @Test
    public void shallCreateAContainerFor1Dices() throws Exception {
        new DiceContainerModel(1, 6);

        assert true;
    }

    @Test
    public void shallCreateAContainerFor6Dices() throws Exception {

        new DiceContainerModel(6, 6);

        assert true;
    }

    @Test
    public void shallRollAllDicesInContainer() throws Exception {

        // Create the dices and get their original values
        DiceContainerModel container = new DiceContainerModel(6, 6);

        boolean done = false;

        /*
         * To be sure that all dices changes values, do this for most 100 times
         * or until all dices has changed values
         */
        for(int j = 0; done || j < 100; j++){

            // Reroll the dices
            int[] oldValues = container.getValues();
            container.roll();
            int[] newValues = container.getValues();
            done = true;

            // Get their new values
            for(int i = 0; i < oldValues.length; i++){
                if(newValues[i] == oldValues[i]){
                    done = false;
                    break;
                }
            }

            if(done){
                break;
            }

        }

        assert done;
    }

    @Test
    public void shallReroll2and5dice() throws IndexOutOfBoundsException {

        // Create the dices and get their original values
        DiceModel[] dices = new DiceModel[6];
        int[] orgValues = new int[6];
        for(int i = 0; i < dices.length; i++){
            dices[i] = new DiceModel(6);
            orgValues[i] = dices[i].getValue();
        }
        DiceContainerModel container = new DiceContainerModel(6, 6);
        container.toggleDiceLock(0);
        container.toggleDiceLock(2);
        container.toggleDiceLock(3);
        container.toggleDiceLock(5);
        container.roll();

        assert (dices[0].getValue() == orgValues[0]
                && dices[2].getValue() == orgValues[2]
                && dices[3].getValue() == orgValues[3]
                && dices[5].getValue() == orgValues[5]);

    }

    @Test
    public void shallHaveThreesIn1for212141() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(2, 1, 2, 1, 4, 1);
        int value = container.hasThrees(1);
        assertEquals(1, value);
    }

    @Test
    public void shallHaveThreesIn1for111111() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 1, 1, 1, 1, 1);
        int value = container.hasThrees(1);
        assertEquals(2, value);
    }

    @Test
    public void shallHaveThreesIn1for116211() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 1, 6, 2, 1, 1);
        int value = container.hasThrees(1);
        assertEquals(1, value);
    }

    @Test
    public void shallHaveThreesIn4for146244() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 4, 6, 2, 4, 4);
        int value = container.hasThrees(4);
        assertEquals(1, value);
    }

    @Test
    public void shallHave1Single5() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 1, 3, 4, 5, 3);
        int value = container.hasSingles(5);
        assertEquals(1, value);
    }

    @Test
    public void shallHave2Single5() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(5, 1, 3, 4, 5, 3);
        int value = container.hasSingles(5);
        assertEquals(2, value);
    }

    @Test
    public void shallHave0Single5() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(5, 5, 3, 4, 5, 3);
        int value = container.hasSingles(5);
        assertEquals(0, value);
    }

    @Test
    public void shallHave1Single5for4() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(5, 5, 5, 4, 5, 3);
        int value = container.hasSingles(5);
        assertEquals(1, value);
    }

    @Test
    public void shallHave2Single5for5() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(5, 5, 5, 5, 5, 3);
        int value = container.hasSingles(5);
        assertEquals(2, value);
    }

    @Test
    public void shallHave0Single5for6() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(5, 5, 5, 5, 5, 5);
        int value = container.hasSingles(5);

        assertEquals(0, value);
    }

    @Test
    public void shallHave2Single1for5() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 1, 1, 1, 1, 5);
        int value = container.hasSingles(1);
        assertEquals(2, value);
    }

    @Test
    public void shallHaveStraightFor123456() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 2, 3, 4, 5, 6);
        boolean value = container.hasStraight();
        assertEquals(true, value);
    }

    @Test
    public void shallNotStraightFor133456() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 3, 3, 4, 5, 6);
        boolean value = container.hasStraight();
        assertEquals(false, value);
    }

    @Test
    public void shallReturn1000for212141() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(2, 1, 2, 1, 4, 1);
        int points = container.getPoints();
        assertEquals(1000, points);
    }

    @Test
    public void shallReturn150for133456() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 3, 3, 4, 5, 6);
        int points = container.getPoints();
        assertEquals(150, points);
    }

    @Test
    public void shallReturn200for135456() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 3, 5, 4, 5, 6);
        int points = container.getPoints();
        assertEquals(200, points);
    }

    @Test
    public void shallReturn2000for111111() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 1, 1, 1, 1, 1);
        int points = container.getPoints();
        assertEquals(2000, points);
    }

    @Test
    public void shallReturn1000for146211() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 4, 6, 2, 1, 1);
        int points = container.getPoints();
        assertEquals(1000, points);
    }

    @Test
    public void shallReturn1100for116211() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 1, 6, 2, 1, 1);
        int points = container.getPoints();
        assertEquals(1100, points);
    }

    @Test
    public void shallReturn1200for111211() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 1, 1, 2, 1, 1);
        int points = container.getPoints();
        assertEquals(1200, points);
    }

    @Test
    public void shallReturn1250for111511() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 1, 1, 5, 1, 1);
        int points = container.getPoints();
        assertEquals(1250, points);
    }

    @Test
    public void shallReturn1050for511641() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(5, 1, 1, 6, 4, 1);
        int points = container.getPoints();
        assertEquals(1050, points);
    }

    @Test
    public void shallReturn1100for511541() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(5, 1, 1, 5, 4, 1);
        int points = container.getPoints();
        assertEquals(1100, points);
    }

    @Test
    public void shallReturn200for222346() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(2, 2, 2, 3, 4, 6);
        int points = container.getPoints();
        assertEquals(200, points);
    }

    @Test
    public void shallReturn400for222222() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(2, 2, 2, 2, 2, 2);
        int points = container.getPoints();
        assertEquals(400, points);
    }
    @Test
    public void shallReturn300for234363() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(2, 3, 4, 3, 6, 3);
        int points = container.getPoints();
        assertEquals(300, points);
    }

    @Test
    public void shallReturn600for333333() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(3, 3, 3, 3, 3, 3);
        int points = container.getPoints();
        assertEquals(600, points);
    }

    @Test
    public void shallReturn400for244364() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(2, 4, 4, 3, 6, 4);
        int points = container.getPoints();
        assertEquals(400, points);
    }

    @Test
    public void shallReturn800for444444() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(4, 4, 4, 4, 4, 4);
        int points = container.getPoints();
        assertEquals(800, points);
    }

    @Test
    public void shallReturn1200for121131() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 2, 1, 1, 3, 1);
        int points = container.getPoints();
        assertEquals(1100, points);
    }

    @Test
    public void shallReturn500for552354() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(5, 5, 2, 3, 5, 4);
        int points = container.getPoints();
        assertEquals(500, points);
    }

    @Test
    public void shallReturn1000for555555() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(5, 5, 5, 5, 5, 5);
        int points = container.getPoints();
        assertEquals(1000, points);
    }

    @Test
    public void shallReturn600for664364() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(6, 6, 4, 3, 6, 4);
        int points = container.getPoints();
        assertEquals(600, points);
    }

    @Test
    public void shallReturn1200for666666() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(6, 6, 6, 6, 6, 6);
        int points = container.getPoints();
        assertEquals(1200, points);
    }

    @Test
    public void shallReturn300for214322() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(2, 1, 4, 3, 2, 2);
        int points = container.getPoints();
        assertEquals(300, points);
    }

    @Test
    public void shallReturn250for523224() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(5, 2, 3, 2, 2, 4);
        int points = container.getPoints();
        assertEquals(250, points);
    }

    @Test
    public void shallReturn400for122124() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 2, 2, 1, 2, 4);
        int points = container.getPoints();
        assertEquals(400, points);
    }

    @Test
    public void shallReturn700for525225() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(5, 2, 5, 2, 2, 5);
        int points = container.getPoints();
        assertEquals(700, points);
    }

    @Test
    public void shallReturn900for336636() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(3, 3, 6, 6, 3, 6);
        int points = container.getPoints();
        assertEquals(900, points);
    }

    @Test
    public void shallReturn1000for123456() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 2, 3, 4, 5, 6);
        int points = container.getPoints();
        assertEquals(1000, points);
    }

    @Test
    public void shallReturn1000for132456() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 3, 2, 4, 5, 6);
        int points = container.getPoints();
        assertEquals(1000, points);
    }

    @Test
    public void shallReturn1000for132654() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(1, 3, 2, 6, 5, 4);
        int points = container.getPoints();
        assertEquals(1000, points);
    }

    @Test
    public void shallReturn1000for352614() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(3, 5, 2, 6, 1, 4);
        int points = container.getPoints();
        assertEquals(1000, points);
    }

    @Test
    public void shallReturn750for416566() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(4, 1, 6, 5, 6, 6);
        int points = container.getPoints();
        assertEquals(750, points);
    }

    @Test
    public void shallReturn200for424211() throws Exception {
        DiceContainerModel container
                = createContainerFor6DicesWithSetValues(4, 2, 4, 2, 1, 1);
        int points = container.getPoints();
        assertEquals(200, points);
    }

    private DiceContainerModel createContainerFor6DicesWithSetValues(
            final int... val) throws Exception {

        DiceContainerModel diceContainerModel = new DiceContainerModel(val.length, 6);

        for(int i = 0; i < val.length; i++){
            diceContainerModel.setValueForDice(val[i],i);
        }


        return diceContainerModel;
    }
}