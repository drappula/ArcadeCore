package org.drappula.arcadeCore.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.drappula.arcadeApi.systems.game.Game;
import org.drappula.arcadeApi.systems.map.IArcadeMap;
import org.drappula.arcadeCore.ArcadeCore;
import org.drappula.arcadeCore.config.DataConfig;
import org.drappula.arcadeCore.config.MessagesConfig;
import org.drappula.arcadeCore.managers.game.GameManager;
import org.drappula.arcadeCore.managers.map.MapManager;
import org.drappula.arcadeCore.managers.queue.QueueManager;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class MainCommand {
    public static LiteralCommandNode<CommandSourceStack> get() {
        return Commands.literal("arcade")
                .executes(MainCommand::info)
                .then(Commands.literal("reload").executes(MainCommand::reload))
                .then(Commands.literal("setspawn").executes(MainCommand::setSpawn))
                .then(
                        Commands.literal("start")
                                .requires(ctx -> ctx.getSender().hasPermission("arcade.force-start"))
                                .then(Commands.argument("game", StringArgumentType.word())
                                        .executes(MainCommand::start)
                                        .suggests(MainCommand::suggestGames))
                )
                .then(
                        Commands.literal("queue")
                                .then(Commands.argument("game", StringArgumentType.word())
                                                .executes(MainCommand::queue)
                                                .suggests(MainCommand::suggestGames)))
                .then(
                        Commands.literal("map")
                                .requires(ctx -> ctx.getSender().hasPermission("arcade.map.admin"))
                                .then(Commands.literal("create")
                                        .then(Commands.argument("mapId", StringArgumentType.word())
                                                .then(Commands.argument("game", StringArgumentType.word())
                                                        .executes(MainCommand::mapCreate)
                                                        .suggests(MainCommand::suggestGames))))
                                .then(Commands.literal("addspawn")
                                        .then(Commands.argument("mapId", StringArgumentType.word())
                                                .executes(MainCommand::mapAddSpawn)
                                                .suggests(MainCommand::suggestMaps)))
                                .then(Commands.literal("enable")
                                        .then(Commands.argument("mapId", StringArgumentType.word())
                                                .executes(ctx -> mapSetEnabled(ctx, true))
                                                .suggests(MainCommand::suggestMaps)))
                                .then(Commands.literal("disable")
                                        .then(Commands.argument("mapId", StringArgumentType.word())
                                                .executes(ctx -> mapSetEnabled(ctx, false))
                                                .suggests(MainCommand::suggestMaps)))
                                .then(Commands.literal("list")
                                        .executes(MainCommand::mapList)
                                        .then(Commands.argument("game", StringArgumentType.word())
                                                .executes(MainCommand::mapList)
                                                .suggests(MainCommand::suggestGames)))
                                .then(Commands.literal("delete")
                                        .then(Commands.argument("mapId", StringArgumentType.word())
                                                .executes(MainCommand::mapDelete)
                                                .suggests(MainCommand::suggestMaps)))
                )
                .build();
    }

    private static CompletableFuture<Suggestions> suggestMaps(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        for (Game game : GameManager.get().getGames().values()) {
            for (IArcadeMap map : MapManager.get().getMaps(game.getId())) {
                builder.suggest(map.getId());
            }
        }
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestGames(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        for (Game game : GameManager.get().getGames().values()) {
            builder.suggest(game.getId());
        }
        return builder.buildFuture();
    }

    private static int start(CommandContext<CommandSourceStack> ctx) {
        String gameId = StringArgumentType.getString(ctx, "game");
        Game game = GameManager.get().getGame(gameId);
        if (game == null) {
            ctx.getSource().getSender().sendRichMessage("<red>Unknown game: <gray><id></gray>",
                    TagResolver.resolver(Placeholder.unparsed("id", gameId)));
            return Command.SINGLE_SUCCESS;
        }
        if (!QueueManager.get().forceStart(game)) {
            ctx.getSource().getSender().sendRichMessage("<red>No players are queued for that game.");
        }
        return Command.SINGLE_SUCCESS;
    }
    private static int queue(CommandContext<CommandSourceStack> ctx) {
        String gameId = StringArgumentType.getString(ctx, "game");
        Game game = GameManager.get().getGame(gameId);
        if (game == null) {
            ctx.getSource().getSender().sendRichMessage(MessagesConfig.get().getString("bad-arguments.unknown-game"),
                    TagResolver.resolver(Placeholder.unparsed("id", gameId)));
            return Command.SINGLE_SUCCESS;
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int mapCreate(CommandContext<CommandSourceStack> ctx) {
        String mapId = StringArgumentType.getString(ctx, "mapId");
        String gameId = StringArgumentType.getString(ctx, "game");
        Game game = GameManager.get().getGame(gameId);
        if (game == null) {
            ctx.getSource().getSender().sendRichMessage(MessagesConfig.get().getString("bad-arguments.unknown-game"),
                    TagResolver.resolver(Placeholder.unparsed("id", gameId)));
            return Command.SINGLE_SUCCESS;
        }
        try {
            MapManager.get().createMap(mapId, gameId, mapId, ctx.getSource().getLocation().getWorld().getName());
            ctx.getSource().getSender().sendRichMessage(MessagesConfig.get().getString("map-created"),
                    TagResolver.resolver(Placeholder.unparsed("id", mapId), Placeholder.unparsed("game", gameId)));
        } catch (SQLException e) {
            ctx.getSource().getSender().sendRichMessage("<red>Failed to create the map. See the console for more details.");
            ArcadeCore.get().getSLF4JLogger().error("Failed to create map {}", mapId, e);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int mapAddSpawn(CommandContext<CommandSourceStack> ctx) {
        String mapId = StringArgumentType.getString(ctx, "mapId");
        if (MapManager.get().getMap(mapId) == null) {
            ctx.getSource().getSender().sendRichMessage(MessagesConfig.get().getString("map-not-found"),
                    TagResolver.resolver(Placeholder.unparsed("id", mapId)));
            return Command.SINGLE_SUCCESS;
        }
        try {
            MapManager.get().addSpawn(mapId, ctx.getSource().getLocation());
            ctx.getSource().getSender().sendRichMessage("<green>Added a spawn point to map <gray><id></gray>.",
                    TagResolver.resolver(Placeholder.unparsed("id", mapId)));
        } catch (SQLException e) {
            ctx.getSource().getSender().sendRichMessage("<red>Failed to add the spawn point. See the console for more details.");
            ArcadeCore.get().getSLF4JLogger().error("Failed to add a spawn point to map {}", mapId, e);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int mapSetEnabled(CommandContext<CommandSourceStack> ctx, boolean enabled) {
        String mapId = StringArgumentType.getString(ctx, "mapId");
        if (MapManager.get().getMap(mapId) == null) {
            ctx.getSource().getSender().sendRichMessage(MessagesConfig.get().getString("map-not-found"),
                    TagResolver.resolver(Placeholder.unparsed("id", mapId)));
            return Command.SINGLE_SUCCESS;
        }
        try {
            MapManager.get().setEnabled(mapId, enabled);
            ctx.getSource().getSender().sendRichMessage(enabled ? "<green>Map <gray><id></gray> enabled." : "<yellow>Map <gray><id></gray> disabled.",
                    TagResolver.resolver(Placeholder.unparsed("id", mapId)));
        } catch (SQLException e) {
            ctx.getSource().getSender().sendRichMessage("<red>Failed to update the map. See the console for more details.");
            ArcadeCore.get().getSLF4JLogger().error("Failed to set enabled={} on map {}", enabled, mapId, e);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int mapList(CommandContext<CommandSourceStack> ctx) {
        String gameId;
        try {
            gameId = StringArgumentType.getString(ctx, "game");
        } catch (IllegalArgumentException e) {
            gameId = null;
        }
        StringBuilder message = new StringBuilder("<aqua>Maps:</aqua>");
        for (Game game : GameManager.get().getGames().values()) {
            if (gameId != null && !game.getId().equalsIgnoreCase(gameId)) continue;
            for (IArcadeMap map : MapManager.get().getMaps(game.getId())) {
                message.append("<br><gray> - ").append(map.getId()).append(" (").append(game.getId()).append(")")
                        .append(map.isEnabled() ? "" : " <red>[disabled]</red>")
                        .append(map.isInUse() ? " <yellow>[in use]</yellow>" : "");
            }
        }
        ctx.getSource().getSender().sendRichMessage(message.toString());
        return Command.SINGLE_SUCCESS;
    }

    private static int mapDelete(CommandContext<CommandSourceStack> ctx) {
        String mapId = StringArgumentType.getString(ctx, "mapId");
        if (MapManager.get().getMap(mapId) == null) {
            ctx.getSource().getSender().sendRichMessage(MessagesConfig.get().getString("map-not-found"),
                    TagResolver.resolver(Placeholder.unparsed("id", mapId)));
            return Command.SINGLE_SUCCESS;
        }
        try {
            MapManager.get().deleteMap(mapId);
            ctx.getSource().getSender().sendRichMessage("<green>Deleted map <gray><id></gray>.",
                    TagResolver.resolver(Placeholder.unparsed("id", mapId)));
        } catch (SQLException e) {
            ctx.getSource().getSender().sendRichMessage("<red>Failed to delete the map. See the console for more details.");
            ArcadeCore.get().getSLF4JLogger().error("Failed to delete map {}", mapId, e);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int info(CommandContext<CommandSourceStack> ctx) {
        String name = ArcadeCore.get().getPluginMeta().getDisplayName();
        String description = ArcadeCore.get().getPluginMeta().getDescription();
        String version = ArcadeCore.get().getPluginMeta().getVersion();
        ctx.getSource().getSender().sendRichMessage(
                "<aqua><b><name></b></aqua> <dark_gray><i>(v<version>)</i></dark_gray>" + "<br>" +
                        "<gray><description>" + "<br>",
                TagResolver.resolver(
                        Placeholder.unparsed("name", name),
                        Placeholder.unparsed("description", description == null ? "" : description),
                        Placeholder.unparsed("version", version)
                ));
        return Command.SINGLE_SUCCESS;
    }
    private static int reload(CommandContext<CommandSourceStack> ctx) {
        try {
            DataConfig.get().reload();
            ctx.getSource().getSender().sendRichMessage("<green>Reloaded the plugin configuration.");
        } catch (Exception e) {
            ctx.getSource().getSender().sendRichMessage("<red>Failed to reload the plugin! See the console for more details.");
            throw new RuntimeException(e);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setSpawn(CommandContext<CommandSourceStack> ctx) {
        try {
            DataConfig.setSpawnLocation(ctx.getSource().getLocation());
            ctx.getSource().getSender().sendRichMessage("<green>Spawn location updated.");
        } catch (Exception e) {
            ctx.getSource().getSender().sendRichMessage("<red>An error occurred while trying to save spawn location. See the console for more details.");
            ArcadeCore.get().getSLF4JLogger().error("An error occurred while trying to save spawn location", e);
        }
        return Command.SINGLE_SUCCESS;
    }
}
