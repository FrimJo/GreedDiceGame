package com.fredrikux.greed.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void shallReturnRightName(){
        Player p = new Player("Pelle");
        String name = p.getName();
        assertEquals("Pelle", name);
    }

    @Test
    public void shallSetStartingPointsToZero(){
        Player p = new Player("Pelle");
        int points = p.getPoints();
        assertEquals(0, points);
    }

    @Test(expected = Exception.class)
    public void shallThrowExceptionForNegativePoints()
            throws ArithmeticException {
        Player p = new Player("Pelle");
        p.incrementPoints(-10);
    }

    @Test
    public void addPointsToPlayer()
            throws ArithmeticException {
        Player p = new Player("Pelle");
        p.incrementPoints(10);
        int points = p.getPoints();
        assertEquals(10, points);
    }
}