package io.musician101.bukkitier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.musician101.bukkitier.command.LiteralCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bukkitier {

    private static final CommandDispatcher<CommandSender> DISPATCHER = new CommandDispatcher<>();

    private Bukkitier() {

    }

    /**
     * Registers a command.
     * <p>
     * The command must still be manually added to plugin.yml for this to work properly.
     *
     * @param plugin The {@link JavaPlugin} that the command is registered to.
     * @param command The {@link LiteralCommand} that will be passed onto the {@link PluginCommand} as a {@link TabExecutor}
     */
    public static void registerCommand(@Nonnull JavaPlugin plugin, @Nonnull LiteralCommand command) {
        registerCommand(plugin, command.toBrigadier(), command.aliases().toArray(new String[0]));
    }

    /**
     * Registers a command.
     * <p>
     * The command must still be manually added to plugin.yml for this to work properly.
     *
     * @param plugin The {@link JavaPlugin} that the command is registered to.
     * @param builder The {@link LiteralArgumentBuilder<CommandSender>} that will be passed onto the {@link PluginCommand} as a {@link TabExecutor}.
     * @param aliases The aliases of the command, if any.
     */
    public static void registerCommand(@Nonnull JavaPlugin plugin, @Nonnull LiteralArgumentBuilder<CommandSender> builder, @Nonnull String... aliases) {
        PluginCommand pluginCommand = plugin.getCommand(builder.getLiteral());
        if (pluginCommand == null || !plugin.equals(pluginCommand.getPlugin())) {
            throw new NullPointerException(builder.getLiteral() + " is not registered in " + plugin.getName() + "'s plugin.yml");
        }

        DISPATCHER.register(builder);
        LiteralCommandNode<CommandSender> lcn = builder.build();
        Arrays.stream(aliases).forEach(alias -> DISPATCHER.register(literal(alias).redirect(lcn)));
        pluginCommand.setExecutor((sender, command, label, args) -> {
            try {
                String parsedArgs = args.length == 0 ? "" : " " + String.join(" ", args);
                return DISPATCHER.execute(command.getName() + parsedArgs, sender) > 0;
            }
            catch (CommandSyntaxException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
                return false;
            }
        });

        pluginCommand.setTabCompleter((sender, command, alias, args) -> {
            List<String> list = new ArrayList<>();
            String parsedArgs = args.length == 0 ? "" : " " + String.join(" ", args);
            String rawCommand = command.getName() + parsedArgs;
            ParseResults<CommandSender> parseResults = DISPATCHER.parse(rawCommand, sender);
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
    public static LiteralArgumentBuilder<CommandSender> literal(@Nonnull String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    /**
     * Helper method to create a new instance of {@link LiteralArgumentBuilder<CommandSender>}
     *
     * @param <T> The type that the argument returns.
     * @param type The name of the command.
     * @return New instance of {@link RequiredArgumentBuilder}
     */
    public static <T> RequiredArgumentBuilder<CommandSender, T> argument(@Nonnull String name, @Nonnull ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}
