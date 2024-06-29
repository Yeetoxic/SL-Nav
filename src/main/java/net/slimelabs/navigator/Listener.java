package net.slimelabs.navigator;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;

import java.util.UUID;

public class Listener {

    @Subscribe
    public void onServerConnect(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        if (player != null && player.getCurrentServer().isPresent()) {
            UUID playerUUID = player.getUniqueId();
            String fromServer = player.getCurrentServer().get().getServer().getServerInfo().getName();
            if (fromServer.equals("lobby") || fromServer.equals("minigames_lobby")) {
                SL_Nav.lastServer.put(playerUUID, fromServer);
            }
        }
    }
}
