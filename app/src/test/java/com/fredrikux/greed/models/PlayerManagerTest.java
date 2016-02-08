package com.fredrikux.greed.models;

import android.support.annotation.NonNull;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerManagerTest {

    @Test
    public void shallHave1ActivePlayersForAdd1Player() throws
            Exception {
        PlayerManager playerManager = new PlayerManager();
        playerManager.addPlayer("Lisa");
        int nrPlayers = playerManager.getNrPlayers();
        assertEquals(1, nrPlayers);
    }

    @Test
    public void shallHave2ActivePlayersForAdd2PlayerAndSpaceFor3() throws
            Exception {
        PlayerManager playerManager = new PlayerManager();
        playerManager.addPlayer("Lisa");
        playerManager.addPlayer("Bert");
        int nrPlayers = playerManager.getNrPlayers();
        assertEquals(2, nrPlayers);
    }

    @Test
    public void shouldHave3ActivePlayersForAdd3PlayerAndSpaceFor3() throws Exception {
        PlayerManager playerManager = createPlayerManagerFor3PlayersAndAddThem();
        int nrPlayers = playerManager.getNrPlayers();
        assertEquals(3, nrPlayers);
    }

    @Test
    public void shallHaveLisaAsCurrentPlayer() throws Exception {
        PlayerManager playerManager = createPlayerManagerFor3PlayersAndAddThem();
        String name = playerManager.getCurrentPlayer().getName();
        assertEquals("Lisa", name);
    }

    @Test
    public void shallHaveBertAsCurrentPlayerForNext1Time() throws Exception {
        PlayerManager playerManager = createPlayerManagerFor3PlayersAndAddThem();
        playerManager.setNextPlayer();
        String name = playerManager.getCurrentPlayer().getName();
        assertEquals("Bert", name);
    }

    @Test
    public void shallHaveErikAsCurrentPlayerForNext2Times() throws Exception {
        PlayerManager playerManager = createPlayerManagerFor3PlayersAndAddThem();
        playerManager.setNextPlayer();
        playerManager.setNextPlayer();
        String name = playerManager.getCurrentPlayer().getName();
        assertEquals("Erik", name);
    }

    @Test
    public void shallHaveLisaAsCurrentPlayerForNext3Times() throws Exception {
        PlayerManager playerManager = createPlayerManagerFor3PlayersAndAddThem();
        playerManager.setNextPlayer();
        playerManager.setNextPlayer();
        playerManager.setNextPlayer();
        String name = playerManager.getCurrentPlayer().getName();
        assertEquals("Lisa", name);
    }

    @NonNull
    private PlayerManager createPlayerManagerFor3PlayersAndAddThem() throws Exception {
        PlayerManager playerManager = new PlayerManager();
        playerManager.addPlayer("Lisa");
        playerManager.addPlayer("Bert");
        playerManager.addPlayer("Erik");
        return playerManager;
    }

}