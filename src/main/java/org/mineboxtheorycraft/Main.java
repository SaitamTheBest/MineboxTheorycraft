package org.mineboxtheorycraft;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

public class Main {
    public static void main(String[] args) {
        DiscordApi api = new DiscordApiBuilder()
                .setToken(args[0])
                .addIntents(Intent.MESSAGE_CONTENT)
                .login().join();

    }
}