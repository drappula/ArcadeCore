package org.drappula.arcadeCore.managers.game;

import org.bukkit.entity.Player;
import org.drappula.arcadeApi.systems.game.GameState;
import org.drappula.arcadeApi.systems.game.IGame;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static final List<IGame> games = new ArrayList<>();

    public static List<IGame> getGames() {
        return games;
    }

    public static Game initGame(List<Player> players) {
        Game game = new Game(players);
        game.setState(GameState.STARTING);
        addGame(game);
        return game;
    }

    public static void addGame(IGame game) {
        games.add(game);
    }

    public static void removeGame(IGame game) {
        games.remove(game);
    }
}
