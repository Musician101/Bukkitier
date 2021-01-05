package io.musician101.bukkitier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public final class Bukkitier {

    private static final CommandDispatcher<CommandSender> DISPATCHER = new CommandDispatcher<>();

    private Bukkitier() {

    }

    /**
     * Registers a command.
     *
     * The command must still be manually added to plugin.yml for this to work properly.
     *
     * @param plugin The {@link Plugin} that the command is registered to.
     * @param builder The {@link LiteralArgumentBuilder<CommandSender>} that will be passed onto the {@link PluginCommand} as a {@link TabExecutor}
     */
    public static void registerCommand(@Nonnull Plugin plugin, @Nonnull LiteralArgumentBuilder<CommandSender> builder) {
        DISPATCHER.register(builder);
        PluginCommand pluginCommand = Bukkit.getPluginCommand(builder.getLiteral());
        if (pluginCommand == null || !plugin.equals(pluginCommand.getPlugin())) {
            throw new NullPointerException(builder.getLiteral() + " is not registered in " + plugin.getName() + "'s plugin.yml");
        }

        pluginCommand.setExecutor((sender, command, label, args) -> {
            try {
                return DISPATCHER.execute(Stream.concat(Stream.of(label), Stream.of(args)).collect(Collectors.joining(" ")), sender) >= 1;
            }
            catch (CommandSyntaxException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
                return false;
            }
        });
        pluginCommand.setTabCompleter((sender, command, alias, args) -> {
            String rawCommand = Stream.concat(Stream.of(alias), Stream.of(args)).collect(Collectors.joining(" "));
            ParseResults<CommandSender> parseResults = DISPATCHER.parse(rawCommand, sender);
            List<String> list = new ArrayList<>();
            DISPATCHER.getCompletionSuggestions(parseResults).thenAccept(suggestions -> suggestions.getList().stream().map(Suggestion::getText).forEach(list::add));
            return list;
        });
    }

    /**
     * Helper method to create a new instance of {@link LiteralArgumentBuilder} with {@link CommandSender} as it's generic type
     *
     * @param name The name of the command.
     * @return New instance of {@link LiteralArgumentBuilder}
     */
    public static LiteralArgumentBuilder<CommandSender> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    /**
     * Helper method to create a new instance of {@link LiteralArgumentBuilder<CommandSender>}
     *
     * @param <T> The type that the argument returns.
     * @param type The name of the command.
     * @return New instance of {@link RequiredArgumentBuilder}
     */
    public static <T> RequiredArgumentBuilder<CommandSender, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}
