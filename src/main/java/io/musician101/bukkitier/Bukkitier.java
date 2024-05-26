package io.musician101.bukkitier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import io.musician101.bukkitier.command.LiteralCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class Bukkitier {

    private static final CommandDispatcher<CommandSender> DISPATCHER = new CommandDispatcher<>();

    private Bukkitier() {
    }

    /**
     * Helper method to create a new instance of {@link LiteralArgumentBuilder<CommandSender>}
     *
     * @param <T>  The type that the argument returns.
     * @param type The name of the command.
     * @return New instance of {@link RequiredArgumentBuilder}
     */
    public static <T> RequiredArgumentBuilder<CommandSender, T> argument(@NotNull String name, @NotNull ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    /**
     * Helper method to create a new instance of {@link LiteralArgumentBuilder} with {@link CommandSender} as it's generic type
     *
     * @param name The name of the command.
     * @return New instance of {@link LiteralArgumentBuilder}
     */
    public static LiteralArgumentBuilder<CommandSender> literal(@NotNull String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    /**
     * Registers a command.
     * <p>
     * The command must still be manually added to plugin.yml for this to work properly.
     *
     * @param plugin  The {@link JavaPlugin} that the command is registered to.
     * @param command The {@link LiteralCommand} that will be passed onto the {@link PluginCommand} as a {@link TabExecutor}
     */
    public static void registerCommand(@NotNull JavaPlugin plugin, @NotNull LiteralCommand command) {
        registerCommand(plugin, command.toBrigadier());
    }

    /**
     * Registers a command.
     * <p>
     * The command must still be manually added to plugin.yml for this to work properly.
     *
     * @param plugin  The {@link JavaPlugin} that the command is registered to.
     * @param builder The {@link LiteralArgumentBuilder<CommandSender>} that will be passed onto the {@link PluginCommand} as a {@link TabExecutor}.
     * @param aliases The aliases of the command, if any.
     */
    public static void registerCommand(@NotNull JavaPlugin plugin, @NotNull LiteralArgumentBuilder<CommandSender> builder, @NotNull String... aliases) {
        registerCommand(plugin, builder, Arrays.asList(aliases));
    }

    /**
     * Registers a command.
     * <p>
     * The command must still be manually added to plugin.yml for this to work properly.
     *
     * @param plugin  The {@link JavaPlugin} that the command is registered to.
     * @param builder The {@link LiteralArgumentBuilder<CommandSender>} that will be passed onto the {@link PluginCommand} as a {@link TabExecutor}.
     * @param aliases The aliases of the command, if any.
     */
    public static void registerCommand(@NotNull JavaPlugin plugin, @NotNull LiteralArgumentBuilder<CommandSender> builder, @NotNull List<String> aliases) {
        PluginCommand pluginCommand = plugin.getCommand(builder.getLiteral());
        if (pluginCommand == null || !plugin.equals(pluginCommand.getPlugin())) {
            if (!Bukkit.getName().equals("Paper")) {
                throw new NullPointerException(builder.getLiteral() + " is not registered in " + plugin.getName() + "'s plugin.yml");
            }

            Command command = getCommand(builder, aliases);
            Bukkit.getCommandMap().register(plugin.getName().toLowerCase(), command);
        }
        else {
            List<String> allAliases = Stream.concat(pluginCommand.getAliases().stream(), aliases.stream()).distinct().toList();
            pluginCommand.setAliases(allAliases);
            pluginCommand.setExecutor(Bukkitier::execute);
            pluginCommand.setTabCompleter(Bukkitier::tabComplete);
        }

        DISPATCHER.register(builder);
    }

    private static Command getCommand(LiteralArgumentBuilder<CommandSender> builder, List<String> aliases) {
        Command command = new Command(builder.getLiteral()) {

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                return Bukkitier.execute(sender, this, commandLabel, args);
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                return Bukkitier.tabComplete(sender, this, alias, args);
            }
        };

        command.setAliases(aliases);
        return command;
    }

    private static boolean execute(CommandSender sender, Command command, String label, String[] args) {
        try {
            String parsedArgs = args.length == 0 ? "" : " " + String.join(" ", args);
            return DISPATCHER.execute(command.getName() + parsedArgs, sender) > 0;
        }
        catch (CommandSyntaxException e) {
            sender.sendMessage(miniMessage().deserialize("<red>" + e.getMessage()));
            return false;
        }
    }

    private static List<String> tabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        String parsedArgs = args.length == 0 ? "" : " " + String.join(" ", args);
        String rawCommand = command.getName() + parsedArgs;
        ParseResults<CommandSender> parseResults = DISPATCHER.parse(rawCommand, sender);
        DISPATCHER.getCompletionSuggestions(parseResults).thenAccept(suggestions -> suggestions.getList().stream().map(Suggestion::getText).forEach(list::add));
        return list;
    }

    /**
     * Registers a command.
     * <p>
     * The command must still be manually added to plugin.yml for this to work properly.
     *
     * @param plugin  The {@link JavaPlugin} that the command is registered to.
     * @param command The {@link LiteralCommand} that will be passed onto the {@link PluginCommand} as a {@link TabExecutor}
     * @param aliases The aliases for the command
     */
    public static void registerCommand(@NotNull JavaPlugin plugin, @NotNull LiteralCommand command, @NotNull String... aliases) {
        registerCommand(plugin, command.toBrigadier(), aliases);
    }

    /**
     * Registers a command.
     * <p>
     * The command must still be manually added to plugin.yml for this to work properly.
     *
     * @param plugin  The {@link JavaPlugin} that the command is registered to.
     * @param command The {@link LiteralCommand} that will be passed onto the {@link PluginCommand} as a {@link TabExecutor}
     * @param aliases The aliases for the command
     */
    public static void registerCommand(@NotNull JavaPlugin plugin, @NotNull LiteralCommand command, @NotNull List<String> aliases) {
        registerCommand(plugin, command.toBrigadier(), aliases);
    }
}
