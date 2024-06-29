package net.slimelabs.navigator;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.google.inject.Inject;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.UUID;

@Plugin(
        id = "navigator",
        name = "SL_Nav",
        version = "v2",
        description = "Server Navigation plugin. Version 2.0 by protoxon & Yeetoxic",
        authors = {"protoxon & Yeetoxic"}
)
public class SL_Nav {

    public static ProxyServer PROXY;
    public static HashMap<UUID, String> lastServer = new HashMap<>();
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public SL_Nav(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        PROXY = server;
        Listener listener = new Listener();  // Instantiate Listener
        server.getEventManager().register(this, listener);  // Register Listener

        String RESET = "\u001B[0m";
        String YELLOW = "\u001B[33m";
        String GREEN = "\u001B[32m";
        String CYAN = "\u001B[36m";
        String BLUE = "\u001B[34m";
        logger.info(CYAN + " \n" + CYAN + "————————————————————————————————————————————————\n" +
                GREEN + "   _____ _          _   _     __      _______ _____       _______ ____  _____  \n" +
                GREEN + "  / ____| |        | \\ | |   /\\ \\    / /_   _/ ____|   /\\|__   __/ __ \\|  __ \\ \n" +
                GREEN + " | (___ | |  ______|  \\| |  /  \\ \\  / /  | || |  __   /  \\  | | | |  | | |__) |\n" +
                GREEN + "  \\___ \\| | |______| . ` | / /\\ \\ \\/ /   | || | |_ | / /\\ \\ | | | |  | |  _  / \n" +
                GREEN + "  ____) | |____    | |\\  |/ ____ \\  /   _| || |__| |/ ____ \\| | | |__| | | \\ \\ \n" +
                GREEN + " |_____/|______|   |_| \\_/_/    \\_\\/   |_____\\_____/_/    \\_\\_|  \\____/|_|  \\_\\\n" + RESET +
                "\n" +
                "[" + GREEN + "SL_NAV" + RESET + "]" + CYAN + " SlimeLabs Server Navigation Plugin " + RESET + YELLOW + "Version 2.0" + RESET + CYAN +" by " + RESET + BLUE + "protoxon" + RESET + CYAN + " & " + RESET + BLUE + "Yeetoxic" + RESET + "\n" +
                " \n" + CYAN + "————————————————————————————————————————————————");

        server.getCommandManager().register(server.getCommandManager().metaBuilder("lobby").aliases("hub").build(), new Commands.Lobby(server));
        server.getCommandManager().register(server.getCommandManager().metaBuilder("back").build(), new Commands.Back(server));

        logger.info("Commands registered successfully.");
    }
}
