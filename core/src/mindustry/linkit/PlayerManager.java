package mindustry.linkit;

import mindustry.Vars;
import mindustry.gen.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private Map<String, Player> players = new HashMap<>();

    public Player setCurrentPlayer(Player player) {
        players.put(player.name, player);
        Vars.player.remove();
        Vars.player = player;
        Vars.player.added = false;
        Vars.player.add();
        return player;
    }

    public void addOtherPlayers() {
        players.forEach((name, player) -> {
            if (Vars.player != player) player.add();
        });
    }

}
