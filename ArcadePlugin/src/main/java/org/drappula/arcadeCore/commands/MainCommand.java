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
import org.drappula.arcadeCore.ArcadeCore;
import org.drappula.arcadeCore.config.DataConfig;
import org.drappula.arcadeCore.config.MessagesConfig;
import org.drappula.arcadeCore.managers.game.GameManager;
import org.drappula.arcadeCore.managers.queue.QueueManager;

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
                .build();
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
        } catch (Exception e) {
            ctx.getSource().getSender().sendRichMessage("<red>Failed to reload the plugin! See the console for more details.");
            throw new RuntimeException(e);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setSpawn(CommandContext<CommandSourceStack> ctx) {
        try {
            DataConfig.setSpawnLocation(ctx.getSource().getLocation());
        } catch (Exception e) {
            ctx.getSource().getSender().sendRichMessage("<red>An error occurred while trying to save spawn location. See the console for more details.");
            ArcadeCore.get().getSLF4JLogger().error("An error occurred while trying to save spawn location", e);
        }
        return Command.SINGLE_SUCCESS;
    }
}
