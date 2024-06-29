package net.slimelabs.navigator;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Commands {

    public static class Lobby implements SimpleCommand {
        private final ProxyServer server;

        public Lobby(ProxyServer server) {
            this.server = server;
        }

        @Override
        public void execute(Invocation invocation) {
            CommandSource source = invocation.source();
            if (!(source instanceof Player)) {
                source.sendMessage(Component.text("§cOnly players can run this command."));
                return;
            }

            Player player = (Player) source;
            String[] args = invocation.arguments();
            final String targetServerName;
            if (args.length < 1) {
                targetServerName = "lobby";
            } else {
                switch (args[0]) {
                    case "main":
                        targetServerName = "lobby";
                        break;
                    case "minigames":
                        targetServerName = "minigames_lobby";
                        break;
                    default:
                        player.sendMessage(Component.text("§cIncorrect Command Usage"));
                        player.sendMessage(Component.text("§7Usage: /lobby <main|minigames>"));
                        return;
                }
            }

            if (player.getCurrentServer().isPresent() &&
                    player.getCurrentServer().get().getServer().getServerInfo().getName().equals(targetServerName)) {
                player.sendMessage(Component.text("§cYou are already connected to this server."));
                return;
            }

            Optional<RegisteredServer> targetServer = this.server.getServer(targetServerName);
            targetServer.ifPresentOrElse(
                    srv -> {
                        player.createConnectionRequest(srv).connect();
                        player.sendMessage(Component.text("§aConnecting to the " + targetServerName + "..."));
                    },
                    () -> player.sendMessage(Component.text("§cServer " + targetServerName + " not found."))
            );
        }

        @Override
        public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
            String[] args = invocation.arguments();
            if (args.length <= 1) {
                return CompletableFuture.completedFuture(List.of("main", "minigames"));
            }
            return CompletableFuture.completedFuture(List.of());
        }
    }

    public static class Back implements SimpleCommand {
        private final ProxyServer server;

        public Back(ProxyServer server) {
            this.server = server;
        }

        @Override
        public void execute(Invocation invocation) {
            CommandSource source = invocation.source();
            if (!(source instanceof Player)) {
                source.sendMessage(Component.text("§cOnly players can run this command."));
                return;
            }

            Player player = (Player) source;
            UUID playerUUID = player.getUniqueId();
            if (SL_Nav.lastServer.containsKey(playerUUID)) {
                final String serverName = SL_Nav.lastServer.get(playerUUID);
                if (player.getCurrentServer().isPresent() &&
                        player.getCurrentServer().get().getServer().getServerInfo().getName().equals(serverName)) {
                    player.sendMessage(Component.text("§cYou are already connected to this server."));
                    return;
                }

                Optional<RegisteredServer> server = this.server.getServer(serverName);
                server.ifPresentOrElse(
                        srv -> {
                            player.createConnectionRequest(srv).connect();
                            player.sendMessage(Component.text("§aConnecting to the previous server..."));
                        },
                        () -> player.sendMessage(Component.text("§cPrevious server not found."))
                );
            } else {
                player.sendMessage(Component.text("§3You don't have a previous server to return to."));
            }
        }
    }
}
